# 🤖 AI Chatbot using Spring Boot & Spring AI

An AI-powered chatbot built using **Spring Boot**, **Spring AI**, **Ollama**, **PostgreSQL**, and a responsive **HTML, CSS, and JavaScript** frontend. The application provides secure user authentication, AI-powered conversations, chat history management, sentiment analysis, document upload, PDF parsing, and personalized responses.

---

# 🚀 Features

## 🔐 Authentication & Security
- User Registration
- User Login
- JWT Authentication
- Spring Security
- Password Encryption (BCrypt)
- Email Verification

## 💬 AI Chat Features
- AI Chat using Spring AI
- Ollama Integration
- Chat History Storage
- Get Chat History API
- Delete Chat API
- Session Management
- Personalized AI Responses
- AI Conversation Summary
- Sentiment Analysis

## 📄 Document Features
- Document Upload API
- PDF Upload
- PDF Parsing using Apache PDFBox
- Document Storage

## 🎨 Frontend
- Responsive User Interface
- HTML5
- CSS3
- JavaScript (ES6)
- REST API Integration using Fetch API
- Login & Registration Pages
- AI Chat Interface

## 🗄 Database
- PostgreSQL
- Spring Data JPA
- Hibernate ORM

---

# 🛠 Tech Stack

## Backend
- Java 21
- Spring Boot
- Spring AI
- Spring Security
- Spring Data JPA
- Hibernate
- Maven

## Frontend
- HTML5
- CSS3
- JavaScript

## Database
- PostgreSQL

## AI
- Ollama
- Phi3 Mini / Llama3

## Libraries
- Apache PDFBox
- JWT
- Lombok

## Testing
- Postman

---

# 📂 Project Structure

```
src
├── Controller
├── Service
│   └── impl
├── Repository
├── Entity
├── DTO
├── Security
├── Config
├── Resources
│   ├── static
│   │   ├── css
│   │   ├── js
│   │   ├── index.html
│   │   ├── login.html
│   │   ├── register.html
│   │   └── dashboard.html
│   └── application.properties
```

---

# ✅ Implemented Modules

- JWT Authentication
- User Registration
- User Login
- Email Verification
- AI Chat API
- Chat History Storage
- Get Chat History API
- Delete Chat API
- Session Management
- Sentiment Analysis
- AI Conversation Summary
- Personalized AI Responses
- PDF Upload
- PDF Parsing
- Document Storage

---

# 📡 REST APIs

## Authentication

```
POST /api/auth/register
POST /api/auth/login
GET  /api/auth/verify
```

## Chat

```
POST   /api/chat
GET    /api/chat/history/{userId}
DELETE /api/chat/{chatId}
```

## Document

```
POST /api/document/upload
```

---

# ⚙ Installation

## Clone Repository

```bash
git clone https://github.com/krups826/AI-Chatbot.git
```

## Configure PostgreSQL

Update **application.properties**

```properties
spring.datasource.url=jdbc:postgresql://localhost:5432/ai_chatbot
spring.datasource.username=postgres
spring.datasource.password=your_password
```

## Configure Ollama

```properties
spring.ai.ollama.base-url=http://localhost:11434
spring.ai.ollama.chat.options.model=phi3:mini
```

Download the model:

```bash
ollama pull phi3:mini
```

Run the application:

```bash
mvn spring-boot:run
```

---

# 🔮 Future Enhancements

- Vector Embeddings
- PGVector Integration
- RAG (Retrieval-Augmented Generation)
- Multi-Document Support
- AI Recommendation System
- WebSocket Real-Time Chat
- Docker Deployment
- CI/CD Pipeline
- Cloud Deployment (AWS / Render / Railway)

---

# 👨‍💻 Developer

**Krupa Nirmal**

### Skills
- Java
- Spring Boot
- Spring Security
- Spring AI
- PostgreSQL
- HTML
- CSS
- JavaScript
- REST APIs
- JWT Authentication
- Ollama
- Apache PDFBox

---

## ⭐ Support

If you found this project helpful, please consider giving it a **⭐ Star** on GitHub.
