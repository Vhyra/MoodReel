from sentence_transformers import SentenceTransformer
import pandas as pd
import numpy as np
from sklearn.metrics.pairwise import cosine_similarity
import nltk
from nltk.corpus import stopwords
from nltk.stem import WordNetLemmatizer
import re
import os
from pathlib import Path
from nrclex import NRCLex
from keybert import KeyBERT

current_dir = Path(__file__).parent.resolve()
MODEL_PATH = str(current_dir / "sentence_transformer")

if os.path.exists(MODEL_PATH):
    model = SentenceTransformer(MODEL_PATH, device="cpu")
    kw_model = KeyBERT(model)
else:
    model = SentenceTransformer("all-MiniLM-L6-v2", device="cpu")
    kw_model = KeyBERT(model)

#nltk.download("stopwords", quiet=True)
STOPWORDS = set(stopwords.words("english"))

embeddings = np.load("model/embeddings.npy")
df = pd.read_csv("model/movies_indexed.csv")
lemmatizer = WordNetLemmatizer()

EMOTIONS = ["fear", "anger", "anticipation", "surprise", "sadness", "joy", "disgust"]

def recommend(query, top_n=10):

    query = getKeyWords(query)

    cleaned_query = clean_and_stem(query)
    query_vector = model.encode([cleaned_query])

    similarities = cosine_similarity(query_vector, embeddings)[0]

    emo = get_emotions(cleaned_query)

    combined = np.zeros(len(embeddings))

    for i in range(len(embeddings)):
        film_vec = np.array([df.iloc[i][e] for e in EMOTIONS])
        emotion_score = 1 - np.mean(np.abs(emo - film_vec))

        combined[i] = (similarities[i] * 0.9) + (emotion_score * 0.1)

    
    top_indices = np.argsort(combined)[::-1][:top_n]

    query_words = set(cleaned_query.split())
    total_words = len(query_words)
    
    results = []
    for i in top_indices:
        title = df.iloc[i]["title"]
        comb_words = set(df.iloc[i]["lemmatized"].split())
        similarity_pct = round(similarities[i] * 100, 2)

        print(f"{df.iloc[i]['title']} - semantic: {round(similarities[i]*100,2)}% - combined: {round(combined[i]*100,2)}%")
        
        common_words = query_words & comb_words
        matching_words = len(common_words)
        matching_pct = round((matching_words / total_words * 100) if total_words > 0 else 0, 2)

        print(f"{title}")
        print(f"  Similarity:      {similarity_pct}%")
        print(f"  Common words:   {matching_words}/{total_words} ({matching_pct}%)")
        print(f"  Matching words:  {common_words}\n")

        results.append({
            "title": df.iloc[i]["title"]
        })        
    
    return results

def clean_and_stem(text: str) -> str:
    text = text.lower()
    text = re.sub(r"[^\w\s]", "", text)
    tokens = [t for t in text.split() if t not in STOPWORDS]
    tokens = [lemmatizer.lemmatize(t) for t in tokens]
    return " ".join(tokens)

def get_emotions(text):
    obj = NRCLex(text)
    obj.load_raw_text(text)
    emo = obj.affect_frequencies
    return pd.Series({e: emo.get(e, 0.0) for e in EMOTIONS})

def getKeyWords(text: str) -> str:
    text = text.lower()
    keywords = kw_model.extract_keywords(
        text,
        keyphrase_ngram_range=(1, 2),
        stop_words='english',
        top_n=8
    )
    return " ".join(k for k, score in keywords if score > 0.5 )