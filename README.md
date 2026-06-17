# AppVerse AI – Smart App Marketplace & Analytics Platform

A full-stack application with a **Spring Boot** backend and **React** frontend.

---

## Prerequisites (Windows)

| Tool | Version | Download |
|------|---------|----------|
| Java JDK | 17 | https://adoptium.net/temurin/releases/?version=17&os=windows |
| Node.js | 18+ | https://nodejs.org/ |
| Git | Latest | https://git-scm.com/download/win |

> **Note:** Make sure `java`, `node`, `npm`, and `git` are available in your PATH after installation.

---

## Clone the Repository

```bash
git clone https://github.com/Manjunath28/java.git
cd java
```

---

## Backend Setup

### 1. Navigate to backend folder

```bash
cd appverse-backend
```

### 2. Run the backend

```bash
.\mvnw.cmd clean spring-boot:run
```

> On first run, Maven will download all dependencies (may take a few minutes).

### 3. Verify

- Backend runs on: **http://localhost:8080**
- H2 Console: **http://localhost:8080/h2-console**
  - JDBC URL: `jdbc:h2:mem:appversedb`
  - Username: `sa`
  - Password: *(leave empty)*

---

## Frontend Setup

Open a **new terminal window**.

### 1. Navigate to frontend folder

```bash
cd appverse-frontend
```

### 2. Install dependencies

```bash
npm install
```

### 3. Start the frontend

```bash
npm start
```

### 4. Verify

- Frontend runs on: **http://localhost:3000**
- It will automatically open in your browser.

---

## Demo Accounts

| Role | Username | Password |
|------|----------|----------|
| Admin | admin | admin123 |
| Developer | developer | dev123 |
| User | user | user123 |

---

## Running Both Together (Quick Start)

Open **two terminal windows**:

**Terminal 1 – Backend:**
```bash
cd appverse-backend
.\mvnw.cmd clean spring-boot:run
```

**Terminal 2 – Frontend:**
```bash
cd appverse-frontend
npm install
npm start
```

> Always start the backend **first**, then the frontend.

---

## Tech Stack

### Backend
- Java 17
- Spring Boot 3.2.0
- Spring Security + JWT Authentication
- Spring Data JPA
- H2 Database (in-memory, development)
- Lombok
- Maven

### Frontend
- React 18
- React Router 6
- Axios
- Bootstrap 5
- Chart.js
- React Toastify

---

## API Endpoints

| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| POST | `/api/auth/register` | Register new user | No |
| POST | `/api/auth/login` | Login | No |
| GET | `/api/apps` | List all apps | No |
| GET | `/api/apps/{id}` | Get app details | No |
| GET | `/api/categories` | List categories | No |
| POST | `/api/developer/apps` | Upload new app | Developer |
| GET | `/api/developer/apps` | Developer's apps | Developer |
| GET | `/api/developer/analytics` | Developer analytics | Developer |
| POST | `/api/apps/{id}/reviews` | Add review | User |
| GET | `/api/admin/users` | List all users | Admin |

---

## Troubleshooting (Windows)

### `mvnw.cmd` not recognized
Make sure you are inside the `appverse-backend` folder. Run:
```bash
dir mvnw.cmd
```
If it shows the file, try: `.\mvnw.cmd clean spring-boot:run`

### Java version error
Verify Java 17 is installed:
```bash
java -version
```
If a different version appears, set `JAVA_HOME`:
```bash
set JAVA_HOME=C:\Program Files\Eclipse Adoptium\jdk-17.x.x-hotspot
set PATH=%JAVA_HOME%\bin;%PATH%
```

### Port already in use
Kill the process using the port:
```bash
netstat -ano | findstr :8080
taskkill /PID <PID_NUMBER> /F
```

### `npm install` fails
Delete `node_modules` and retry:
```bash
rmdir /s /q node_modules
del package-lock.json
npm install
```

---

## Project Structure

```
java/
├── appverse-backend/
│   ├── src/main/java/com/appverse/
│   │   ├── config/          # Security, CORS, Data initializer
│   │   ├── controller/      # REST controllers
│   │   ├── dto/             # Data transfer objects
│   │   ├── entity/          # JPA entities
│   │   ├── enums/           # Role enum
│   │   ├── exception/       # Global exception handling
│   │   ├── repository/      # Spring Data repositories
│   │   ├── security/        # JWT, filters, user details
│   │   └── service/         # Business logic
│   ├── src/main/resources/
│   │   └── application.properties
│   ├── mvnw / mvnw.cmd      # Maven wrapper
│   └── pom.xml
├── appverse-frontend/
│   ├── public/
│   ├── src/
│   │   ├── components/       # React components
│   │   │   ├── admin/
│   │   │   ├── apps/
│   │   │   ├── auth/
│   │   │   ├── common/
│   │   │   ├── developer/
│   │   │   └── reviews/
│   │   ├── services/         # API service layer
│   │   ├── App.js
│   │   └── index.js
│   └── package.json
├── .gitignore
└── README.md
```
