# AI Career Copilot

> **AI-Powered Career Guidance and Job Recommendation System**

AI Career Copilot helps students, graduates, and job seekers discover relevant employment opportunities and understand how their skills align with job requirements. It combines full-stack web development with AI-assisted job analysis.

**Repository:** https://github.com/Mustafeez24/ai-career-copilot  
**Live demo:** https://jobradar-ai-gold.vercel.app (the current Vercel deployment still uses the previous project URL; it will be updated after the Vercel project is renamed).

## Project Objectives

- Provide a user-friendly career guidance interface.
- Help users explore job opportunities relevant to their skills and interests.
- Compare candidate skills with job requirements.
- Identify matching skills and skill gaps.
- Support career preparation through interview questions and action plans.
- Demonstrate frontend, backend, database, API, and authentication concepts.

## Features

- Search live job listings using SerpApi Google Jobs.
- Enter role, location, experience, and skills.
- Analyse a selected job using NVIDIA Nemotron.
- Generate a match score and recommendation.
- Identify matching skills and missing skills.
- Generate interview questions and a preparation plan.
- Register and log in using JWT authentication through the Spring Boot API.
- Store user account information in PostgreSQL.
- Responsive user interface.

## Technology Stack

| Layer | Technology | Purpose |
|---|---|---|
| Frontend | React.js, Next.js, TypeScript | User interface |
| Backend | Java 21, Spring Boot | Application logic and REST APIs |
| Database | PostgreSQL | Store user account information |
| API communication | REST APIs | Connect frontend and backend |
| Authentication | Spring Security, JWT | User registration, login, and protected profile |
| Job data | SerpApi Google Jobs | Retrieve job listings |
| AI analysis | NVIDIA Nemotron | Job matching and career assistance |
| Styling | Tailwind CSS | Responsive interface |

## System Architecture

When the Spring Boot backend is configured, the application follows this flow:

```text
User
 |
 v
React.js / Next.js Frontend
 |
 | REST API requests
 v
Java + Spring Boot Backend
 |             |
 |             +---- JWT Authentication
 |
 +---- PostgreSQL Database
 |
 +---- SerpApi Google Jobs
 |
 +---- NVIDIA Nemotron AI
 |
 v
Job Results and Career Guidance
```

The Next.js API routes proxy job-search and AI-analysis requests to Spring Boot when `SPRING_BOOT_API_URL` is configured. Without that setting, the existing Next.js API integrations remain available as a fallback for the current Vercel deployment.

## Repository Structure

```text
app/                  Next.js / React frontend and API proxy routes
backend/              Java Spring Boot REST API
  src/main/java/      Controllers, services, entities, security
  src/main/resources/ application configuration
docker-compose.yml    PostgreSQL + Spring Boot local environment
.env.example          Environment variable template
```

## Run Locally

Clone the updated repository:

```bash
git clone https://github.com/Mustafeez24/ai-career-copilot.git
cd ai-career-copilot
```

### 1. Start PostgreSQL and Spring Boot

Requirements: Docker Desktop.

Copy the environment template:

```bash
# Windows PowerShell
Copy-Item .env.example .env
```

Add your SerpApi and NVIDIA API keys to `.env`, then run:

```bash
docker compose up --build
```

The backend will be available at http://localhost:8080.

### 2. Start the React / Next.js frontend

In a second terminal:

```bash
npm install
npm run dev
```

Create a local `.env.local` file containing:

```env
SPRING_BOOT_API_URL=http://localhost:8080
```

Open http://localhost:3000.

### 3. Build checks

Frontend:

```bash
npm run build
```

Backend (from the `backend` directory):

```bash
mvn test
mvn package
```

## REST API Endpoints

| Method | Endpoint | Purpose |
|---|---|---|
| POST | `/api/auth/register` | Register a user |
| POST | `/api/auth/login` | Log in and receive a JWT |
| GET/PUT | `/api/profile/me` | Read or update the authenticated career profile |
| GET | `/api/jobs?q=Software%20Engineer` | Search jobs |
| POST | `/api/analyze` | Analyse a candidate and job |
| GET | `/actuator/health` | Check backend availability |

The Next.js frontend also exposes proxy routes under `/api/auth` and `/api/profile/me` when the Spring Boot backend URL is configured.

## Environment Variables

Backend / Docker:

- `SERPAPI_KEY`
- `NVIDIA_API_KEY`
- `JWT_SECRET` — use a unique Base64-encoded secret outside local development.
- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `FRONTEND_ORIGIN`

Next.js frontend:

- `SPRING_BOOT_API_URL` — optional; when set, job search and AI analysis are sent to the Spring Boot API.

Never commit `.env` or `.env.local` files or expose API keys in browser code.

## Academic Project Context

The project is titled **AI Career Copilot – An AI-Powered Career Guidance and Job Recommendation System**. The BCA internship report describes React.js, Java, Spring Boot, PostgreSQL, REST APIs, and JWT authentication. This repository includes a Spring Boot backend foundation for those components, alongside the existing Next.js job search and AI analysis functionality.

## Future Scope

- Resume analysis and advanced skill assessment.
- Personalised learning recommendations.
- Saved jobs and application tracking.
- More job sources and career resources.
- Further automated testing and production deployment.

---

**Internship Organisation:** YouByte Labs  
**Internship Role:** Full Stack Developer Intern  
**Project:** AI Career Copilot
