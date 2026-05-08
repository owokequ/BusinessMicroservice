# Auth Service

![Java](https://img.shields.io/badge/Java-17-green)
![Spring Boot](https://img.shields.io/badge/Spring_Boot-4.0.6-red)
![PostgreSQL](https://img.shields.io/badge/PostgreSQL-15-blue)

## Описание

Auth Service - это микросервис аутентификации, реализованный с использованием современных технологий Spring Boot. Сервис предоставляет базовые функции регистрации и аутентификации пользователей с использованием JWT токенов.

## Особенности

- 🚀 Современный Spring Boot 4.0.6
- 🔐 Аутентификация с JWT
- 🗄️ Хранение данных в PostgreSQL
- 🔄 Управление миграциями через Liquibase
- 🐳 Контейнеризация с Docker
- 📦 Современные Java
- 🛠️ Полностью настроенная среда разработки

## Архитектура

```
auth-service/
├── src/
│   ├── main/
│   │   ├── java/
│   │   │   └── ru.artem.auth_service/
│   │   │       ├── controller/
│   │   │       ├── service/
│   │   │       ├── entity/
│   │   │       ├── dto/
│   │   │       │   ├── request/
│   │   │       │   └── response/
│   │   │       └── config/
│   │   └── resources/
│   │       ├── db/
│   │       │   └── changelog/
│   │       └── application.yml
├── docker-compose.yml
└── README.md
```

## Запуск проекта

### Через Docker

```bash
# Запуск базы данных и adminer
docker-compose up -d

# Сборка и запуск приложения
./mvnw spring-boot:run
```

### Локальный запуск

```bash
# Установка зависимостей
./mvnw clean install

# Запуск приложения
./mvnw spring-boot:run
```

## Endpoints

### Пользователи

- `GET /api/users/home` - Главная страница
- `GET /api/users/health` - Проверка работоспособности
- `POST /api/users/register` - Регистрация нового пользователя
- `GET /api/users` - Получение всех пользователей (заглушка)
- `GET /api/users/{username}` - Получение пользователя по имени (заглушка)

## Настройка окружения

Создайте файл `.env` в корне проекта:

```env
POSTGRES_DB=auth_db
POSTGRES_USER=postgres
POSTGRES_PASSWORD=postgres
```