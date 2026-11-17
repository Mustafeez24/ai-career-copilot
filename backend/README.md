# AI Career Copilot – Spring Boot Backend

This backend implements the architecture described in the BCA internship report.

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

```bash
# Set the integration keys in your shell or .env file first.
docker compose up --build
```

The API runs at http://localhost:8080. PostgreSQL runs on port 5432.

## Run without Docker

Create a PostgreSQL database named `career_copilot`, then configure these environment variables:

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

## Notes

- Hibernate creates/updates the `app_users` table from the JPA entity.
- Keep API keys and JWT secrets out of source control.
- The current Next.js API routes can proxy to this backend when `SPRING_BOOT_API_URL` is set. Without it, the original Next.js integration remains available for the existing Vercel deployment.
- Run `mvn test` or `mvn package` before deploying to validate the backend in your local Java environment.
