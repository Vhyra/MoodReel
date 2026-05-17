from sentence_transformers import SentenceTransformer
import pandas as pd
import numpy as np
from pathlib import Path

df = pd.read_csv("movies_clean.csv")

model = SentenceTransformer("all-MiniLM-L6-v2")


print("Building embedding...")
embeddings = model.encode(df["lemmatized"].tolist(), show_progress_bar=True)

current_dir = Path(__file__).parent.resolve()
model_dir = current_dir.parent.parent / "movieml" / "model"
model_dir.mkdir(parents=True, exist_ok=True)

model.save(model_dir / "sentence_transformer")
np.save(model_dir / "embeddings.npy", embeddings)
df.to_csv(model_dir / "movies_indexed.csv", index=False)

print("Ready!")