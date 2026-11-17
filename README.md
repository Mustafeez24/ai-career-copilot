# AI Career Copilot

> An AI-powered career guidance and job recommendation system.

AI Career Copilot helps students, graduates, and job seekers explore relevant employment opportunities and understand how their skills align with job requirements. It combines job discovery with AI-assisted analysis to provide actionable career insights.

## Project Overview

Job seekers often spend time searching across multiple platforms, comparing job descriptions, identifying missing skills, and preparing for interviews. AI Career Copilot brings job discovery and career assistance into one workflow.

## Current Features

- Search live job listings using SerpApi Google Jobs.
- Enter a target role, location, experience level, and skills.
- Analyse a selected job with NVIDIA Nemotron.
- Generate a match score and recommendation.
- Identify matching skills and skill gaps.
- Generate interview questions and a preparation plan.
- Responsive user interface with loading and error states.

## Current Implementation Architecture

The repository currently implements the following architecture:

```text
React / Next.js Frontend
        |
        +---- /api/jobs ----> SerpApi Google Jobs
        |
        +---- /api/analyze -> NVIDIA Nemotron
```

## Technology Stack (Actual Repository)

| Layer | Technology | Responsibility |
|---|---|---|
| Frontend | React 19, Next.js 16, TypeScript | User interface and interactions |
| Styling | Tailwind CSS 4 | Responsive UI |
| Animation | Motion | Interface animations |
| Job discovery | SerpApi Google Jobs API | Retrieves job listings |
| AI analysis | NVIDIA Nemotron API | Job-fit analysis and career guidance |
| Runtime | Node.js | Application runtime |

> **Architecture note:** The accompanying BCA internship report describes a proposed full-stack architecture using React.js, Java, Spring Boot, PostgreSQL, REST APIs, and JWT authentication. This repository's current implementation uses Next.js API routes, SerpApi, and NVIDIA Nemotron. Java/Spring Boot, PostgreSQL, and JWT should be described as planned or studied components unless and until they are implemented and verified in this codebase.

## How It Works

1. The user enters a job role, location, experience, and skills.
2. The application searches Google Jobs through SerpApi.
3. The user selects a job for analysis.
4. The server sends the profile and job information to NVIDIA Nemotron.
5. The application displays the match score, recommendation, matching skills, missing skills, interview questions, and preparation plan.

## Run Locally

Requirements: Node.js and npm.

```bash
git clone https://github.com/Mustafeez24/jobradar-ai.git
cd jobradar-ai
npm install
```

Create `.env.local` in the project root:

```env
SERPAPI_KEY=your_serpapi_key
NVIDIA_API_KEY=your_nvidia_api_key
```

Start the development server:

```bash
npm run dev
```

Open http://localhost:3000.

## Build

```bash
npm run build
```

## Security

- Keep API keys in server-side environment variables.
- Never commit `.env.local`.
- Do not expose provider keys in browser code.

## Academic Context

This project is related to the BCA internship topic **AI Career Copilot – An AI-Powered Career Guidance and Job Recommendation System**. The internship report discusses requirement analysis, system design, frontend and backend concepts, database management, API integration, authentication, testing, and future enhancements.

## Future Scope

Potential future enhancements described for the project include:

- User profile management and persistence.
- Resume analysis and skill assessment.
- Advanced personalised job recommendations.
- Skill-gap analysis and learning recommendations.
- Interview preparation assistance.
- A dedicated Java/Spring Boot backend, PostgreSQL persistence, and JWT authentication, if implemented in a later development phase.

---

**Project:** AI Career Copilot  
**Repository:** Mustafeez24/jobradar-ai
