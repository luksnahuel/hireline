# hireline

## Stack
- Kotlin on JDK 25 (Temurin), Gradle with Kotlin DSL and the wrapper
- Javalin (HTTP), Flyway (migrations), PostgreSQL 18 + pgvector
- JUnit + Testcontainers for tests
- Versions: `gradle/libs.versions.toml`, `.sdkmanrc`, `compose.yaml`, `.github/workflows/ci.yml`

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
- Run: `DB_URL=jdbc:postgresql://localhost:5432/hireline DB_USER=hireline DB_PASSWORD=hireline HTTP_PORT=7070 ./gradlew run`
- Health: `curl localhost:7070/health`
