# M0 design: walking skeleton

Written 2026-11-21 to 2026-11-23. Under each heading, my latest answer, verbatim. Lines marked **Open** are still to decide.

## What's stored
> pgvector could be paid at milestone 0 or 2 I don't see the difference but for the walking skeleton being as healthy and as representative that it can I would add it at 0. Because it is just an extension, right?

**Open:** the exact Postgres + pgvector image tag, and how to check it really ships pgvector.

## What can fail at startup

### Postgres isn't ready
> I guess it can start and retry a number of times to boot Postgres up. Meanwhile a health check should be "negative" and after some attempts try to restart or just exit.

> I guess two at max with a wait of a couple of minutes less than 5 from each try. Since this is starting time is not really a constraint a clean startup of the service could take up to 5 to 10 minutes easily.

> That order should be the one. Migrations should be the most critical step in order to be live because the data is the most important part in a system. So if we can't ensure migrations are up to date we can't serve the HTTP server and start receiving requests.

**Open:** measure how long Postgres takes to accept connections, then size the retries to it.

### A migration fails
> If this runs with a transcation it shouldn't the migration engine should take care of that for sure.

> That some parts of the migration were applied and some didn't. A manual rollback should be executed.

> A migration that fails most of the time won't be fixed by retrying but rather checking the integrity of the DB and the data so at the first fail the app shouldn't start up.

## /health
> 503 is temporary. No deploy yeah but a health check step shouldn't be a big deal. A test never calls /health makes no sense.

> I guess not. Or well not for this iteration or this stage of what we are trying to build. But I guess defining what a health check should consider is something we must do.

## How I'll know it works
> Is a health status check a good test? We are doing a walking skeleton in M0 the test itself shouldn't matter a lot but I guess we can do a /health test. Postgres up = 200 Postgres down = 503. Postgres stopped I don't know. To know it's alive I guess we can do a readiness prove like "SELECT 1;"

> It should send a real http request for a test server that startups for the e2e test.

> It should be orchestrated by the test but mainly the app configuration and how is started.

## Migrations (Flyway)
> Concurrency and versioning.

> add a migration for that alter table change.
