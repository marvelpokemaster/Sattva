from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
import os
import json
import urllib.request
import urllib.error

app = FastAPI(title="Utsavam Backend API", version="1.0.0")

app.add_middleware(
    CORSMiddleware,
    allow_origins=["*"],
    allow_credentials=True,
    allow_methods=["*"],
    allow_headers=["*"],
)

class AskRequest(BaseModel):
    query: str
    userId: str = None

class AskResponse(BaseModel):
    response: str

@app.get("/")
def read_root():
    return {
        "message": "Utsavam API running on Cloudflare Workers",
        "status": "healthy",
        "endpoints": [
            "/health",
            "/api/v1/health",
            "/api/v1/catalog/gaushalas",
            "/api/v1/catalog/pujas",
            "/api/v1/ai/ask"
        ]
    }

@app.get("/health")
@app.get("/api/v1/health")
def health_check():
    return {
        "status": "healthy",
        "service": "utsavam-backend",
        "runtime": "cloudflare-workers-python"
    }

@app.get("/api/v1/catalog/gaushalas")
def get_gaushalas():
    project_id = os.environ.get("FIREBASE_PROJECT_ID", "sattva-utsavam-dev")
    url = f"https://firestore.googleapis.com/v1/projects/{project_id}/databases/(default)/documents/gaushalas"
    try:
        req = urllib.request.Request(url, headers={"User-Agent": "Utsavam-Worker"})
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
            return {"gaushalas": results, "count": len(results), "source": "firestore"}
    except urllib.error.HTTPError as e:
        # If Firestore collection is empty or project doesn't exist yet, return structured fallback
        return {
            "gaushalas": [],
            "count": 0,
            "source": f"firestore:{project_id}",
            "http_status": e.code,
            "message": f"Firestore query returned HTTP {e.code}"
        }
    except Exception as e:
        return {
            "gaushalas": [],
            "count": 0,
            "source": f"firestore:{project_id}",
            "error": str(e)
        }

@app.get("/api/v1/catalog/pujas")
def get_pujas():
    project_id = os.environ.get("FIREBASE_PROJECT_ID", "sattva-utsavam-dev")
    url = f"https://firestore.googleapis.com/v1/projects/{project_id}/databases/(default)/documents/pujas"
    try:
        req = urllib.request.Request(url, headers={"User-Agent": "Utsavam-Worker"})
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
            return {"pujas": results, "count": len(results), "source": "firestore"}
    except Exception as e:
        return {"pujas": [], "count": 0, "source": f"firestore:{project_id}", "info": str(e)}

@app.post("/api/v1/ai/ask", response_model=AskResponse)
def ask_ai(req: AskRequest):
    api_key = os.environ.get("GEMINI_API_KEY")
    if not api_key:
        raise HTTPException(status_code=500, detail="Gemini API Key missing on server")
    
    url = f"https://generativelanguage.googleapis.com/v1beta/models/gemini-2.5-flash:generateContent?key={api_key}"
    payload = {
        "contents": [
            {
                "parts": [{"text": req.query}]
            }
        ]
    }
    
    try:
        req_data = json.dumps(payload).encode("utf-8")
        request = urllib.request.Request(
            url,
            data=req_data,
            headers={"Content-Type": "application/json"}
        )
        with urllib.request.urlopen(request, timeout=15) as response:
            res_data = json.loads(response.read().decode("utf-8"))
            candidates = res_data.get("candidates", [])
            if candidates:
                text = candidates[0].get("content", {}).get("parts", [{}])[0].get("text", "")
                return AskResponse(response=text)
            else:
                return AskResponse(response="No response generated.")
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

import asgi
from workers import WorkerEntrypoint

class Default(WorkerEntrypoint):
    async def fetch(self, request):
        return await asgi.fetch(app, request, self.env)
