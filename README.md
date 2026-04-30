# TokenAPI

A lightweight authentication API built with Spring Boot, providing secure JWT-based login and token refresh functionality.  
This project demonstrates a clean and maintainable architecture for handling Access Tokens, Refresh Tokens, and user authentication using Spring Security and MyBatis.

---

## 🚀 Features

- User authentication with username & password  
- Issue **Access Token** and **Refresh Token**  
- Refresh expired Access Tokens using a valid Refresh Token  
- JWT validation via custom filter  
- MyBatis-based user lookup  
- Environment variable–driven configuration  
- Swagger UI for API documentation  

---

## 🛠️ Tech Stack

- **Java 17**  
- **Spring Boot**  
- **Spring Security**  
- **MyBatis**  
- **MySQL**  
- **JWT (jjwt)**  
- **Lombok**  
- **Swagger / OpenAPI**  

---

## 📁 Project Structure
```
src/main/java/jp/co/rin/tokenApi/
├── config/                 # Security & OpenAPI configuration
├── controller/             # REST controllers
├── jwt/                    # Token provider & JWT utilities
├── object/dto/             # Request/Response DTOs
├── repository/             # MyBatis mapper interfaces
├── service/                # Token services
├── service/impl/           # Service implementations
└── TokenApiApplication.java
```
---

## 🔐 JWT Overview

### Access Token
- Short-lived  
- Used for authenticating API requests  
- Contains user information and expiration  

### Refresh Token
- Longer-lived  
- Stored securely on the client  
- Used to issue a new Access Token  

### Why environment variables?
- Secret keys and expiration times should **never** be hardcoded  
- Environment variables allow safe deployment across environments  

---

## 📘 API Endpoints

### **POST** `/api/auth/login`
Authenticate user and issue tokens.

**Request Body**
```json
{
  "userId": "string",
  "password": "string"
}
```
**Response**
```json
{
  "accessToken": "string",
  "refreshToken": "string"
}
```
### **POST** `/api/auth/refresh`
Issue a new Access Token using a valid Refresh Token.

**Request Body**
```json
{
  "refreshToken": "string"
}
```

**Response**
```json
{
  "accessToken": "string",
  "refreshToken": "string"
}
```
---

## ⚙️ Environment Variables

Set the following variables before running the application:
```
TOKEN_SECRET_KEY=your-secret-key
TOKEN_ACCESS_EXPIRE=600000
TOKEN_REFRESH_EXPIRE=1209600000

DB_HOST=localhost
DB_PORT=3306
DB_NAME=your_db
DB_USER=root
DB_PASSWORD=your_password
```
---

## 🧪 Setup Instructions

1. Copy `application-example.yml` → `application.yml`
2. Set environment variables
3. Create required MySQL tables
4. Run the application: ./mvnw spring-boot:run
---

## 📌 TODO

- Add error handling  
- Add logging  
- Add user registration endpoint  
- Add password update endpoint  
- Add test cases  

---

## 📄 License

This project is for learning and portfolio purposes.

---

