import { verifyAuthToken } from "./auth";

export interface Env {
  FIREBASE_PROJECT_ID: string;
  GEMINI_API_KEY: string;
  FIREBASE_API_KEY: string;
}

export default {
  async fetch(request: Request, env: Env): Promise<Response> {
    const url = new URL(request.url);
    const path = url.pathname;
    const method = request.method;

    const corsHeaders = {
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "GET, POST, PUT, OPTIONS",
      "Access-Control-Allow-Headers": "Content-Type, Authorization",
    };

    if (method === "OPTIONS") {
      return new Response(null, { status: 204, headers: corsHeaders });
    }

    const projectId = env.FIREBASE_PROJECT_ID || "sattva-utsavam-dev";

    // Helper to fetch from Firestore
    async function fetchFirestoreCollection(collection: string) {
      const fsUrl = `https://firestore.googleapis.com/v1/projects/${projectId}/databases/(default)/documents/${collection}?pageSize=100`;
      const resp = await fetch(fsUrl, { headers: { "User-Agent": "Utsavam-Worker" } });
      if (!resp.ok) throw new Error(`HTTP ${resp.status}`);
      const data: any = await resp.json();
      return (data.documents || []).map((doc: any) => {
        const item: Record<string, any> = { id: doc.name.split("/").pop() };
        for (const [k, v] of Object.entries<any>(doc.fields || {})) {
          if ("stringValue" in v) item[k] = v.stringValue;
          else if ("integerValue" in v) item[k] = parseInt(v.integerValue, 10);
          else if ("doubleValue" in v) item[k] = parseFloat(v.doubleValue);
          else if ("booleanValue" in v) item[k] = v.booleanValue;
        }
        return item;
      });
    }

    // Health Check Endpoints
    if (path === "/" || path === "/health" || path === "/api/v1/health") {
      return new Response(
        JSON.stringify({
          status: "healthy",
          endpoints: [
            "/api/v1/catalog/gaushalas",
            "/api/v1/catalog/animals",
            "/api/v1/catalog/pujas",
            "/api/v1/ai/ask",
            "/api/v1/welfare",
            "/api/v1/donations",
            "/api/v1/profile",
          ],
        }),
        { status: 200, headers: corsHeaders }
      );
    }

    // Gaushalas
    if (path === "/api/v1/catalog/gaushalas" && method === "GET") {
      try {
        let results = await fetchFirestoreCollection("gaushalas");
        const city = url.searchParams.get("city");
        if (city) {
          results = results.filter((g: any) => g.location?.toLowerCase().includes(city.toLowerCase()));
        }
        return new Response(JSON.stringify({ gaushalas: results, count: results.length }), { status: 200, headers: corsHeaders });
      } catch (err: any) {
        return new Response(JSON.stringify({ error: err.message }), { status: 500, headers: corsHeaders });
      }
    }

    // Animals
    if (path === "/api/v1/catalog/animals" && method === "GET") {
      try {
        let results = await fetchFirestoreCollection("animals");
        const gaushalaId = url.searchParams.get("gaushalaId");
        if (gaushalaId) {
          results = results.filter((a: any) => a.gaushalaId === gaushalaId);
        }
        return new Response(JSON.stringify({ animals: results, count: results.length }), { status: 200, headers: corsHeaders });
      } catch (err: any) {
        return new Response(JSON.stringify({ error: err.message }), { status: 500, headers: corsHeaders });
      }
    }

    // Pujas
    if (path === "/api/v1/catalog/pujas" && method === "GET") {
      try {
        let results = await fetchFirestoreCollection("pujas");
        const category = url.searchParams.get("category");
        const search = url.searchParams.get("search");
        if (category && category !== "All") {
          results = results.filter((p: any) => p.category?.toLowerCase() === category.toLowerCase());
        }
        if (search) {
          const s = search.toLowerCase();
          results = results.filter((p: any) =>
            p.title?.toLowerCase().includes(s) ||
            p.templeName?.toLowerCase().includes(s) ||
            p.location?.toLowerCase().includes(s)
          );
        }
        return new Response(JSON.stringify({ pujas: results, count: results.length }), { status: 200, headers: corsHeaders });
      } catch (err: any) {
        return new Response(JSON.stringify({ error: err.message }), { status: 500, headers: corsHeaders });
      }
    }

    // Welfare Stats
    if (path === "/api/v1/welfare" && method === "GET") {
      try {
        const gaushalas = await fetchFirestoreCollection("gaushalas");
        let totalRescued = 0;
        gaushalas.forEach((g: any) => totalRescued += g.animalsRescuedCount || 0);
        return new Response(JSON.stringify({
          totalRescued,
          activeSanctuaries: gaushalas.length,
          totalMealsServed: totalRescued * 30
        }), { status: 200, headers: corsHeaders });
      } catch (err: any) {
        return new Response(JSON.stringify({ error: err.message }), { status: 500, headers: corsHeaders });
      }
    }

    // Welfare Updates
    if (path === "/api/v1/catalog/welfare_updates" && method === "GET") {
      try {
        let results = await fetchFirestoreCollection("welfare_updates");
        const gaushalaId = url.searchParams.get("gaushalaId");
        const animalId = url.searchParams.get("animalId");
        if (gaushalaId) {
          results = results.filter((w: any) => w.gaushalaId === gaushalaId);
        }
        if (animalId) {
          results = results.filter((w: any) => w.animalId === animalId);
        }
        return new Response(JSON.stringify({ updates: results, count: results.length }), { status: 200, headers: corsHeaders });
      } catch (err: any) {
        return new Response(JSON.stringify({ error: err.message }), { status: 500, headers: corsHeaders });
      }
    }

    // --- Authenticated Endpoints Below ---
    const isAuthRoute = path === "/api/v1/profile" || path === "/api/v1/donations";
    let uid: string | null = null;
    
    if (isAuthRoute) {
      uid = await verifyAuthToken(request, env.FIREBASE_API_KEY);
      if (!uid) {
        return new Response(JSON.stringify({ error: "Unauthorized" }), { status: 401, headers: corsHeaders });
      }
    }

    // Profile GET
    if (path === "/api/v1/profile" && method === "GET") {
      try {
        const fsUrl = `https://firestore.googleapis.com/v1/projects/${projectId}/databases/(default)/documents/users/${uid}`;
        const resp = await fetch(fsUrl, { headers: { "User-Agent": "Utsavam-Worker" } });
        if (!resp.ok) {
          if (resp.status === 404) return new Response(JSON.stringify({ profile: null }), { status: 200, headers: corsHeaders });
          throw new Error(`HTTP ${resp.status}`);
        }
        const data: any = await resp.json();
        const profile: Record<string, any> = { id: uid };
        for (const [k, v] of Object.entries<any>(data.fields || {})) {
          if ("stringValue" in v) profile[k] = v.stringValue;
          else if ("integerValue" in v) profile[k] = parseInt(v.integerValue, 10);
        }
        return new Response(JSON.stringify({ profile }), { status: 200, headers: corsHeaders });
      } catch (err: any) {
        return new Response(JSON.stringify({ error: err.message }), { status: 500, headers: corsHeaders });
      }
    }

    // Profile PUT (Update)
    if (path === "/api/v1/profile" && method === "PUT") {
      try {
        const body: any = await request.json();
        const fields: any = {};
        for (const [k, v] of Object.entries(body)) {
          if (typeof v === "string") fields[k] = { stringValue: v };
          else if (typeof v === "number") fields[k] = { integerValue: v.toString() };
        }
        
        const updateMaskPaths = Object.keys(fields).map(k => `updateMask.fieldPaths=${k}`).join("&");
        const fsUrl = `https://firestore.googleapis.com/v1/projects/${projectId}/databases/(default)/documents/users/${uid}?${updateMaskPaths}`;
        
        const resp = await fetch(fsUrl, {
          method: "PATCH",
          headers: { "Content-Type": "application/json", "User-Agent": "Utsavam-Worker" },
          body: JSON.stringify({ name: `projects/${projectId}/databases/(default)/documents/users/${uid}`, fields })
        });
        
        if (!resp.ok) throw new Error(`HTTP ${resp.status} ${await resp.text()}`);
        return new Response(JSON.stringify({ success: true }), { status: 200, headers: corsHeaders });
      } catch (err: any) {
        return new Response(JSON.stringify({ error: err.message }), { status: 500, headers: corsHeaders });
      }
    }

    // Donations GET
    if (path === "/api/v1/donations" && method === "GET") {
      try {
        const fsUrl = `https://firestore.googleapis.com/v1/projects/${projectId}/databases/(default)/documents/users/${uid}/seva_contributions`;
        const resp = await fetch(fsUrl, { headers: { "User-Agent": "Utsavam-Worker" } });
        if (!resp.ok) {
           if (resp.status === 404) return new Response(JSON.stringify({ donations: [] }), { status: 200, headers: corsHeaders });
           throw new Error(`HTTP ${resp.status}`);
        }
        const data: any = await resp.json();
        const results = (data.documents || []).map((doc: any) => {
          const item: Record<string, any> = { id: doc.name.split("/").pop() };
          for (const [k, v] of Object.entries<any>(doc.fields || {})) {
            if ("stringValue" in v) item[k] = v.stringValue;
            else if ("integerValue" in v) item[k] = parseInt(v.integerValue, 10);
          }
          return item;
        });
        return new Response(JSON.stringify({ donations: results }), { status: 200, headers: corsHeaders });
      } catch (err: any) {
        return new Response(JSON.stringify({ error: err.message }), { status: 500, headers: corsHeaders });
      }
    }

    // Donations POST
    if (path === "/api/v1/donations" && method === "POST") {
      try {
        const body: any = await request.json();
        const donationId = crypto.randomUUID().replace(/-/g, "");
        const fsUrl = `https://firestore.googleapis.com/v1/projects/${projectId}/databases/(default)/documents/users/${uid}/seva_contributions?documentId=${donationId}`;
        
        const fields: any = {
          targetType: { stringValue: body.targetType || "GAUSHALA" },
          targetId: { stringValue: body.targetId || "" },
          targetName: { stringValue: body.targetName || "" },
          amountRupees: { integerValue: (body.amountRupees || 0).toString() },
          paymentStatus: { stringValue: "PENDING" },
          dateStr: { stringValue: new Date().toISOString().split("T")[0] },
          sevaCategory: { stringValue: body.sevaCategory || "" }
        };

        const resp = await fetch(fsUrl, {
          method: "POST",
          headers: { "Content-Type": "application/json", "User-Agent": "Utsavam-Worker" },
          body: JSON.stringify({ fields })
        });
        
        if (!resp.ok) throw new Error(`HTTP ${resp.status} ${await resp.text()}`);
        return new Response(JSON.stringify({ id: donationId, status: "PENDING" }), { status: 201, headers: corsHeaders });
      } catch (err: any) {
        return new Response(JSON.stringify({ error: err.message }), { status: 500, headers: corsHeaders });
      }
    }

    // Gemini AI Ask Endpoint
    if (path === "/api/v1/ai/ask" && method === "POST") {
      const apiKey = env.GEMINI_API_KEY;
      if (!apiKey) return new Response(JSON.stringify({ detail: "Gemini API Key missing on server" }), { status: 500, headers: corsHeaders });

      try {
        const body: any = await request.json();
        const query = body.query;
        if (!query) return new Response(JSON.stringify({ detail: "Field 'query' is required" }), { status: 400, headers: corsHeaders });

        const aiUrl = `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=${apiKey}`;
        const aiResp = await fetch(aiUrl, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({ contents: [{ parts: [{ text: query }] }] }),
        });

        if (!aiResp.ok) throw new Error(await aiResp.text());
        const aiData: any = await aiResp.json();
        const text = aiData.candidates?.[0]?.content?.parts?.[0]?.text || "No response generated.";

        return new Response(JSON.stringify({ response: text }), { status: 200, headers: corsHeaders });
      } catch (err: any) {
        return new Response(JSON.stringify({ detail: err.message || "Internal server error" }), { status: 500, headers: corsHeaders });
      }
    }

    return new Response(JSON.stringify({ detail: "Not Found" }), { status: 404, headers: corsHeaders });
  },
};
