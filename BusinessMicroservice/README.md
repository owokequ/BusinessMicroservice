# Business Microservice

Этот проект представляет собой RESTful-сервис на Spring Boot для управления бизнес-сущностями, такими как пользователи, кошельки и транзакции.

## Технологический стек

- **Java 17**
- **Spring Boot 4.0.5**
  - Spring Web
  - Spring Data JPA
  - Spring Validation
  - Spring Liquibase
- **PostgreSQL** — реляционная база данных
- **Lombok** — для уменьшения boilerplate кода
- **MapStruct** — для маппинга DTO и сущностей
- **Liquibase** — управление миграциями базы данных

## Структура проекта

```
src/main/java/ru/artem/business/app/buisness_rest_service
├── controller       — REST-контроллеры
├── dto              — Data Transfer Objects
├── entity           — JPA-сущности
├── enums            — Перечисления
├── exception        — Обработка исключений
├── repository       — Spring Data JPA репозитории
├── service          — Бизнес-логика
└── BuisnessRestServiceApplication.java — Точка входа приложения
```

## Конфигурация

Порт: `8083`

База данных:
- URL: `jdbc:postgresql://localhost:5434/business_db`
- Пользователь: `artem`
- Пароль: `12345678`

Миграции базы данных управляются через Liquibase. Главный файл миграции: `db/changelog/db.changelog-master.yaml`.

## Запуск проекта

1. Убедитесь, что установлены:
   - JDK 17
   - Maven
   - PostgreSQL (или Docker)

2. Запустите PostgreSQL (например, через Docker):
   ```bash
   docker-compose up -d
   ```

3. Соберите и запустите приложение:
   ```bash
   ./mvnw spring-boot:run
   ```

   Или:
   ```bash
   ./mvnw clean install
   java -jar target/business-rest-service-0.0.1.jar
   ```

4. Приложение будет доступно по адресу: `http://localhost:8083`

## Docker

Проект включает `docker-compose.yml` для запуска PostgreSQL.
