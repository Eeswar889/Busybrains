# BusyBrains Full Stack Assignment

Reference scaffold for the BusyBrains React + Spring Boot assignment.

## Structure

- `backend/` Spring Boot API
- `frontend/` React client

## Implemented Features

- JWT login and registration
- Google OAuth2 login flow hook
- Role-based access with `ROLE_ADMIN` and `ROLE_USER`
- Pre-seeded users:
  - `admin / password`
  - `user / password`
- Product listing for all users
- Product create, update, delete for admins
- Profile view and update
- Change password flow

## Backend Run

1. Install Java 17 and Maven.
2. Update MySQL settings in `backend/src/main/resources/application.properties` if needed.
3. Set Google OAuth client ID and secret in the same file.
4. Start:

```bash
cd backend
mvn spring-boot:run
```

Backend URL: `http://localhost:8080`

## Frontend Run

1. Install Node.js 18+.
2. Start:

```bash
cd frontend
npm install
npm start
```

Frontend URL: `http://localhost:3000`

## API Summary

- `POST /api/auth/register`
- `POST /api/auth/login`
- `GET /api/products`
- `POST /api/products`
- `PUT /api/products/{id}`
- `DELETE /api/products/{id}`
- `GET /api/profile`
- `PUT /api/profile`
- `PUT /api/profile/change-password`

## Notes

- OAuth success redirects to `http://localhost:3000/oauth-success?token=...`.
- Tooling is not installed in this workspace, so the project was scaffolded without running Maven or npm verification locally.
