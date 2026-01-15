# Gemma 2 RAG Chatbot

A locally hosted AI chatbot featuring short-term conversational memory and long-term knowledge retrieval using Retrieval-Augmented Generation (RAG).

---

## 🛠️ Tech Stack

### Backend
- **Java 17+**
- **Spring Boot 3**
- **Spring AI**
- **Spring WebFlux**

### AI & Machine Learning
- **Ollama** — Local model orchestration  
- **Gemma 2 (LLM)** — Google’s high-efficiency reasoning model  
- **Nomic-Embed-Text** — High-performance embedding model  
- **RAG Pipeline** — Retrieval-Augmented Generation for private and domain-specific data  

### Database & Storage
- **PostgreSQL** — Relational data management  
- **PGVector** — High-speed vector similarity search  
- **In-Memory Storage** — Session-based conversational memory  

### Data Ingestion
- **Apache Tika** — Universal document reader (PDF, DOCX, TXT, etc.)  
- **Token Text Splitter** — Intelligent chunking for optimized context windows  
