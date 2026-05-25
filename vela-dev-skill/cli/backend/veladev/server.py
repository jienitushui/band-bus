# src/veladev/server.py
from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from .retriever import VelaRetriever
import os

app = FastAPI()

# 全局检索器实例
retriever = None

def get_retriever():
    global retriever
    if retriever is None:
        db_path = os.path.join(os.getcwd(), "doc_vector_db")
        if not os.path.exists(db_path):
            raise RuntimeError("Database not found. Please run 'python -m veladev.build_index' first.")
        retriever = VelaRetriever(db_path=db_path)
    return retriever

class QueryRequest(BaseModel):
    question: str
    k: int = 3

@app.on_event("startup")
def load_model():
    get_retriever()

@app.post("/search")
def search_docs(request: QueryRequest):
    try:
        r = get_retriever()
        results = r.search(request.question, k=request.k)
        return {"results": results}
    except Exception as e:
        raise HTTPException(status_code=500, detail=str(e))

if __name__ == "__main__":
    import uvicorn
    uvicorn.run(app, host="127.0.0.1", port=8000)
