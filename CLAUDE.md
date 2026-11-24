# hireline

## Stack
- Kotlin on JDK 25 (Temurin), Gradle with Kotlin DSL and the wrapper
- Javalin (HTTP), Flyway (migrations), PostgreSQL 18 + pgvector
- JUnit + Testcontainers for tests
- Versions: `gradle/libs.versions.toml`, `.sdkmanrc`, `compose.yaml`, `.github/workflows/ci.yml`
- JDK version: change `.sdkmanrc` and `.github/workflows/ci.yml` together
- pgvector image tag: change `gradle/libs.versions.toml` and `compose.yaml` together

## Package layout (`src/main/kotlin/hireline`)
- `Main.kt`: `main()` and `start()`, wires everything together
- `core/domain`, `core/app`, `core/infrastructure`: domain, use cases, adapters
- `platform`: config, database, migrations. Never imports `core` or `webapi`
- `webapi`: Javalin app and routes
- Migrations: `src/main/resources/db/migration`

## Commands
- Build: `./gradlew build`
- Test: `./gradlew test` (needs Docker; on Colima, export `DOCKER_HOST=unix://$HOME/.colima/default/docker.sock` and `TESTCONTAINERS_DOCKER_SOCKET_OVERRIDE=/var/run/docker.sock`)
- Local Postgres: `docker compose up -d --wait`
- Local env: `cp .env.example .env` (`.env` is gitignored; only `.env.example` is committed)
- Run: `./gradlew run` (the app reads `.env` via dotenv-java; variables already set in the shell win)
- Health: `curl localhost:7070/health`
