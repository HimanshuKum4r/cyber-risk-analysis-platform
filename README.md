# AI-Powered Phishing Detection Platform

A production-oriented phishing detection platform built with **Java**, **Spring Boot**, and modern backend engineering practices.

This project started as a simple phishing detection application but is now being rebuilt from the ground up with a focus on **security, scalability, clean architecture, and real-world backend design**. The goal is not only to detect phishing attacks but also to understand how production SaaS applications are engineered.

> 🚧 The project is currently under active development and is being built in phases.

---

## Current Status

### ✅ Phase 1: Authentication & Authorization (Completed)

The first phase establishes a secure foundation for the platform by implementing a complete authentication lifecycle instead of basic JWT authentication.

### Authentication

- User Registration
- Secure Login
- JWT Access Tokens
- Refresh Tokens
- Refresh Token Rotation
- Logout
- Logout from All Devices

### Account Security

- Email Verification
- Forgot Password
- Reset Password
- Change Password

### Authorization

- Role-Based Access Control (RBAC)
- User → Role → Permission Architecture
- Permission-Based Authorization
- Spring Security Integration

---

## Production Techniques

During this phase, the focus was on implementing patterns commonly used in production applications.

- Spring Security 6
- Stateless JWT Authentication
- Database-backed Refresh Tokens
- Refresh Token Rotation
- Secure Password Hashing (BCrypt)
- Token Revocation Strategy
- Event-Driven Email Workflows
- Global Exception Handling
- Bean Validation
- Transaction Management
- DTO Mapping (MapStruct)
- Layered Architecture

---

# Authentication Module

## Authentication Flow

```mermaid
flowchart TD

A([User])

A --> B[Register]
B --> C[Email Verification]
C --> D[Login]

D --> E[Authentication Manager]
E --> F[Validate Credentials]

F -->|Valid| G[Generate JWT Access Token]
F -->|Valid| H[Generate Refresh Token]

G --> I[Access Protected APIs]

I --> J{Access Token Expired?}

J -- No --> K[Process Request]

J -- Yes --> L[Validate Refresh Token]

L --> M{Refresh Token Valid?}

M -- Yes --> N[Rotate Refresh Token]
N --> O[Generate New Access Token]
O --> I

M -- No --> D

I --> P[Logout]
P --> Q[Revoke Refresh Token]

A --> R[Forgot Password]
R --> S[Generate Reset Token]
S --> T[Reset Password]
T --> U[Revoke All User Sessions]
U --> D
```



## Authentication Architecture

```mermaid
flowchart LR

subgraph Client
A[Web / Mobile Client]
end

subgraph Security
B[Spring Security Filter Chain]
C[JWT Authentication Filter]
D[Authentication Manager]
E[UserDetailsService]
F[Security Context]
end

subgraph Authentication
G[Authentication Controller]
H[Authentication Service]
I[JWT Service]
J[Refresh Token Service]
K[Verification Token Service]
L[Password Reset Service]
end

subgraph Authorization
M[RBAC Engine]
N[User]
O[Role]
P[Permission]
end

subgraph Infrastructure
Q[(MySQL Database)]
R[Application Events]
S[Email Service]
end

A --> G
G --> H

H --> D
H --> I
H --> J
H --> K
H --> L

D --> E
E --> N

I --> B
B --> C
C --> F

F --> M
M --> O
O --> P

J --> Q
K --> Q
L --> Q
N --> Q

H --> R
R --> S
```

## Tech Stack

### Backend

- Java 21
- Spring Boot 3
- Spring Security
- Spring Data JPA
- Hibernate

### Database

- MySQL

### Authentication

- JWT
- Refresh Tokens
- BCrypt Password Hashing

### Utilities

- Maven
- Lombok
- MapStruct

---

## Project Structure

```
src
├── auth
├── config
├── security
├── user
├── role
├── permission
├── email
├── exception
├── common
└── util
```

---

## Roadmap

### ✅ Phase 1 — Authentication & Authorization

- Authentication
- Authorization
- Account Security
- Session Management

### 🚧 Phase 2 — Multi-Tenant Architecture

- Organization Management
- Individual & Organization Workspaces
- Tenant Isolation
- Team Invitations
- Organization Roles

### ⏳ Phase 3 — Detection Engine

- URL Analysis
- Redirect Analysis
- Domain Analysis
- Keyword Detection
- Risk Scoring Engine

### ⏳ Phase 4 — AI Intelligence

- Gemini Integration
- AI Threat Analysis
- Explainable AI
- Security Recommendations

### ⏳ Phase 5 — Enterprise Features

- Audit Logging
- Active Sessions
- Device Management
- Rate Limiting
- Brute Force Protection

### ⏳ Phase 6 — Deployment

- Docker
- CI/CD
- Monitoring
- Logging
- Production Deployment


---

## Why This Project?

This project is part of my journey to become a better backend engineer.

Instead of focusing only on implementing features, I'm using it to learn how production systems are designed—from authentication and authorization to multi-tenancy, security, and scalable backend architecture.

Every phase introduces new engineering challenges, and I'll continue improving the platform as I explore more advanced backend concepts.

---

## Contributing

Suggestions, feedback, and discussions are always welcome.

---

## License

This project is licensed under the MIT License.
