import { createClient } from "npm:@supabase/supabase-js@2.50.0";

const corsHeaders = {
  "Access-Control-Allow-Origin": "*",
  "Access-Control-Allow-Methods": "GET, POST, PUT, DELETE, OPTIONS",
  "Access-Control-Allow-Headers": "Content-Type, Authorization, X-Client-Info, Apikey",
};

interface EnvVars {
  SUPABASE_URL: string;
  SUPABASE_SERVICE_ROLE_KEY: string;
  FIREBASE_API_KEY: string;
  GEMINI_API_KEY: string;
}

async function verifyFirebaseToken(idToken: string, firebaseApiKey: string): Promise<string | null> {
  const url = `https://identitytoolkit.googleapis.com/v1/accounts:lookup?key=${firebaseApiKey}`;
  try {
    const resp = await fetch(url, {
      method: "POST",
      headers: { "Content-Type": "application/json" },
      body: JSON.stringify({ idToken }),
    });
    if (!resp.ok) return null;
    const data = await resp.json();
    if (data.users && data.users.length > 0 && data.users[0].localId) {
      return data.users[0].localId as string;
    }
    return null;
  } catch {
    return null;
  }
}

function jsonResponse(body: unknown, status = 200): Response {
  return new Response(JSON.stringify(body), {
    status,
    headers: { ...corsHeaders, "Content-Type": "application/json" },
  });
}

function errorResponse(message: string, status: number): Response {
  return jsonResponse({ error: message }, status);
}

Deno.serve(async (req: Request) => {
  if (req.method === "OPTIONS") {
    return new Response(null, { status: 200, headers: corsHeaders });
  }

  const env: EnvVars = {
    SUPABASE_URL: Deno.env.get("SUPABASE_URL")!,
    SUPABASE_SERVICE_ROLE_KEY: Deno.env.get("SUPABASE_SERVICE_ROLE_KEY")!,
    FIREBASE_API_KEY: Deno.env.get("FIREBASE_API_KEY")!,
    GEMINI_API_KEY: Deno.env.get("GEMINI_API_KEY") ?? "",
  };

  const supabase = createClient(env.SUPABASE_URL, env.SUPABASE_SERVICE_ROLE_KEY, {
    auth: { persistSession: false, autoRefreshToken: false },
  });

  const url = new URL(req.url);
  const path = url.pathname;
  const method = req.method;

  // Strip /functions/v1/sattva-api prefix if present
  const cleanPath = path.replace(/^\/functions\/v1\/sattva-api/, "");

  try {
    // --- Public routes ---

    if (cleanPath === "/health" || cleanPath === "/api/v1/health" || cleanPath === "/") {
      return jsonResponse({
        status: "healthy",
        service: "sattva-api",
        endpoints: ["/api/v1/catalog/gaushalas", "/api/v1/catalog/animals", "/api/v1/catalog/pujas", "/api/v1/welfare", "/api/v1/ai/ask", "/api/v1/profile", "/api/v1/donations", "/api/v1/bookings", "/api/v1/family"],
      });
    }

    // Gaushalas
    if (cleanPath === "/api/v1/catalog/gaushalas" && method === "GET") {
      let query = supabase.from("gaushalas").select("*").eq("is_active", true);
      const city = url.searchParams.get("city");
      if (city) {
        query = query.ilike("city", `%${city}%`);
      }
      const { data, error } = await query.order("created_at", { ascending: false });
      if (error) return errorResponse(error.message, 500);
      return jsonResponse({ gaushalas: data, count: data.length });
    }

    // Animals
    if (cleanPath === "/api/v1/catalog/animals" && method === "GET") {
      let query = supabase.from("animals").select("*");
      const gaushalaId = url.searchParams.get("gaushalaId");
      if (gaushalaId) {
        query = query.eq("gaushala_id", gaushalaId);
      }
      const { data, error } = await query.order("created_at", { ascending: false });
      if (error) return errorResponse(error.message, 500);
      return jsonResponse({ animals: data, count: data.length });
    }

    // Pujas
    if (cleanPath === "/api/v1/catalog/pujas" && method === "GET") {
      let query = supabase.from("pujas").select("*");
      const category = url.searchParams.get("category");
      const search = url.searchParams.get("search");
      if (category && category !== "All") {
        query = query.ilike("category", category);
      }
      if (search) {
        query = query.or(`title.ilike.%${search}%,temple_name.ilike.%${search}%,location.ilike.%${search}%`);
      }
      const { data, error } = await query.order("created_at", { ascending: false });
      if (error) return errorResponse(error.message, 500);
      return jsonResponse({ pujas: data, count: data.length });
    }

    // Welfare stats
    if (cleanPath === "/api/v1/welfare" && method === "GET") {
      const { data: gaushalas, error: gErr } = await supabase.from("gaushalas").select("animals_rescued_count").eq("is_active", true);
      if (gErr) return errorResponse(gErr.message, 500);
      const totalRescued = (gaushalas || []).reduce((sum: number, g: { animals_rescued_count: number }) => sum + (g.animals_rescued_count || 0), 0);
      return jsonResponse({
        totalRescued,
        activeSanctuaries: (gaushalas || []).length,
        totalMealsServed: totalRescued * 30,
      });
    }

    // Welfare updates
    if (cleanPath === "/api/v1/catalog/welfare_updates" && method === "GET") {
      let query = supabase.from("welfare_updates").select("*");
      const gaushalaId = url.searchParams.get("gaushalaId");
      const animalId = url.searchParams.get("animalId");
      if (gaushalaId) query = query.eq("gaushala_id", gaushalaId);
      if (animalId) query = query.eq("animal_id", animalId);
      const { data, error } = await query.order("created_at", { ascending: false });
      if (error) return errorResponse(error.message, 500);
      return jsonResponse({ updates: data, count: data.length });
    }

    // AI Ask (public, uses Gemini)
    if (cleanPath === "/api/v1/ai/ask" && method === "POST") {
      if (!env.GEMINI_API_KEY) return errorResponse("Gemini API Key missing on server", 500);
      const body = await req.json();
      const queryText = body.query;
      if (!queryText) return errorResponse("Field 'query' is required", 400);

      const aiUrl = `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=${env.GEMINI_API_KEY}`;
      const aiResp = await fetch(aiUrl, {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify({ contents: [{ parts: [{ text: queryText }] }] }),
      });
      if (!aiResp.ok) {
        const errText = await aiResp.text();
        return errorResponse(`Gemini API error: ${errText}`, 500);
      }
      const aiData = await aiResp.json();
      const text = aiData.candidates?.[0]?.content?.parts?.[0]?.text || "No response generated.";
      return jsonResponse({ response: text });
    }

    // --- Authenticated routes ---
    const isAuthRoute = cleanPath.startsWith("/api/v1/profile") ||
                        cleanPath.startsWith("/api/v1/donations") ||
                        cleanPath.startsWith("/api/v1/bookings") ||
                        cleanPath.startsWith("/api/v1/family");

    let firebaseUid: string | null = null;

    if (isAuthRoute) {
      const authHeader = req.headers.get("Authorization");
      if (!authHeader || !authHeader.startsWith("Bearer ")) {
        return errorResponse("Unauthorized: Missing authentication token", 401);
      }
      const token = authHeader.substring(7);
      firebaseUid = await verifyFirebaseToken(token, env.FIREBASE_API_KEY);
      if (!firebaseUid) {
        return errorResponse("Unauthorized: Invalid or expired authentication token", 401);
      }
    }

    // Profile GET
    if (cleanPath === "/api/v1/profile" && method === "GET") {
      const { data, error } = await supabase.from("profiles").select("*").eq("firebase_uid", firebaseUid).maybeSingle();
      if (error) return errorResponse(error.message, 500);
      if (!data) {
        // Auto-create a profile on first access
        const { data: newProfile, error: createErr } = await supabase
          .from("profiles")
          .insert({ firebase_uid: firebaseUid, display_name: "Seeker" })
          .select("*")
          .single();
        if (createErr) return errorResponse(createErr.message, 500);
        return jsonResponse({ profile: newProfile });
      }
      return jsonResponse({ profile: data });
    }

    // Profile PUT
    if (cleanPath === "/api/v1/profile" && method === "PUT") {
      const body = await req.json();
      const updates: Record<string, string> = {};
      if (typeof body.display_name === "string") updates.display_name = body.display_name;
      if (typeof body.city === "string") updates.city = body.city;
      if (typeof body.photo_url === "string") updates.photo_url = body.photo_url;
      updates.updated_at = new Date().toISOString();

      const { data: existing } = await supabase.from("profiles").select("id").eq("firebase_uid", firebaseUid).maybeSingle();

      if (existing) {
        const { error: updateErr } = await supabase.from("profiles").update(updates).eq("firebase_uid", firebaseUid);
        if (updateErr) return errorResponse(updateErr.message, 500);
      } else {
        const { error: insertErr } = await supabase
          .from("profiles")
          .insert({ firebase_uid: firebaseUid!, ...updates });
        if (insertErr) return errorResponse(insertErr.message, 500);
      }
      return jsonResponse({ success: true });
    }

    // Donations GET
    if (cleanPath === "/api/v1/donations" && method === "GET") {
      const { data, error } = await supabase
        .from("seva_contributions")
        .select("*")
        .eq("firebase_uid", firebaseUid)
        .order("created_at", { ascending: false });
      if (error) return errorResponse(error.message, 500);
      return jsonResponse({ donations: data });
    }

    // Donations POST
    if (cleanPath === "/api/v1/donations" && method === "POST") {
      const body = await req.json();
      const { data, error } = await supabase
        .from("seva_contributions")
        .insert({
          firebase_uid: firebaseUid,
          target_type: body.targetType || "GAUSHALA",
          target_id: body.targetId || null,
          target_name: body.targetName || "",
          amount_rupees: body.amountRupees || 0,
          seva_category: body.sevaCategory || "",
          payment_status: "PENDING",
        })
        .select("id")
        .single();
      if (error) return errorResponse(error.message, 500);
      return jsonResponse({ id: data.id, status: "PENDING" }, 201);
    }

    // Bookings GET
    if (cleanPath === "/api/v1/bookings" && method === "GET") {
      const { data, error } = await supabase
        .from("puja_bookings")
        .select("*")
        .eq("firebase_uid", firebaseUid)
        .order("created_at", { ascending: false });
      if (error) return errorResponse(error.message, 500);
      return jsonResponse({ bookings: data });
    }

    // Bookings POST
    if (cleanPath === "/api/v1/bookings" && method === "POST") {
      const body = await req.json();
      const { data, error } = await supabase
        .from("puja_bookings")
        .insert({
          firebase_uid: firebaseUid,
          puja_id: body.pujaId || "",
          devotee_name: body.devoteeName || "",
          gotra: body.gotra || "",
          scheduled_date_str: body.scheduledDateStr || "",
          ai_generated_sankalpa: body.aiGeneratedSankalpa || "",
          status: "PENDING",
          payment_status: "PENDING",
        })
        .select("id")
        .single();
      if (error) return errorResponse(error.message, 500);
      return jsonResponse({ id: data.id, status: "PENDING" }, 201);
    }

    // Family GET
    if (cleanPath === "/api/v1/family" && method === "GET") {
      const { data, error } = await supabase
        .from("family_members")
        .select("*")
        .eq("firebase_uid", firebaseUid)
        .order("created_at", { ascending: false });
      if (error) return errorResponse(error.message, 500);
      return jsonResponse({ family: data });
    }

    // Family POST
    if (cleanPath === "/api/v1/family" && method === "POST") {
      const body = await req.json();
      const { data, error } = await supabase
        .from("family_members")
        .insert({
          firebase_uid: firebaseUid,
          name: body.name || "",
          relation: body.relation || "",
          gotra: body.gotra || "",
          nakshatra: body.nakshatra || "",
        })
        .select("id")
        .single();
      if (error) return errorResponse(error.message, 500);
      return jsonResponse({ id: data.id }, 201);
    }

    return errorResponse("Not Found", 404);
  } catch (err) {
    const message = err instanceof Error ? err.message : "Internal server error";
    return errorResponse(message, 500);
  }
});
