# 🔬 Privacy-Aware Forensic DNA Matching System

> A production-inspired enterprise application for secure forensic DNA profile management, STR-based DNA matching, role-based access control, and containerized deployment.

![Java](https://img.shields.io/badge/Java-21-red)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-3.x-green)
![React](https://img.shields.io/badge/React-18-blue)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-16-blue)
![Docker](https://img.shields.io/badge/Docker-Containerized-blue)
![JWT](https://img.shields.io/badge/JWT-Authentication-orange)
![License](https://img.shields.io/badge/License-MIT-yellow)

---

# 📖 Overview

The **Privacy-Aware Forensic DNA Matching System** is an enterprise-style web application developed to simulate how forensic laboratories securely manage DNA evidence and perform DNA profile matching while maintaining privacy and access control.

The application supports secure authentication, role-based authorization, DNA profile management, STR genotype storage, evidence management, DNA matching, auditing, and Docker-based deployment.

---

# 🚀 Features

## Authentication

- JWT Authentication
- Secure Login
- Password Encryption
- Session Management

---

## Role-Based Access Control

Three user roles are supported:

- 👨‍💼 Admin
- 🕵️ Investigator
- 🚔 Field Officer

Each role has different permissions within the system.

---

## DNA Profile Management

- Create DNA Profiles
- Store STR Genotypes
- Population Information
- Privacy Encoding
- Profile Search

---

## Evidence Management

- Upload Evidence
- Evidence Tracking
- Evidence Status
- Evidence History

---

## DNA Matching

- STR-based DNA Matching
- Match Reports
- Candidate Profiles
- Matching Statistics

---

## Audit Logging

Every important operation is recorded including:

- Login
- Profile Creation
- Evidence Upload
- DNA Matching
- User Activity

---

## Docker Support

The complete application is containerized using Docker.

Services include:

- Spring Boot API
- React Frontend
- PostgreSQL Database

---

# 🏗️ System Architecture

```
                React Frontend
                       │
                       │ REST API
                       ▼
             Spring Boot Backend
                       │
        ┌──────────────┼──────────────┐
        │              │              │
 Authentication   DNA Matching   Evidence Module
        │              │              │
        └──────────────┼──────────────┘
                       │
                 PostgreSQL Database
```

---

# 🛠️ Technology Stack

## Backend

- Java 21
- Spring Boot
- Spring Security
- Spring Data JPA
- JWT Authentication
- Maven

## Frontend

- React
- Axios
- React Router
- CSS

## Database

- PostgreSQL

## DevOps

- Docker
- Docker Compose

---

# 📂 Project Structure

```
privacy-aware-forensic-dna-matching-system

│
├── forensic_dna_spring_backend
│
├── forensic_dna_frontend
│
├── infra
│
├── data
│
├── README.md
│
└── .gitignore
```

---

# 🗄️ Database Tables

The project uses PostgreSQL with the following tables:

- users
- roles
- profiles
- profile_genotypes
- populations
- str_loci
- evidence
- evidence_genotypes
- evidence_matches
- audit_logs
- feedback
- allele_frequencies
- profile_privacy_encodings

---

# 🔐 Authentication Flow

```
User Login

      │

      ▼

Spring Security

      │

      ▼

JWT Token Generation

      │

      ▼

Protected REST APIs
```

---

# 🔬 DNA Matching Workflow

```
Evidence Upload

        │

        ▼

Extract STR Genotypes

        │

        ▼

Compare with DNA Profiles

        │

        ▼

Generate Match Report
```

---

# 🐳 Running with Docker

Clone the repository

```bash
git clone https://github.com/YOUR_USERNAME/privacy-aware-forensic-dna-matching-system.git
```

Go to project folder

```bash
cd privacy-aware-forensic-dna-matching-system
```

Start services

```bash
docker compose up --build
```

Stop services

```bash
docker compose down
```

---

# ▶️ Running Backend

```bash
cd forensic_dna_spring_backend

mvn spring-boot:run
```

---

# ▶️ Running Frontend

```bash
cd forensic_dna_frontend

npm install

npm start
```

---

# 📷 Screenshots

(Add screenshots here)

- Login Page
- Dashboard
- Evidence Management
- DNA Profiles
- DNA Matching
- Reports

---

# Future Enhancements

- AI-assisted DNA Matching
- Face Recognition Integration
- DNA Similarity Visualization
- Notification System
- Email Alerts
- Report Generation (PDF)
- Multi-factor Authentication
- Cloud Deployment

---

# 👨‍💻 Author

**Saqib Ali**

B.Tech Computer Science & Engineering

Java | Spring Boot | React | PostgreSQL | Docker | DevOps

---

# 📜 License

This project is developed for educational and research purposes.
