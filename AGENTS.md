# Repository Guidelines

## Project Structure & Module Organization

This is a Java 21 Spring Boot Maven project. Application code lives under `src/main/java/com/jvictornascimento/leadCompass`, with `LeadCompassApplication` as the entry point. Runtime configuration is in `src/main/resources/application.yaml`. Tests mirror the main package under `src/test/java/com/jvictornascimento/leadCompass`. Use `src/main/resources/db/migration` for Flyway migrations when database schema changes are introduced, following Flyway naming such as `V1__create_leads_table.sql`. Product roadmap context is documented in `docs/backend-context.md`; agile/TDD implementation stories are in `docs/agile-stories.md`.

## Build, Test, and Development Commands

Use the Maven wrapper so contributors run the same Maven version:

- `./mvnw spring-boot:run` starts the application locally.
- `./mvnw test` runs the JUnit/Spring Boot test suite.
- `./mvnw package` compiles, tests, and builds the application artifact in `target/`.
- `./mvnw clean` removes generated build output.

The project includes PostgreSQL, JPA, Flyway, Quartz, Spring Security, Web MVC, OpenFeign, Lombok, and MapStruct dependencies. Ensure local configuration is supplied before running features that require a database or external services.

## Coding Style & Naming Conventions

Follow standard Java conventions with 4-space indentation in new files. Keep packages under `com.jvictornascimento.leadCompass`. Use `PascalCase` for classes, `camelCase` for methods and fields, and descriptive suffixes such as `Controller`, `Service`, `Repository`, `Entity`, `Dto`, and `Mapper`. Prefer constructor injection for Spring beans. Keep configuration in YAML and avoid committing secrets.

Lombok and MapStruct are enabled through Maven annotation processing. Use them where they reduce boilerplate, but keep generated behavior obvious from the source.

## Testing Guidelines

Tests use JUnit 5 with Spring Boot test support. Name test classes after the unit or feature under test, for example `LeadServiceTest` or `LeadControllerTest`. Use focused unit tests for business logic and Spring integration tests only when framework wiring, persistence, security, or request handling matters. Run `./mvnw test` before submitting changes.

## Commit & Pull Request Guidelines

Git history is not available in this checkout, so use clear, conventional commit messages such as `feat: add lead import endpoint`, `fix: validate lead email`, or `test: cover lead mapping`. Pull requests should include a short summary, testing performed, linked issue when applicable, and screenshots or sample requests for API-facing behavior changes.

## Security & Configuration Tips

Do not commit credentials, database URLs with passwords, API keys, or local-only overrides. Prefer environment variables or profile-specific configuration ignored by Git. Review Spring Security changes carefully and add tests for authentication or authorization behavior.
