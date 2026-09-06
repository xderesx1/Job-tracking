# 💼 Job Tracking

![Java](https://img.shields.io/badge/Java-17-orange)
![Spring Boot](https://img.shields.io/badge/Spring%20Boot-4.0.3-brightgreen)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-17-blue)
![Gradle](https://img.shields.io/badge/Gradle-Build%20Tool-02303A)
![Docker](https://img.shields.io/badge/Docker-Compose-2496ED)
![CI](https://github.com/xderesx1/Job-tracking/actions/workflows/ci.yml/badge.svg)

**Job Tracking** — backend-приложение на Java и Spring Boot для хранения пользователей и вакансий и автоматического подбора наиболее подходящих предложений на основе навыков и опыта кандидата.

Проект предоставляет REST API, использует PostgreSQL для хранения данных и содержит unit- и integration-тесты.

---

## 🎯 Цель проекта

Основная задача проекта — автоматизировать первичный подбор вакансий для пользователя.

Пользователь задаёт:

- опыт работы;
- профессиональные навыки.

Для вакансии задаются:

- название;
- компания;
- требуемый опыт;
- необходимые технологии.

Сервис сравнивает данные пользователя с требованиями вакансий, рассчитывает рейтинг каждого предложения и возвращает наиболее релевантные результаты.

---

## 🚀 Возможности

### 👤 Пользователи

- создание пользователей;
- получение пользователя по имени;
- получение списка пользователей;
- хранение опыта работы;
- хранение профессиональных навыков.

### 💼 Вакансии

- создание вакансий;
- получение вакансии по названию;
- получение списка вакансий;
- хранение информации о компании;
- хранение требуемого опыта;
- хранение необходимых технологий.

### 🎯 Рекомендации

Сервис рассчитывает релевантность вакансий для конкретного пользователя.

При расчёте учитываются:

- количество совпавших навыков;
- соответствие опыта пользователя требованиям вакансии.

Если опыта пользователя недостаточно, итоговый рейтинг вакансии уменьшается.

После расчёта вакансии сортируются по рейтингу, и пользователь получает наиболее подходящие предложения.

### 📊 Статистика

Приложение позволяет получать:

- вакансии с заданным требованием к опыту;
- пользователей, подходящих под определённое количество вакансий;
- наиболее популярные навыки пользователей.

### ⏰ Автоматический поиск вакансий

После запуска приложения работает периодическая задача, которая:

1. получает список пользователей;
2. рассчитывает рекомендации;
3. определяет лучшее предложение для каждого пользователя.

---

## 🛠 Tech Stack

| Категория | Технологии |
|---|---|
| Language | Java 17 |
| Framework | Spring Boot 4 |
| Web | Spring Web, REST API |
| Data Access | Spring JDBC |
| Database | PostgreSQL |
| Build Tool | Gradle |
| Testing | JUnit 5, Mockito |
| Integration Testing | Testcontainers |
| Infrastructure | Docker, Docker Compose |
| CI | GitHub Actions |

В проекте также используются:

- Java Collections;
- Stream API;
- `ExecutorService`;
- `ScheduledExecutorService`;
- многослойная архитектура;
- Repository Pattern;
- Service Layer.

---

## 🏗 Архитектура

Проект построен по многослойной архитектуре:

```text
HTTP Client
     │
     ▼
┌─────────────┐
│ Controller  │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│   Service   │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ Repository  │
└──────┬──────┘
       │
       ▼
┌─────────────┐
│ PostgreSQL  │
└─────────────┘
```

### Controller Layer

Отвечает за:

- обработку HTTP-запросов;
- получение входных данных;
- вызов соответствующего сервисного слоя;
- формирование HTTP-ответов.

### Service Layer

Содержит основную бизнес-логику:

- управление пользователями;
- управление вакансиями;
- подбор вакансий;
- расчёт статистики;
- поиск лучших предложений.

### Repository Layer

Отвечает за:

- взаимодействие с PostgreSQL;
- выполнение SQL-запросов;
- получение и сохранение данных.

---

## 🧠 Алгоритм подбора вакансий

Для каждой вакансии рассчитывается рейтинг относительно выбранного пользователя.

Основным фактором является количество совпадений между навыками пользователя и технологиями вакансии:

```text
score = matchingSkills
```

Если опыт пользователя меньше необходимого:

```text
score = score / 2
```

После расчёта вакансии сортируются по рейтингу.

Таким образом, выше располагаются вакансии, которые лучше соответствуют навыкам и опыту пользователя.

---

# 📡 REST API

## Users

### Создать пользователя

```http
POST /api/users
```

Пример запроса:

```json
{
  "name": "Alex",
  "exp": 3,
  "skills": [
    "Java",
    "Spring",
    "PostgreSQL"
  ]
}
```

### Получить всех пользователей

```http
GET /api/users
```

### Получить пользователя

```http
GET /api/users/{name}
```

---

## Jobs

### Создать вакансию

```http
POST /api/jobs
```

Пример запроса:

```json
{
  "title": "Java Developer",
  "company": "T-Bank",
  "exp": 2,
  "tags": [
    "Java",
    "Spring",
    "PostgreSQL"
  ]
}
```

### Получить все вакансии

```http
GET /api/jobs
```

### Получить вакансию

```http
GET /api/jobs/{title}
```

---

## Recommendations

### Получить рекомендации для пользователя

```http
GET /api/suggest/{username}
```

Сервис рассчитывает рейтинг доступных вакансий и возвращает наиболее подходящие предложения.

---

## Statistics

### Вакансии по требуемому опыту

```http
GET /api/stat/exp/{exp}
```

### Пользователи по количеству совпадений

```http
GET /api/stat/match/{count}
```

### Наиболее популярные навыки

```http
GET /api/stat/top-skills/{count}
```

---

# 🗄 PostgreSQL

В качестве основной базы данных используется PostgreSQL.

При запуске приложения структура БД инициализируется с помощью:

```text
src/main/resources/schema.sql
```

Основные данные приложения:

### User

| Поле | Описание |
|---|---|
| `name` | имя пользователя |
| `exp` | опыт работы |
| `skills` | профессиональные навыки |

### Job

| Поле | Описание |
|---|---|
| `title` | название вакансии |
| `company` | компания |
| `exp` | требуемый опыт |
| `tags` | необходимые технологии |

---

# 🐳 Docker Compose

PostgreSQL запускается через Docker Compose.

Конфигурация находится в:

```text
docker-compose.yml
```

Для запуска базы данных:

```bash
docker compose up -d
```

Проверить состояние контейнера:

```bash
docker compose ps
```

Остановить контейнер:

```bash
docker compose down
```

Удалить контейнер вместе с данными PostgreSQL:

```bash
docker compose down -v
```

---

# ▶️ Запуск проекта

## Требования

Для локального запуска необходимы:

- Java 17+;
- Docker;
- Git.

## 1. Клонировать репозиторий

```bash
git clone https://github.com/xderesx1/Job-tracking.git
cd Job-tracking
```

## 2. Запустить PostgreSQL

```bash
docker compose up -d
```

Docker создаст PostgreSQL со следующей конфигурацией:

```text
Database: job_tracking
Host: localhost
Port: 5432
Username: postgres
Password: postgres
```

## 3. Запустить приложение

### macOS / Linux

```bash
./gradlew bootRun
```

### Windows

```bash
gradlew.bat bootRun
```

После запуска REST API доступен по адресу:

```text
http://localhost:8080
```

---

# ⚙️ Конфигурация

Параметры подключения к PostgreSQL можно передавать через environment variables:

```text
DB_URL
DB_USERNAME
DB_PASSWORD
```

По умолчанию приложение использует локальную PostgreSQL из `docker-compose.yml`:

```yaml
spring:
  datasource:
    url: ${DB_URL:jdbc:postgresql://localhost:5432/job_tracking}
    username: ${DB_USERNAME:postgres}
    password: ${DB_PASSWORD:postgres}
```

Благодаря этому для стандартного локального запуска дополнительная настройка не требуется.

---

# 🧪 Тестирование

Проект содержит unit- и integration-тесты.

## Unit Tests

Для тестирования бизнес-логики используются:

- JUnit 5;
- Mockito.

Проверяются сценарии:

- корректного ранжирования вакансий;
- отсутствия вакансий;
- отсутствия пользователя;
- работы с одной вакансией;
- формирования рекомендаций.

Запуск:

```bash
./gradlew test
```

---

## Integration Tests

Интеграционные тесты используют:

- Spring Boot Test;
- Testcontainers;
- PostgreSQL.

Testcontainers автоматически поднимает отдельный PostgreSQL-контейнер во время выполнения тестов.

Интеграционные тесты проверяют:

- repository layer;
- взаимодействие приложения с PostgreSQL;
- выполнение SQL-запросов;
- работу сервиса рекомендаций.

Для запуска integration-тестов Docker должен быть запущен.

---

# 🔄 Continuous Integration

В проекте используется **GitHub Actions**.

Workflow находится в:

```text
.github/workflows/ci.yml
```

При каждом `push` и `pull request` в ветку `main` автоматически:

1. клонируется репозиторий;
2. устанавливается Java;
3. запускается Gradle;
4. выполняются тесты.

Основная команда CI:

```bash
./gradlew test
```

Это позволяет автоматически проверять изменения перед их попаданием в основную ветку.

---

# 📁 Структура проекта

```text
Job-tracking/
│
├── .github/
│   └── workflows/
│       └── ci.yml
│
├── gradle/
│   └── wrapper/
│
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ru/vk/education/job/
│   │   │       ├── controller/
│   │   │       ├── domain/
│   │   │       ├── repository/
│   │   │       ├── service/
│   │   │       └── ...
│   │   │
│   │   └── resources/
│   │       ├── application.yml
│   │       └── schema.sql
│   │
│   └── test/
│
├── .gitignore
├── build.gradle
├── docker-compose.yml
├── gradlew
├── gradlew.bat
├── settings.gradle
└── README.md
```

---

# 🔮 Возможные улучшения

Проект можно развивать дальше:

- добавить Swagger / OpenAPI;
- использовать Flyway для миграций БД;
- добавить централизованную обработку исключений;
- добавить Bean Validation для входных DTO;
- реализовать пагинацию;
- расширить фильтрацию вакансий;
- добавить логирование;
- добавить кэширование рекомендаций;
- контейнеризировать Spring Boot приложение.

---

# 👨‍💻 Author

**xderesx1**

GitHub: https://github.com/xderesx1