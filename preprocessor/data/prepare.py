import pandas as pd
import json
import numpy as np
from nltk.stem import WordNetLemmatizer
from nltk.corpus import stopwords
import nltk
import re
from nrclex import NRCLex


# --------------------------------------- Utilities --------------------------------------- #

def clean_and_lemm(text: str) -> str:
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

def extract_names(json_str):
    try:
        items = json.loads(json_str) if isinstance(json_str, str) else json_str
        return " ".join([item["name"] for item in items])
    except:
        return ""


# --------------------------------------- Data preparation --------------------------------------- #

df = pd.read_parquet("hf://datasets/AiresPucrs/tmdb-5000-movies/data/train-00000-of-00001-6db04ab1c75d6817.parquet")

nltk.download("stopwords", quiet=True)
STOPWORDS = set(stopwords.words("english"))
lemmatizer = WordNetLemmatizer()
EMOTIONS = ["fear", "anger", "anticipation", "surprise", "sadness", "joy", "disgust", "positive", "negative"]

language_map = {
    "en": "english",
    "de": "german",
    "es": "spanish",
    "zh": "chinese",
    "ja": "japanese",
    "fr": "french",
    "da": "danish",
    "it": "italian",
    "sv": "swedish",
    "hi": "hindi",
    "ru": "russian",
    "pt": "portuguese",
    "ko": "korean",
    "af": "afrikaans",
    "ro": "romanian",
    "nl": "dutch",
    "ar": "arabic",
    "he": "hebrew",
    "th": "thai",
    "cn": "chinese",
    "tr": "turkish",
    "cs": "czech",
    "fa": "persian",
    "no": "norwegian",
    "ps": "pashto",
    "vi": "vietnamese",
    "el": "greek",
    "hu": "hungarian",
    "nb": "norwegian",
    "xx": "",           
    "id": "indonesian",
    "pl": "polish",
    "is": "icelandic",
    "te": "telugu",
    "ta": "tamil",
    "ky": "kyrgyz",
    "sl": "slovenian",
}

# --------------------------------------- Data processing --------------------------------------- #

df["language_clean"] = df["original_language"].map(language_map).fillna("")


df["genres_clean"] = df["genres"].apply(extract_names)
df["keywords_clean"] = df["keywords"].apply(extract_names)

#df["overview"] = df["overview"].fillna("N/D")
df["tagline"] = df["tagline"].fillna("N/D")

df["combined"] = (
    df["keywords_clean"].fillna("") + " " +
    df["tagline"].fillna("") + " " +
    df["genres_clean"] + " " +
    df["production_countries"].fillna("") + " " +
    df["language_clean"].fillna("")
)

df["sentiment"] = (
    df["overview"].fillna("") + " " +
    df["genres_clean"] + " " +
    df["keywords_clean"]
)

df["lemmatized"] = df["combined"].apply(clean_and_lemm)
df["sentiment_lemm"] = df["sentiment"].apply(clean_and_lemm)
df[EMOTIONS] = df["sentiment_lemm"].apply(get_emotions)

print(df[EMOTIONS].head(3))

df[["id", 
    "title", 
    "lemmatized", 
    "sentiment_lemm",
    "fear", 
    "anger", 
    "anticipation", 
    "surprise", 
    "sadness", 
    "joy", 
    "disgust", 
    "positive", 
    "negative"]
    ].to_csv("movies_clean.csv", index=False)

print(f"Dataset ready: {len(df)} film")
print(df[["title", "lemmatized"]].head(3))