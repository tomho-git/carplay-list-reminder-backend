# Java + Maven Dockerfile template

Run the following commands to create the Docker image and run the container.

```sh
docker-compose up --build
```

## Environment Variables

Create a `.env` file in the root directory with the following content, replacing the placeholders with your actual values:

```
DB_URL=jdbc:postgresql://db:5432/postgres
DB_USER=your_db_user
DB_PASSWORD=your_db_password
```

## Docker Compose

The `docker-compose.yml` file is used to define and run multi-container Docker applications. It includes the configuration for the application and the database.

```yaml
services:
  app:
    build: .
    ports:
      - "8080:8080"
    environment:
      - DB_URL=${DB_URL}
      - DB_USER=${DB_USER}
      - DB_PASSWORD=${DB_PASSWORD}
    depends_on:
      - db

  db:
    image: postgres
    environment:
      POSTGRES_DB: postgres
      POSTGRES_USER: ${DB_USER}
      POSTGRES_PASSWORD: ${DB_PASSWORD}
    ports:
      - "5432:5432"
```