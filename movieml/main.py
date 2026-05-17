from fastapi import FastAPI
from pydantic import BaseModel
from model.search import recommend

app = FastAPI()

class QueryRequest(BaseModel):
    query: str
    top_n: int = 10

@app.post("/recommend")
def get_recommendations(request: QueryRequest):
    results = recommend(request.query, request.top_n)
    return {"results": results}