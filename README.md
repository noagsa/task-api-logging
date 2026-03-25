# 📋 Task API Logging

REST API developed with Spring Boot for managing tasks. This project focuses on
structured JSON logging with SLF4J and Logback, global exception handling with
meaningful HTTP error responses, request validation and a complete testing suite
with unit and integration tests.

## 🛠️ Tech Stack
Java 21 · Spring Boot 3.5.12 · Maven · Docker · JUnit 5 · Mockito

## 🚀 Getting Started

### Requirements
- Java 21
- Docker
- Make

### Run the project

1. Clone the repository
```bash
git clone https://github.com/noagsa/task-api-logging
cd task-api-logging
```

2. Run locally
```bash
make run
```

3. Or run with Docker
```bash
make docker-build
make docker-run
```

4. Run tests
```bash
make test
```

## ⚙️ Configuration
No external configuration required. Data is stored in memory.

## ⚠️ Common Issues

**Port 8080 already in use**
```bash
# Windows
netstat -ano | findstr :8080

# Linux/WSL
lsof -i :8080
```

**Make not found**

Install Make depending on your OS:

**Windows (Chocolatey)**
```bash
choco install make
```

**Windows (WSL)**
```bash
sudo apt install make
```

**Docker not running**
Make sure Docker Desktop is running before executing `make docker-build` or `make docker-run`.

## 📡 Endpoints

| Method | Endpoint | Description |
|--------|----------|-------------|
| POST | /api/tasks | Create a task |
| GET | /api/tasks | Get all tasks |
| GET | /api/tasks/{id} | Get task by id |
| PUT | /api/tasks/{id} | Update a task |
| DELETE | /api/tasks/{id} | Delete a task |

### Request body (POST / PUT)
```json
{
  "title": "Task title",
  "description": "Task description",
  "isDone": false,
  "dueDate": "2026-03-25"
}
```

## 🧪 Testing

- **Unit tests**: TaskService and TaskController
- **Integration tests**: Full request flow through TaskController
```bash
make test
```
