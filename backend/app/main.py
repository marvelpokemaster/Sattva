from fastapi import FastAPI, HTTPException
from fastapi.middleware.cors import CORSMiddleware
from pydantic import BaseModel
from google import genai
import os
from dotenv import load_dotenv

load_dotenv()

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

@app.get("/api/v1/health")
def health_check():
    return {"status": "healthy"}

@app.post("/api/v1/ai/ask", response_model=AskResponse)
def ask_ai(req: AskRequest):
    api_key = os.environ.get("GEMINI_API_KEY")
    if not api_key:
        raise HTTPException(status_code=500, detail="Gemini API Key missing on server")
    try:
        client = genai.Client(api_key=api_key)
        response = client.models.generate_content(
            model='gemini-2.5-flash',
            contents=req.query
        )
        return AskResponse(response=response.text)
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

