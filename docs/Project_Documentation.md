# AppVerse AI – Smart App Marketplace & Analytics Platform
## Project Documentation

---

## 1. Introduction

### 1.1 Project Title
**AppVerse AI – Smart App Marketplace & Analytics Platform**

### 1.2 Project Overview
AppVerse AI is a full-stack web application that serves as a smart app marketplace platform. It allows developers to publish and manage their applications, users to browse, download, and review apps, and administrators to oversee the platform. The system implements role-based access control with three distinct user roles: User, Developer, and Admin.

### 1.3 Objective
- To build a centralized marketplace for application discovery and distribution
- To provide developers with a console for app publishing, version management, and performance analytics
- To implement secure authentication and authorization using JWT tokens
- To enable users to discover apps through search, category filtering, and trending/top-rated sections
- To provide administrators with tools for user management and platform moderation

### 1.4 Scope
The application covers:
- User registration and authentication (JWT-based)
- App publishing, updating, and deletion
- App browsing, searching, and filtering
- Review and rating system
- Developer analytics dashboard
- Admin user and app management
- MySQL database persistence via JDBC

---

## 2. Technology Stack

### 2.1 Backend Technologies
| Technology | Version | Purpose |
|-----------|---------|---------|
| Java | 17 | Programming language |
| Spring Boot | 3.2.0 | Application framework |
| Spring Security | 6.x | Authentication & authorization |
| Spring Data JPA | 3.x | Data access layer (ORM) |
| Hibernate | 6.x | Object-Relational Mapping |
| MySQL | 8.0 | Relational database |
| JWT (jjwt) | 0.12.3 | Token-based authentication |
| Lombok | 1.18.36 | Boilerplate code reduction |
| Maven | 3.x | Build and dependency management |
| BCrypt | - | Password hashing |

### 2.2 Frontend Technologies
| Technology | Version | Purpose |
|-----------|---------|---------|
| React | 18.2 | UI library |
| React Router | 6.21 | Client-side routing |
| Axios | 1.6.2 | HTTP client |
| Bootstrap | 5.3.2 | CSS framework |
| Chart.js | 4.4.1 | Data visualization |
| React Toastify | 9.1.3 | Toast notifications |
| React Icons | 4.12.0 | Icon library |

### 2.3 Development Tools
| Tool | Purpose |
|------|---------|
| VS Code | IDE |
| MySQL Workbench | Database management |
| Postman | API testing |
| Git & GitHub | Version control |
| Maven Wrapper | Consistent build environment |

---

## 3. System Architecture

### 3.1 Architecture Overview
The application follows a **three-tier architecture**:

1. **Presentation Layer (Frontend):** React-based SPA with component-based UI
2. **Application Layer (Backend):** Spring Boot REST API with business logic
3. **Data Layer:** MySQL database accessed via Spring Data JPA

### 3.2 Architecture Pattern
The backend follows the **MVC (Model-View-Controller)** pattern extended with a Service layer:

```
Client (React) → Controller → Service → Repository → Database (MySQL)
                                ↓
                         DTO ← Entity Mapping
```

### 3.3 Communication
- Frontend communicates with backend via RESTful HTTP APIs
- JSON is used as the data exchange format
- JWT tokens are sent in the `Authorization` header for authenticated requests
- CORS is configured to allow cross-origin requests

---

## 4. Modules

### 4.1 Authentication Module
**Purpose:** Handles user registration, login, and session management.

**Features:**
- User registration with role selection (USER/DEVELOPER)
- Login with username/password
- JWT token generation and validation
- Password hashing with BCrypt
- Auto-logout on token expiry

**Components:**
- Backend: AuthController, UserService, JwtUtil, JwtAuthFilter, CustomUserDetailsService
- Frontend: Login.js, Register.js, authService.js

### 4.2 App Marketplace Module
**Purpose:** Core marketplace functionality for browsing and discovering apps.

**Features:**
- List all active apps
- Search apps by name or description
- Filter apps by category
- View trending apps (most downloaded)
- View top-rated apps
- Detailed app view with metadata
- App download recording

**Components:**
- Backend: AppController, AppService, AppRepository
- Frontend: AppListing.js, AppCard.js, AppDetails.js, appService.js

### 4.3 Review & Rating Module
**Purpose:** Allows users to rate and review apps.

**Features:**
- Submit reviews with 1-5 star rating
- Write review comments (max 1000 chars)
- One review per user per app constraint
- View all reviews for an app
- Edit/delete own reviews
- Automatic average rating recalculation

**Components:**
- Backend: ReviewController, ReviewService, ReviewRepository
- Frontend: ReviewForm.js, ReviewCard.js, ReviewDashboard.js, reviewService.js

### 4.4 Developer Console Module
**Purpose:** Tools for developers to manage their published apps.

**Features:**
- Publish new apps with validation
- Update app details
- Delete apps
- Add new versions with release notes
- View version history

**Components:**
- Backend: DeveloperController, DeveloperService, AppVersionRepository
- Frontend: DeveloperDashboard.js, AppUpload.js, AppManage.js, developerService.js

### 4.5 Analytics Module
**Purpose:** Provides developers with performance insights.

**Features:**
- Total downloads across all apps
- Average rating across all apps
- Total reviews count
- Downloads per app (Bar chart)
- Download distribution (Doughnut chart)
- Monthly download trends

**Components:**
- Backend: DeveloperService (getAnalytics), DownloadRepository (custom queries)
- Frontend: DeveloperAnalytics.js (Chart.js visualizations)

### 4.6 Admin Dashboard Module
**Purpose:** Platform administration and moderation.

**Features:**
- Platform statistics (total users, apps, categories)
- User management (view, filter by role, delete)
- App moderation (deactivate/activate apps)
- Category management (CRUD operations)
- Recent users overview

**Components:**
- Backend: AdminController, CategoryController
- Frontend: AdminDashboard.js, UserManagement.js, adminService.js

---

## 5. Database Design

### 5.1 Database: MySQL 8.0
- Database Name: `appversedb`
- Connection: JDBC (`jdbc:mysql://localhost:3306/appversedb`)
- Schema Generation: Auto (Hibernate `ddl-auto=update`)

### 5.2 Tables

#### Users Table
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| username | VARCHAR(50) | NOT NULL, UNIQUE |
| email | VARCHAR(255) | NOT NULL, UNIQUE |
| password | VARCHAR(255) | NOT NULL |
| full_name | VARCHAR(255) | NOT NULL |
| role | ENUM('USER','DEVELOPER','ADMIN') | NOT NULL |
| created_at | DATETIME | NOT NULL |
| updated_at | DATETIME | |

#### Apps Table
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| name | VARCHAR(100) | NOT NULL |
| description | VARCHAR(2000) | NOT NULL |
| current_version | VARCHAR(50) | NOT NULL |
| icon_url | VARCHAR(255) | |
| screenshot_url | VARCHAR(255) | |
| download_count | BIGINT | DEFAULT 0 |
| average_rating | DOUBLE | DEFAULT 0.0 |
| active | BOOLEAN | DEFAULT TRUE |
| developer_id | BIGINT | FOREIGN KEY → users(id) |
| category_id | BIGINT | FOREIGN KEY → categories(id) |
| created_at | DATETIME | NOT NULL |
| updated_at | DATETIME | |

#### Categories Table
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| name | VARCHAR(100) | NOT NULL, UNIQUE |
| description | VARCHAR(255) | |
| icon_url | VARCHAR(255) | |

#### Reviews Table
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| rating | INT | NOT NULL (1-5) |
| comment | VARCHAR(1000) | |
| user_id | BIGINT | FOREIGN KEY → users(id) |
| app_id | BIGINT | FOREIGN KEY → apps(id) |
| created_at | DATETIME | NOT NULL |
| updated_at | DATETIME | |
| | | UNIQUE(user_id, app_id) |

#### App_Versions Table
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| version_number | VARCHAR(50) | NOT NULL |
| release_notes | VARCHAR(2000) | |
| download_url | VARCHAR(255) | |
| file_size | BIGINT | |
| app_id | BIGINT | FOREIGN KEY → apps(id) |
| released_at | DATETIME | NOT NULL |

#### Downloads Table
| Column | Type | Constraints |
|--------|------|-------------|
| id | BIGINT | PRIMARY KEY, AUTO_INCREMENT |
| user_id | BIGINT | FOREIGN KEY → users(id) |
| app_id | BIGINT | FOREIGN KEY → apps(id) |
| version | VARCHAR(50) | |
| downloaded_at | DATETIME | NOT NULL |

### 5.3 Entity Relationships
- **User → Apps:** One-to-Many (A developer can publish many apps)
- **Category → Apps:** One-to-Many (A category contains many apps)
- **User → Reviews:** One-to-Many (A user can write many reviews)
- **App → Reviews:** One-to-Many (An app can have many reviews)
- **App → AppVersions:** One-to-Many (An app can have many versions)
- **User → Downloads:** One-to-Many (A user can download many apps)
- **App → Downloads:** One-to-Many (An app can be downloaded many times)

---

## 6. Design Patterns

### 6.1 MVC Pattern (Model-View-Controller)
- **Model:** Entity classes (User, App, Category, Review, AppVersion, Download)
- **View:** React frontend components
- **Controller:** REST controllers (AuthController, AppController, etc.)
- Business logic is separated into a Service layer

### 6.2 DTO Pattern (Data Transfer Object)
- Separate objects for API request/response (UserDTO, AppDTO, ReviewDTO, etc.)
- Prevents exposing internal entity structure
- Controls what data is sent to/from the client

### 6.3 Repository Pattern
- Spring Data JPA repositories abstract database operations
- Custom JPQL queries for complex operations (search, trending, analytics)
- Provides a clean data access interface

### 6.4 Service Layer Pattern
- Business logic encapsulated in service classes
- Interface-based design (AppService interface → AppServiceImpl)
- Separation of concerns between controllers and data access

### 6.5 Builder Pattern
- Lombok's @Builder annotation for entity construction
- Clean object creation without telescoping constructors

### 6.6 Interceptor Pattern
- Axios HTTP interceptor attaches JWT token to all outgoing requests
- JwtAuthFilter intercepts all incoming requests for token validation

### 6.7 Singleton Pattern
- Spring beans are singletons by default
- Single instances of services, repositories, and configurations

### 6.8 Observer Pattern
- JPA lifecycle callbacks (@PrePersist, @PreUpdate) for automatic timestamp management

### 6.9 Global Exception Handling
- @ControllerAdvice with @ExceptionHandler for centralized error responses
- Custom exceptions: ResourceNotFoundException, BadRequestException, UnauthorizedException

### 6.10 Role-Based Access Control (RBAC)
- Three roles: USER, DEVELOPER, ADMIN
- Spring Security's method-level security with @PreAuthorize
- Frontend PrivateRoute component for client-side route protection

---

## 7. API Documentation

### 7.1 Authentication APIs
| Method | Endpoint | Request Body | Response | Auth |
|--------|----------|-------------|----------|------|
| POST | `/api/auth/register` | `{username, email, password, fullName, role}` | UserDTO (201) | No |
| POST | `/api/auth/login` | `{username, password}` | `{token, username, role, userId}` (200) | No |

### 7.2 App APIs
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/apps` | Get all active apps | No |
| GET | `/api/apps/{id}` | Get app by ID | No |
| GET | `/api/apps/category/{categoryId}` | Filter by category | No |
| GET | `/api/apps/search?keyword=x` | Search apps | No |
| GET | `/api/apps/trending` | Most downloaded apps | No |
| GET | `/api/apps/top-rated` | Highest rated apps | No |
| POST | `/api/apps/{id}/download` | Record download | Yes |

### 7.3 Developer APIs
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/developer/apps` | My published apps | DEVELOPER |
| POST | `/api/developer/apps` | Publish new app | DEVELOPER |
| PUT | `/api/developer/apps/{id}` | Update my app | DEVELOPER |
| DELETE | `/api/developer/apps/{id}` | Delete my app | DEVELOPER |
| POST | `/api/developer/apps/{appId}/versions` | Add version | DEVELOPER |
| GET | `/api/developer/apps/{appId}/versions` | Version history | DEVELOPER |
| GET | `/api/developer/analytics` | Analytics data | DEVELOPER |

### 7.4 Review APIs
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/reviews/app/{appId}` | Reviews for app | No |
| GET | `/api/reviews/user` | My reviews | Yes |
| POST | `/api/reviews` | Create review | Yes |
| PUT | `/api/reviews/{id}` | Update review | Yes |
| DELETE | `/api/reviews/{id}` | Delete review | Yes |

### 7.5 Category APIs
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/categories` | All categories | No |
| GET | `/api/categories/{id}` | Category by ID | No |
| POST | `/api/categories` | Create category | ADMIN |
| PUT | `/api/categories/{id}` | Update category | ADMIN |
| DELETE | `/api/categories/{id}` | Delete category | ADMIN |

### 7.6 Admin APIs
| Method | Endpoint | Description | Auth |
|--------|----------|-------------|------|
| GET | `/api/admin/users` | All users | ADMIN |
| GET | `/api/admin/users/role/{role}` | Users by role | ADMIN |
| DELETE | `/api/admin/users/{id}` | Delete user | ADMIN |
| GET | `/api/admin/apps` | All apps | ADMIN |
| PUT | `/api/admin/apps/{id}/deactivate` | Deactivate app | ADMIN |
| GET | `/api/admin/dashboard` | Dashboard stats | ADMIN |

---

## 8. Security Implementation

### 8.1 Authentication Flow
```
1. User submits login credentials
2. Backend validates username/password
3. On success, JWT token is generated with:
   - Subject: username
   - Claims: role
   - Expiration: 24 hours
4. Token returned to frontend
5. Frontend stores token in localStorage
6. Axios interceptor attaches token to all subsequent requests
7. JwtAuthFilter validates token on each backend request
8. On token expiry, user is auto-logged out
```

### 8.2 Password Security
- Passwords are hashed using BCrypt before storage
- Raw passwords are never stored or logged
- Password validation: minimum 6 characters

### 8.3 Authorization
- URL-based security in SecurityConfig
- Role-based access: `hasRole("ADMIN")`, `hasAnyRole("DEVELOPER", "ADMIN")`
- Frontend PrivateRoute component prevents unauthorized navigation

### 8.4 CORS Configuration
- All origins allowed for development
- Allowed methods: GET, POST, PUT, DELETE, OPTIONS
- Credentials supported
- Authorization header allowed

---

## 9. Frontend Component Structure

```
src/
├── App.js                          (Main routing)
├── App.css                         (Global styles)
├── index.js                        (Entry point)
├── components/
│   ├── auth/
│   │   ├── Login.js                (Login form with demo credentials)
│   │   └── Register.js             (Registration with role selection)
│   ├── apps/
│   │   ├── AppListing.js           (App marketplace with search/filter)
│   │   ├── AppCard.js              (App card component)
│   │   └── AppDetails.js           (Full app details + reviews)
│   ├── reviews/
│   │   ├── ReviewDashboard.js      (My reviews list)
│   │   ├── ReviewForm.js           (Star rating + comment form)
│   │   └── ReviewCard.js           (Individual review display)
│   ├── developer/
│   │   ├── DeveloperDashboard.js   (Developer's app list + stats)
│   │   ├── AppUpload.js            (Publish new app form)
│   │   ├── AppManage.js            (Edit app + version management)
│   │   └── DeveloperAnalytics.js   (Charts + metrics)
│   ├── admin/
│   │   ├── AdminDashboard.js       (Platform stats + overview)
│   │   └── UserManagement.js       (User table + role filter)
│   └── common/
│       ├── Navbar.js               (Role-based navigation)
│       ├── Footer.js               (Site footer)
│       └── PrivateRoute.js         (Auth + role route guard)
└── services/
    ├── api.js                      (Axios instance + JWT interceptor)
    ├── authService.js              (Login/register/logout)
    ├── appService.js               (App browsing APIs)
    ├── reviewService.js            (Review CRUD APIs)
    ├── developerService.js         (Developer APIs)
    └── adminService.js             (Admin APIs)
```

---

## 10. Backend Project Structure

```
src/main/java/com/appverse/
├── AppverseApplication.java        (Main class)
├── config/
│   ├── SecurityConfig.java         (Spring Security config)
│   ├── CorsConfig.java             (CORS settings)
│   └── DataInitializer.java        (Seed data on startup)
├── controller/
│   ├── AuthController.java         (Login/Register endpoints)
│   ├── AppController.java          (App CRUD + search)
│   ├── ReviewController.java       (Review CRUD)
│   ├── DeveloperController.java    (Developer endpoints)
│   ├── AdminController.java        (Admin endpoints)
│   └── CategoryController.java     (Category CRUD)
├── dto/
│   ├── UserDTO.java
│   ├── AppDTO.java
│   ├── ReviewDTO.java
│   ├── CategoryDTO.java
│   ├── AppVersionDTO.java
│   ├── AnalyticsDTO.java
│   ├── LoginRequest.java
│   ├── LoginResponse.java
│   └── RegisterRequest.java
├── entity/
│   ├── User.java
│   ├── App.java
│   ├── Category.java
│   ├── Review.java
│   ├── AppVersion.java
│   └── Download.java
├── enums/
│   └── Role.java                   (USER, DEVELOPER, ADMIN)
├── exception/
│   ├── GlobalExceptionHandler.java
│   ├── ResourceNotFoundException.java
│   ├── BadRequestException.java
│   └── UnauthorizedException.java
├── repository/
│   ├── UserRepository.java
│   ├── AppRepository.java
│   ├── CategoryRepository.java
│   ├── ReviewRepository.java
│   ├── AppVersionRepository.java
│   └── DownloadRepository.java
├── security/
│   ├── JwtUtil.java                (Token generation/validation)
│   ├── JwtAuthFilter.java          (Request filter)
│   └── CustomUserDetailsService.java
└── service/
    ├── UserService.java
    ├── AppService.java
    ├── ReviewService.java
    ├── DeveloperService.java
    ├── CategoryService.java
    └── impl/
        ├── UserServiceImpl.java
        ├── AppServiceImpl.java
        ├── ReviewServiceImpl.java
        ├── DeveloperServiceImpl.java
        └── CategoryServiceImpl.java
```

---

## 11. Seed / Demo Data

### 11.1 Demo User Accounts
| Username | Password | Role | Purpose |
|----------|----------|------|---------|
| admin | admin123 | ADMIN | Test admin features |
| developer | dev123 | DEVELOPER | Test developer features |
| user | user123 | USER | Test user features |

### 11.2 Default Categories
Productivity, Games, Education, Social, Entertainment, Health & Fitness, Finance, Utilities

---

## 12. Setup & Installation

### 12.1 Prerequisites
- Java JDK 17
- Node.js 18+
- MySQL 8.0
- Git

### 12.2 Database Setup
```sql
CREATE DATABASE IF NOT EXISTS appversedb;
```

### 12.3 Backend Setup
```cmd
cd appverse-backend
.\mvnw.cmd clean spring-boot:run
```
Backend runs on: http://localhost:8090

### 12.4 Frontend Setup
```cmd
cd appverse-frontend
npm install
npm start
```
Frontend runs on: http://localhost:3000

---

## 13. Testing

### 13.1 Test Scenarios

| # | Test Case | Steps | Expected Result |
|---|-----------|-------|-----------------|
| 1 | User Registration | Fill form → Submit | Account created, redirect to login |
| 2 | User Login | Enter credentials → Submit | JWT token received, redirect to home |
| 3 | Browse Apps | Navigate to /apps | List of all active apps displayed |
| 4 | Search Apps | Enter keyword in search | Filtered apps shown |
| 5 | Filter by Category | Select category | Only apps in category shown |
| 6 | View App Details | Click on app card | Full details + reviews displayed |
| 7 | Submit Review | Rate + Comment → Submit | Review saved, avg rating updated |
| 8 | Publish App | Fill app form → Submit | App created, visible in marketplace |
| 9 | View Analytics | Navigate to analytics | Charts with download/rating data |
| 10 | Admin User Management | View users → Delete | User removed from system |
| 11 | Category Management | Create/Edit/Delete category | CRUD operations successful |
| 12 | JWT Expiry | Wait 24h or clear token | Redirected to login page |

---

## 14. Conclusion

AppVerse AI is a comprehensive full-stack marketplace platform demonstrating modern web development practices. It implements secure authentication, role-based authorization, RESTful API design, and responsive UI. The application uses industry-standard design patterns and follows clean architecture principles with clear separation of concerns.

The platform is extensible and can be enhanced with AI-based recommendations, payment integration, cloud deployment, and mobile applications in future iterations.

---
