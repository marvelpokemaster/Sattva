from workers import WorkerEntrypoint, Response
from urllib.parse import urlparse
import json
import urllib.request
import urllib.error

class Default(WorkerEntrypoint):
    async def fetch(self, request):
        parsed = urlparse(request.url)
        path = parsed.path
        method = request.method

        headers = {
            "Content-Type": "application/json",
            "Access-Control-Allow-Origin": "*",
            "Access-Control-Allow-Methods": "GET, POST, OPTIONS",
            "Access-Control-Allow-Headers": "Content-Type, Authorization",
        }

        # Handle CORS preflight
        if method == "OPTIONS":
            return Response("", status=204, headers=headers)

        # 1. Health check endpoints
        if path in ["/", "/health", "/api/v1/health"]:
            body = {
                "status": "healthy",
                "service": "utsavam-backend",
                "runtime": "cloudflare-workers-python",
                "endpoints": [
                    "/health",
                    "/api/v1/health",
                    "/api/v1/catalog/gaushalas",
                    "/api/v1/catalog/pujas",
                    "/api/v1/ai/ask"
                ]
            }
            return Response(json.dumps(body), status=200, headers=headers)

        # 2. Firestore Catalog - Gaushalas
        if path == "/api/v1/catalog/gaushalas" and method == "GET":
            project_id = str(self.env.FIREBASE_PROJECT_ID) if hasattr(self.env, "FIREBASE_PROJECT_ID") else "sattva-utsavam-dev"
            fs_url = f"https://firestore.googleapis.com/v1/projects/{project_id}/databases/(default)/documents/gaushalas"
            try:
                req = urllib.request.Request(fs_url, headers={"User-Agent": "Utsavam-Worker"})
                with urllib.request.urlopen(req, timeout=10) as resp:
                    data = json.loads(resp.read().decode())
                    documents = data.get("documents", [])
                    results = []
                    for doc in documents:
                        fields = doc.get("fields", {})
                        item = {}
                        for k, v in fields.items():
                            if "stringValue" in v:
                                item[k] = v["stringValue"]
                            elif "integerValue" in v:
                                item[k] = int(v["integerValue"])
                            elif "doubleValue" in v:
                                item[k] = float(v["doubleValue"])
                            elif "booleanValue" in v:
                                item[k] = v["booleanValue"]
                        item["id"] = doc.get("name", "").split("/")[-1]
                        results.append(item)
                    return Response(json.dumps({"gaushalas": results, "count": len(results), "source": "firestore"}), status=200, headers=headers)
            except urllib.error.HTTPError as e:
                return Response(json.dumps({
                    "gaushalas": [],
                    "count": 0,
                    "source": f"firestore:{project_id}",
                    "http_status": e.code,
                    "message": f"Firestore query returned HTTP {e.code}"
                }), status=200, headers=headers)
            except Exception as e:
                return Response(json.dumps({
                    "gaushalas": [],
                    "count": 0,
                    "source": f"firestore:{project_id}",
                    "error": str(e)
                }), status=200, headers=headers)

        # 3. Firestore Catalog - Pujas
        if path == "/api/v1/catalog/pujas" and method == "GET":
            project_id = str(self.env.FIREBASE_PROJECT_ID) if hasattr(self.env, "FIREBASE_PROJECT_ID") else "sattva-utsavam-dev"
            fs_url = f"https://firestore.googleapis.com/v1/projects/{project_id}/databases/(default)/documents/pujas"
            try:
                req = urllib.request.Request(fs_url, headers={"User-Agent": "Utsavam-Worker"})
                with urllib.request.urlopen(req, timeout=10) as resp:
                    data = json.loads(resp.read().decode())
                    documents = data.get("documents", [])
                    results = []
                    for doc in documents:
                        fields = doc.get("fields", {})
                        item = {}
                        for k, v in fields.items():
                            if "stringValue" in v:
                                item[k] = v["stringValue"]
                            elif "integerValue" in v:
                                item[k] = int(v["integerValue"])
                            elif "doubleValue" in v:
                                item[k] = float(v["doubleValue"])
                            elif "booleanValue" in v:
                                item[k] = v["booleanValue"]
                        item["id"] = doc.get("name", "").split("/")[-1]
                        results.append(item)
                    return Response(json.dumps({"pujas": results, "count": len(results), "source": "firestore"}), status=200, headers=headers)
            except Exception as e:
                return Response(json.dumps({
                    "pujas": [],
                    "count": 0,
                    "source": f"firestore:{project_id}",
                    "error": str(e)
                }), status=200, headers=headers)

        # 4. Gemini AI Ask endpoint
        if path == "/api/v1/ai/ask" and method == "POST":
            api_key = str(self.env.GEMINI_API_KEY) if hasattr(self.env, "GEMINI_API_KEY") else ""
            if not api_key:
                return Response(json.dumps({"detail": "Gemini API Key missing on server"}), status=500, headers=headers)

            try:
                body_text = await request.text()
                body_json = json.loads(body_text) if body_text else {}
                query = body_json.get("query", "")
                if not query:
                    return Response(json.dumps({"detail": "Field 'query' is required"}), status=400, headers=headers)

                url = f"https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key={api_key}"
                payload = {
                    "contents": [
                        {
                            "parts": [{"text": query}]
                        }
                    ]
                }
                req_data = json.dumps(payload).encode("utf-8")
                req = urllib.request.Request(
                    url,
                    data=req_data,
                    headers={"Content-Type": "application/json"}
                )
                with urllib.request.urlopen(req, timeout=15) as ai_resp:
                    res_data = json.loads(ai_resp.read().decode("utf-8"))
                    candidates = res_data.get("candidates", [])
                    if candidates:
                        text = candidates[0].get("content", {}).get("parts", [{}])[0].get("text", "")
                        return Response(json.dumps({"response": text}), status=200, headers=headers)
                    else:
                        return Response(json.dumps({"response": "No response generated."}), status=200, headers=headers)
            except Exception as e:
                return Response(json.dumps({"detail": str(e)}), status=500, headers=headers)

        # 404 Not Found
        return Response(json.dumps({"detail": "Not Found"}), status=404, headers=headers)
