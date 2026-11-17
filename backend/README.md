# AI Career Copilot – Spring Boot Backend

This backend implements the Java, Spring Boot, PostgreSQL, REST API and JWT parts of the BCA internship report's architecture.

## Stack

- Java 21
- Spring Boot 3.5
- Spring Web (REST APIs)
- Spring Data JPA
- Spring Security + JWT
- PostgreSQL
- SerpApi Google Jobs
- NVIDIA Nemotron

## Start with Docker

From the repository root:

1. Copy `.env.example` to `.env`.
2. Add your SerpApi and NVIDIA API keys.
3. Change the local database password if required.
4. Run:

```bash
docker compose up --build
```

The API runs at http://localhost:8080. PostgreSQL runs on port 5432.

## Run without Docker

Create a PostgreSQL database named `career_copilot`, then configure:

- `SPRING_DATASOURCE_URL`
- `SPRING_DATASOURCE_USERNAME`
- `SPRING_DATASOURCE_PASSWORD`
- `JWT_SECRET` (Base64-encoded, at least 32 bytes after decoding)
- `SERPAPI_KEY`
- `NVIDIA_API_KEY`

Run:

```bash
mvn spring-boot:run
```

## REST API

| Method | Endpoint | Purpose | Access |
|---|---|---|---|
| POST | `/api/auth/register` | Create an account | Public |
| POST | `/api/auth/login` | Login and receive a JWT | Public |
| GET | `/api/profile/me` | Get current signed-in user | JWT required |
| GET | `/api/jobs?q=...` | Search jobs using SerpApi | Public |
| POST | `/api/analyze` | Analyse candidate/job fit with Nemotron | Public |
| GET | `/actuator/health` | Health check | Public |

### Register

```http
POST /api/auth/register
Content-Type: application/json

{
  "fullName": "Example User",
  "email": "user@example.com",
  "password": "change-this-password"
}
```

The response includes a signed bearer token. Passwords are stored as BCrypt hashes.

### Login

```http
POST /api/auth/login
Content-Type: application/json

{
  "email": "user@example.com",
  "password": "change-this-password"
}
```

Send the returned token as `Authorization: Bearer <token>` to access protected endpoints.

## Frontend Connection

Set `SPRING_BOOT_API_URL=http://localhost:8080` in the Next.js `.env.local` file. The existing `/api/jobs` and `/api/analyze` routes will forward requests to this backend. The auth and profile proxy routes are also available under `/api/auth` and `/api/profile/me`.

When `SPRING_BOOT_API_URL` is not set, job search and AI analysis continue using the original Next.js integrations. This keeps the current Vercel deployment usable until the Spring Boot service is deployed.

## Notes

- Hibernate creates/updates the `app_users` table from the JPA entity.
- Keep API keys and JWT secrets out of source control.
- The default database credentials in Docker Compose are for local development only.
- Set a unique strong `JWT_SECRET` before deploying outside local development.
- Run `mvn test` and `mvn package` before using this backend in production.
