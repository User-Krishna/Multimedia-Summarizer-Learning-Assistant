import streamlit as st
from dotenv import load_dotenv
import os
import base64
import google.generativeai as genai
from youtube_transcript_api import YouTubeTranscriptApi
import re
from fpdf import FPDF

# Load environment variables
load_dotenv()

# Configure API key for Google Gemini
api_key = os.getenv("GOOGLE_API_KEY")
genai.configure(api_key=api_key)

# Use the latest Gemini model
MODEL_NAME = "gemini-1.5-flash"

# Function to extract transcript from YouTube video
def extract_transcript_details(youtube_video_url):
    try:
        video_id = re.search(r"(?:v=|/)([a-zA-Z0-9_-]{11})", youtube_video_url)
        if not video_id:
            raise ValueError("Invalid YouTube video URL")
        transcript_text = YouTubeTranscriptApi.get_transcript(video_id.group(1))
        return " ".join([i["text"] for i in transcript_text])
    except Exception as e:
        st.error(f"Error extracting transcript: {e}")
        return None

# Function to generate AI summary
def generate_gemini_content(transcript_text, prompt):
    try:
        model = genai.GenerativeModel(MODEL_NAME)
        response = model.generate_content(prompt + transcript_text)
        return response.text if response and hasattr(response, 'text') else None
    except Exception as e:
        st.error(f"Error generating summary: {e}")
        return None

# Custom CSS for styling
st.markdown("""
    <style>
        [data-testid="stAppViewContainer"] {
            background: linear-gradient(135deg, #1f4037, #99f2c8);
            color: white;
            font-family: 'Arial', sans-serif;
        }

        .container {
            width: 100%;
            margin: 20px auto;
            background-color: rgba(0, 0, 0, 0.8);
            padding: 0;
            border-radius: 15px;
            text-align: center;
            color: white;
            border: 2px solid black;
            box-shadow: 2px 2px 10px rgba(255, 255, 255, 0.2);
        }

        .styled-div {
            border: 2px solid black;
            padding: 20px;
            margin: 20px auto;
            border-radius: 10px;
            background-color: rgba(0, 0, 0, 0.6);
        }

        .stButton>button {
            background-color: #FFD700;
            color: black;
            font-size: 18px;
            font-weight: bold;
            padding: 10px 20px;
            border-radius: 8px;
            border: none;
            transition: 0.3s;
            display: flex;
            align-items: center;
            justify-content: center;
            margin: 10px auto;
        }

        .stButton>button:hover {
            background-color: #FFA500;
            color: white;
        }

        .stButton>button:before {
            content: '📝';
            margin-right: 10px;
        }

      .generated-notes {
    font-family: 'Georgia', serif;
    font-size: 1.1em;
    background: rgba(255, 255, 255, 0.2);
    padding: 15px;
    border-radius: 10px;
    margin-top: 20px;
    border: 1px solid white;
    width: calc(100vw - 40px); /* Full width minus padding */
    max-width: 100%; /* Prevents overflow */
    margin-left: auto;
    margin-right: auto;
    padding: 20px;
    box-sizing: border-box; /* Ensures padding doesn’t increase width */
}

}


        .stRadio label {
            font-size: 1.2em;
            color: white;
            margin-bottom: 10px;
        }

        .stRadio div[role='radiogroup'] {
            display: flex;
            flex-direction: column;
            align-items: flex-start;
            margin-bottom: 20px;
        }

        .stRadio label span {
            display: flex;
            align-items: center;
        }

        .stRadio label span:before {
            content: '🔘';
            margin-right: 10px;
        }

        .stTextInput div[data-baseweb="input"] {
            border-radius: 8px;
            padding: 10px;
            margin-bottom: 20px;
            width: 100%;
        }

        img {
            border-radius: 15px;
            box-shadow: 0 4px 8px rgba(0, 0, 0, 0.2);
            margin-bottom: 20px;
        }

        .download-button {
            background-color: #131313;
    color: #cfbaba;
    font-size: 18px;
    font-weight: bold;
    padding: 10px 20px;
    border-radius: 8px;
    border: none;
    transition: 0.3s;
    display: flex
;
    align-items: center;
    justify-content: center;
    margin: 10px auto;
        }

        .download-button:hover {
            background-color: #FFA500;
            color: white;
        }
    </style>
""", unsafe_allow_html=True)

# Navbar
st.markdown("""
    <div class="container">
        <h1 style="color: #FFD700;">YouTube Video Summarizer</h1>
    </div>
""", unsafe_allow_html=True)



# Streamlit Inputs and Actions
col1, col2 = st.columns([1, 5])

with col1:
    # Vertical Radio for Summary Length
    summary_length = st.radio("Summary Length", [0, 1, 2], format_func=lambda x: ["Short", "Mid", "Long"][x])

with col2:
    # Define prompt based on radio value
    if summary_length == 0:
        prompt = "Summarize the video in 10-15 bullet points in seperate lines, keeping it concise."
    elif summary_length == 1:
        prompt = "Provide a detailed summary within 400-500 words, covering the key points."
    else:
        prompt = "Give a comprehensive summary with deep insights within 900-1100 words."

    # Input Field for YouTube Link
    st.subheader("Enter YouTube Video Link Below:")
    youtube_link = st.text_input("", placeholder="Paste YouTube video URL here...")

    if youtube_link:
        video_id = re.search(r"(?:v=|/)([a-zA-Z0-9_-]{11})", youtube_link)
        if video_id:
            st.image(f"http://img.youtube.com/vi/{video_id.group(1)}/0.jpg", use_container_width=True)

    if st.button("Get Detailed Notes"):
        transcript_text = extract_transcript_details(youtube_link)
        if transcript_text:
            summary = generate_gemini_content(transcript_text, prompt)
            if summary:
                st.markdown("## 📝 Detailed Notes:", unsafe_allow_html=True)
                st.markdown(f"<div class='generated-notes'>{summary}</div>", unsafe_allow_html=True)

                pdf = FPDF()
                pdf.add_page()
                pdf.set_font("Arial", size=12)
                pdf.multi_cell(190, 10, summary)
                pdf_output = "summary.pdf"
                pdf.output(pdf_output)
                
                with open(pdf_output, "rb") as file:
                    pdf_bytes = file.read()
                    b64 = base64.b64encode(pdf_bytes).decode()
                    href = f'<a class="download-button" href="data:application/pdf;base64,{b64}" download="summary.pdf">Download PDF</a>'
                    st.markdown(href, unsafe_allow_html=True)

st.markdown("</div>", unsafe_allow_html=True)