# M0 design: walking skeleton

## Goal
Prove the wiring end to end (HTTP → app → Postgres) before building any feature. M0 has no LLM call and no domain tables.

## Scope
- A Kotlin + Javalin app with one endpoint: `GET /health`.
- Postgres with the pgvector extension, running in Docker for local development. pgvector is enabled from M0, not M2: it's only an extension, and it keeps the skeleton representative.
- Flyway runs the migrations when the app starts.
- CI runs the test suite on every push.
- One instance. No load balancer, no deploy.

## What's stored
- No domain data yet.
- `flyway_schema_history`, Flyway's own table, which records the migrations applied to this database.

## Startup sequence
1. **Read config.** The database URL comes from config and is never hardcoded, because the tests pass their own.
2. **Connect to Postgres.** If it isn't ready, retry: that error is transient. Two attempts, a few minutes apart, then exit with an error.
3. **Run migrations.** On any failure, exit and don't retry: that error is permanent, and retrying won't fix it.
4. **Start the HTTP server.** It starts only after migrations succeed, so a running app implies an up-to-date schema.

## What can fail at startup
| Failure | What happens |
|---|---|
| Postgres not ready | Retry (step 2), then exit with an error |
| A migration fails | Each migration runs in a transaction. Postgres rolls back schema changes, so nothing is half-applied, and there's no row for it in `flyway_schema_history` either. The app exits, I fix the migration and restart |
| A migration that can't run in a transaction fails (e.g. `CREATE INDEX CONCURRENTLY`) | It may be half-applied: manual rollback, then `flyway repair` to clear the failed entry |
| No database in CI | The tests start their own (see below) |

## /health
- Runs `SELECT 1` against Postgres.
- `200` if it succeeds. `503 Service Unavailable` if Postgres is unreachable: the condition is temporary, not a bug (`500`).
- Doesn't call the LLM provider.
- Read in M0 by the end-to-end test in CI, and by me.
- With several instances behind a load balancer, the Postgres check would become a *readiness* check (stop sending traffic), separate from a *liveness* check (restart the process). Not needed for one instance.

## Migrations
- Each schema change is its own numbered file, applied once and in order. The history table records which ones each database already has, so every database (my laptop, CI's fresh one) reaches the same state by the same steps.
- Flyway stores a checksum per migration, so an edited migration is detected. Never edit a migration that has already run; add a new one (e.g. a column rename is a new migration).
- Flyway takes a lock in Postgres, so two processes starting at once don't both migrate.

## How I'll know it works
- **End-to-end test:** it starts the real app and sends real HTTP requests. Postgres comes from a Docker container that the test starts and removes (Testcontainers), so no data leaks between runs. The test passes that container's URL to the app through config.
  - Postgres up: `GET /health` → `200`.
  - Postgres down (the test stops the container): `GET /health` → `503`.
- **Demo checks:**
  - (a) A migration that creates a table and then fails. Done: afterwards the table wasn't there, and `flyway_schema_history` had no V2 row.
  - (b) Edit a migration that already ran, restart, and see what Flyway does. Skipped: the checksum check stops migrate and the app exits, so there's nothing else to see.
- CI runs the test on every push.

## Open
- The exact Postgres + pgvector image tag, and how to check that it ships pgvector.
- How long Postgres takes to accept connections. Size the retries in step 2 to it.
- What the app does if its HTTP port is already taken.
