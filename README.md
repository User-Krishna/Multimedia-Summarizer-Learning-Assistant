# 🎓 Intelligent Multimedia Summarizer and Learning Assistant

## 📌 Overview
The **Intelligent Multimedia Summarizer and Learning Assistant** is a comprehensive web-based application designed to **automate the summarization of various content formats**, including **documents, audio, and video**, while also offering **interactive chatbot support** and **note-taking capabilities**. It leverages **NLP and AI models** to deliver concise, meaningful summaries, fostering efficient learning and easy information management.

---
## 📈 Architecture Diagram
Below is the system architecture diagram :

![Architecture Diagram](https://example.com/architecture_diagram.png)

**Explanation:**  
The architecture comprises a modular, layered structure:  
1️⃣ **Frontend (HTML/CSS/JS)** – User interfaces for upload, summary, chatbot, note-taking, and translation.  
2️⃣ **Backend (Java/Python)** – Business logic, NLP models, and integration of summarization APIs.  
3️⃣ **Database (MySQL)** – Stores transcripts, summaries, notes, and user data.  
4️⃣ **Speech & Video Modules** – Whisper Model, youtube_transcript_api, and Web Speech API for real-time transcription.  
5️⃣ **Translation & NLP** – GPT/Gemini APIs, Google Translate API for summary generation and translation.  
6️⃣ **Communication** – Email notifications via Jakarta Mail.  
7️⃣ **Security** – HTTPS, input validation, and authentication for safe data handling.

---
## ✨ Features
✅ **Document Summarization** – Extracts key points from lengthy documents for quick insights.  
✅ **Speech Summarization** – Converts spoken input into grammatically correct text and generates summaries.  
✅ **YouTube Video Summarizer** – Generates brief yet comprehensive summaries from video links.  
✅ **Interactive Chatbot (InfoBuddy)** – Offers real-time support and query resolution.  
✅ **Note-taking Tool** – Allows users to capture and manage important details during summarization.  
✅ **Language Translation** – Translates generated summaries and notes into multiple languages for global accessibility.  
✅ **Dynamic Summary Length Control** – Users can choose between short, medium, or long summaries.  

---

## 🛠️ Technology Stack
| Component              | Technology Used                        |
|-------------------------|--------------------------------------|
| **Backend**            | Java Servlets, JSP, Python            |
| **Frontend**           | HTML5, CSS3, JavaScript               |
| **Database**           | MySQL                                 |
| **NLP/AI Models**      | OpenAI GPT / Gemini API               |
| **Speech-to-Text**     | Web Speech API / Whisper Model        |
| **PDF/Text Extraction**| Apache PDFBox, Apache POI             |
| **Video Transcript API**| youtube_transcript_api                |
| **Email Service**      | Jakarta Mail                          |
| **Server**             | Apache Tomcat 10.1.28                 |
| **Translation**        | Google Translate API                  |

---

## 📂 Database Structure
The system uses a **MySQL** database with the following tables:
- **`summaries`** – Stores generated summaries and related metadata.  
- **`user_notes`** – Stores notes created by users linked to their sessions.  
- **`transcripts`** – Stores transcribed text from audio/video input for future reference.  

---

## 📸 Screenshots & UI Preview

Here are the screenshots of the application showcasing various modules and UI components:

### 🏠 Home Page (Dashboard)
![Home Page](https://example.com/homepage.png)

### 📄 Upload Document Module
![Upload Document Module](https://example.com/upload_document.png)

### 📝 Summary Module
![Summary Module](https://example.com/summary_module.png)

### 🤖 InfoBuddy Chatbot
![Chatbot Module](https://example.com/chatbot.png)

### 🎙️ Speech Recognition and Summarizer
![Speech Summarizer Module](https://example.com/speech_module.png)

### 🎥 YouTube Video Summarizer
![YouTube Summarizer Module](https://example.com/youtube_module.png)

### 🗒️ Note-taking Tool
![Note-taking Module](https://example.com/note_taking.png)

### 🌐 Language Translation Module
![Language Translation Module](https://example.com/translation_module.png)

---



## 💻 Software & Hardware Requirements

### 📦 Software Requirements
| S. No. | Software Component         | Purpose                                  |
|--------|----------------------------|------------------------------------------|
| 1      | Windows/Linux OS           | Development and deployment environment   |
| 2      | Eclipse IDE / IntelliJ     | Java web app development                 |
| 3      | Apache Tomcat 9+           | Hosting Java servlets and JSP pages      |
| 4      | MySQL Server               | Database management                       |
| 5      | Python 3.9+                | NLP, transcription, backend AI           |
| 6      | JDK 11+                    | Java code execution                       |
| 7      | OpenAI / Gemini APIs       | Summarization and chatbot functionality  |
| 8      | YouTube Transcript API     | Video transcript extraction               |
| 9      | HTML, CSS, JavaScript      | Frontend design and interactivity         |

### ⚙️ Hardware Requirements
| S. No. | Hardware Component      | Minimum Specification                       |
|--------|--------------------------|---------------------------------------------|
| 1      | Processor               | Intel i5 / AMD Ryzen 5 or higher            |
| 2      | RAM                     | 8 GB or more                                 |
| 3      | Storage                 | Minimum 256 GB SSD / HDD                    |
| 4      | Microphone & Speakers   | Standard audio peripherals                   |
| 5      | GPU (Optional)          | For faster transcription with Whisper/PyTorch|
| 6      | Internet Connection     | Stable broadband for API access              |

---

## ⚙️ Functional Requirements
✅ **File Upload & Summarization** – Document upload & concise summary.  
✅ **Audio/Video Transcription** – Upload & transcript generation.  
✅ **YouTube Transcript Summarization** – Input video URL & receive summary.  
✅ **Interactive Chatbot Support** – Real-time help based on user queries.  
✅ **Dynamic Summary Length Control** – Short/medium/long summary options.  
✅ **User Input Validation** – Clear error handling for invalid inputs.  
✅ **Data Persistence** – Summaries & transcripts stored in the database.  
✅ **Language Translation** – Translate summaries & notes to desired language.

---

## 🔧 Non-Functional Requirements
✅ **Performance** – Most operations complete within 10–20 seconds.  
✅ **Usability** – Clean, intuitive interface for all users.  
✅ **Reliability** – Minimal downtime, robust error handling.  
✅ **Scalability** – Supports growing user base and feature expansion.  
✅ **Security** – HTTPS, input sanitization, and secure file handling.

---

## 🧩 Methodology & Modules
The project follows a modular design approach:

🚀 **Modules:**  
1️⃣ **Home Page (Dashboard)** – Central navigation hub.  
2️⃣ **Upload Document Module** – File input & text extraction.  
3️⃣ **Summary Module** – NLP-driven summarization.  
4️⃣ **InfoBuddy Chatbot** – Real-time support & interaction.  
5️⃣ **Speech Recognition & Summarizer** – Voice-to-text & summary.  
6️⃣ **YouTube Video Summarizer** – Transcript & summary from video links.  
7️⃣ **Note-taking Tool** – Create & manage notes linked to sessions.  
8️⃣ **Language Translation Module** – Translates summaries & notes to different languages.

---

## ⚙️ Techniques & Algorithms
- **NLP** – Tokenization, lemmatization, POS tagging for structured text processing.  
- **Extractive Summarization** – TextRank for key sentence extraction.  
- **Abstractive Summarization** – GPT/Gemini for fluent, human-like summaries.  
- **Speech-to-Text** – Web Speech API & Whisper Model for audio transcription.  
- **YouTube Transcript Extraction** – youtube_transcript_api for video content parsing.  
- **Translation** – Google Translate API for multi-language support.  

---

## 🤝 Contributions
We welcome contributions! Follow these steps:

1️⃣ **Fork the repo on GitHub.**  
2️⃣ **Clone to your local machine:**
```bash
git clone https://github.com/YourUsername/intelligent-multimedia-summarizer.git
````
3️⃣ Create a new branch:
```bash
git checkout -b feature-name
`````

4️⃣ Make your changes and commit:

````bash
git add .
git commit -m "Added new feature X"
````
5️⃣ Push to your fork and submit a pull request!
## 📜 License
© 2025 Krishna Das. All rights reserved.

