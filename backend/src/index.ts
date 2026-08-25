export interface Env {
  FIREBASE_PROJECT_ID: string;
  GEMINI_API_KEY: string;
}

export default {
  async fetch(request: Request, env: Env): Promise<Response> {
    const url = new URL(request.url);
    const path = url.pathname;
    const method = request.method;

    const corsHeaders = {
      "Content-Type": "application/json",
      "Access-Control-Allow-Origin": "*",
      "Access-Control-Allow-Methods": "GET, POST, OPTIONS",
      "Access-Control-Allow-Headers": "Content-Type, Authorization",
    };

    if (method === "OPTIONS") {
      return new Response(null, { status: 204, headers: corsHeaders });
    }

    // Health Check Endpoints
    if (path === "/" || path === "/health" || path === "/api/v1/health") {
      return new Response(
        JSON.stringify({
          status: "healthy",
          service: "utsavam-backend",
          runtime: "cloudflare-workers",
          endpoints: [
            "/health",
            "/api/v1/health",
            "/api/v1/catalog/gaushalas",
            "/api/v1/catalog/pujas",
            "/api/v1/ai/ask",
          ],
        }),
        { status: 200, headers: corsHeaders }
      );
    }

    // Firestore Catalog: Gaushalas
    if (path === "/api/v1/catalog/gaushalas" && method === "GET") {
      const projectId = env.FIREBASE_PROJECT_ID || "sattva-utsavam-dev";
      const fsUrl = `https://firestore.googleapis.com/v1/projects/${projectId}/databases/(default)/documents/gaushalas`;
      try {
        const resp = await fetch(fsUrl, {
          headers: { "User-Agent": "Utsavam-Worker" },
        });
        if (!resp.ok) {
          return new Response(
            JSON.stringify({
              gaushalas: [],
              count: 0,
              source: `firestore:${projectId}`,
              http_status: resp.status,
              message: `Firestore query returned HTTP ${resp.status}`,
            }),
            { status: 200, headers: corsHeaders }
          );
        }
        const data: any = await resp.json();
        const documents = data.documents || [];
        const results = documents.map((doc: any) => {
          const fields = doc.fields || {};
          const item: Record<string, any> = {};
          for (const [k, v] of Object.entries<any>(fields)) {
            if ("stringValue" in v) item[k] = v.stringValue;
            else if ("integerValue" in v) item[k] = parseInt(v.integerValue, 10);
            else if ("doubleValue" in v) item[k] = parseFloat(v.doubleValue);
            else if ("booleanValue" in v) item[k] = v.booleanValue;
          }
          item.id = doc.name.split("/").pop();
          return item;
        });
        return new Response(
          JSON.stringify({ gaushalas: results, count: results.length, source: "firestore" }),
          { status: 200, headers: corsHeaders }
        );
      } catch (err: any) {
        return new Response(
          JSON.stringify({ gaushalas: [], count: 0, source: `firestore:${projectId}`, error: err.message }),
          { status: 200, headers: corsHeaders }
        );
      }
    }

    // Firestore Catalog: Pujas
    if (path === "/api/v1/catalog/pujas" && method === "GET") {
      const projectId = env.FIREBASE_PROJECT_ID || "sattva-utsavam-dev";
      const fsUrl = `https://firestore.googleapis.com/v1/projects/${projectId}/databases/(default)/documents/pujas`;
      try {
        const resp = await fetch(fsUrl, {
          headers: { "User-Agent": "Utsavam-Worker" },
        });
        if (!resp.ok) {
          return new Response(
            JSON.stringify({
              pujas: [],
              count: 0,
              source: `firestore:${projectId}`,
              http_status: resp.status,
              message: `Firestore query returned HTTP ${resp.status}`,
            }),
            { status: 200, headers: corsHeaders }
          );
        }
        const data: any = await resp.json();
        const documents = data.documents || [];
        const results = documents.map((doc: any) => {
          const fields = doc.fields || {};
          const item: Record<string, any> = {};
          for (const [k, v] of Object.entries<any>(fields)) {
            if ("stringValue" in v) item[k] = v.stringValue;
            else if ("integerValue" in v) item[k] = parseInt(v.integerValue, 10);
            else if ("doubleValue" in v) item[k] = parseFloat(v.doubleValue);
            else if ("booleanValue" in v) item[k] = v.booleanValue;
          }
          item.id = doc.name.split("/").pop();
          return item;
        });
        return new Response(
          JSON.stringify({ pujas: results, count: results.length, source: "firestore" }),
          { status: 200, headers: corsHeaders }
        );
      } catch (err: any) {
        return new Response(
          JSON.stringify({ pujas: [], count: 0, source: `firestore:${projectId}`, error: err.message }),
          { status: 200, headers: corsHeaders }
        );
      }
    }

    // Gemini AI Ask Endpoint
    if (path === "/api/v1/ai/ask" && method === "POST") {
      const apiKey = env.GEMINI_API_KEY;
      if (!apiKey) {
        return new Response(
          JSON.stringify({ detail: "Gemini API Key missing on server" }),
          { status: 500, headers: corsHeaders }
        );
      }

      try {
        const body: any = await request.json();
        const query = body.query;
        if (!query) {
          return new Response(
            JSON.stringify({ detail: "Field 'query' is required" }),
            { status: 400, headers: corsHeaders }
          );
        }

        const aiUrl = `https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key=${apiKey}`;
        const aiResp = await fetch(aiUrl, {
          method: "POST",
          headers: { "Content-Type": "application/json" },
          body: JSON.stringify({
            contents: [{ parts: [{ text: query }] }],
          }),
        });

        if (!aiResp.ok) {
          const errText = await aiResp.text();
          return new Response(
            JSON.stringify({ detail: `Gemini API error: ${errText}` }),
            { status: 500, headers: corsHeaders }
          );
        }

        const aiData: any = await aiResp.json();
        const text =
          aiData.candidates?.[0]?.content?.parts?.[0]?.text || "No response generated.";

        return new Response(JSON.stringify({ response: text }), {
          status: 200,
          headers: corsHeaders,
        });
      } catch (err: any) {
        return new Response(
          JSON.stringify({ detail: err.message || "Internal server error" }),
          { status: 500, headers: corsHeaders }
        );
      }
    }

    return new Response(JSON.stringify({ detail: "Not Found" }), {
      status: 404,
      headers: corsHeaders,
    });
  },
};
