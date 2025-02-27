# Buggy Bug Demo App

## 📌 Description
**Buggy Bug Demo App** is a demonstration application showcasing a Domain-Driven Design (DDD) approach using Spring Boot and PostgreSQL. It includes user and meal management features, as well as integration with an external API.

## 🚀 Technologies
- Java 17
- Spring Boot 3
- Spring Data JPA
- PostgreSQL
- OpenAPI (Swagger)
- Docker & Docker Compose

## 📂 Project Structure
```

app/
├── application/    # Application logic (services)
├── domain/         # Domain model (entities, aggregates, repositories)
├── infrastructure/ # Data access and integrations (JPA, API clients)
└── controllers/    # REST API controllers

test/
├── application/    # Unit tests
└── controlllers/   # Integration tests with mock external API
```

## 🛠 Setup & Running
### 1. Running Locally
#### Requirements:
- Docker & Docker Compose
- Java 17
- Maven

#### Steps:
```sh
git clone https://github.com/piotrheliosz/buggy-bug-demo-app.git
cd buggy-bug-demo-app
docker-compose up -d db
mvn spring-boot:run
```
The application will be available at `http://localhost:8080`

### 2. API Documentation
Once running, the documentation is available at: `http://localhost:8080/swagger-ui.html`

## 🧪 Testing
To run tests:
```sh
docker-compose up -d db wiremock
mvn test
```

## 🔧 TODO / Potential Improvements
- Error handling in API
- Query caching

## 📜 License
This is a demonstration project – feel free to use, modify, and extend it as needed!
