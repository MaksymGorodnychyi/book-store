# book-store
Online book store REST API built with Spring Boot

## Running with Docker

### Prerequisites
- [Docker Desktop](https://www.docker.com/products/docker-desktop/)

### 1. Configure environment variables
Copy the template and fill in your own values:

```bash
cp .env.template .env
```

| Variable | Description | Example |
|---|---|---|
| `MYSQLDB_USER` | Database user. Must be `root`, because the password below is set for the root user | `root` |
| `MYSQLDB_ROOT_PASSWORD` | Password for the MySQL root user | your own password |
| `MYSQLDB_DATABASE` | Database name | `book_store` |
| `MYSQLDB_LOCAL_PORT` | MySQL port on your machine. Use a free port (not `3306` if you already have MySQL installed locally) | `3307` |
| `MYSQLDB_DOCKER_PORT` | MySQL port inside the container | `3306` |
| `SPRING_LOCAL_PORT` | Application port on your machine | `8088` |
| `SPRING_DOCKER_PORT` | Application port inside the container | `8080` |
| `JWT_SIGNING_KEY` | Secret key for signing JWT tokens, at least 32 characters | see below |

Generate a secure JWT key:

```powershell
# Windows (PowerShell)
[Convert]::ToBase64String((1..48 | ForEach-Object { Get-Random -Maximum 256 }))
```

```bash
# macOS / Linux
openssl rand -base64 48
```

> **Never commit the `.env` file.** It is listed in `.gitignore`.

### 2. Start the application

```bash
docker compose up --build
```

The first start takes a few minutes: Docker downloads the images and MySQL initializes the database. Liquibase creates the tables automatically.

### 3. Open the API
Swagger UI: `http://localhost:<SPRING_LOCAL_PORT>/api/swagger-ui/index.html`

### 4. Stop the application

```bash
docker compose down
```
