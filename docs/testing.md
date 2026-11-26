# Testing

## What a test targets
A test targets a unit of behavior, not a unit of code such as a class or a function. A refactor that keeps the behavior doesn't break a test. Tests are code too: each one adds coupling, so each one earns its place.

## Levels
- **End-to-end.** Every behavior a user can trigger through an external port has an end-to-end test, with only the edges faked. In this repo, that means real HTTP and a real Postgres (Testcontainers), with the LLM faked. Each scenario of an approved spec is one end-to-end test.
- **Sociable domain tests.** A domain class with rules of its own gets tests that run it with its real collaborators, faking only the edges.
- **Repositories.** No separate tests for now: the end-to-end tests cover them through a real Postgres. A query with logic the end-to-end tests don't reach gets its own test.

Open: other levels and the conventions for naming and structuring tests, added as they are decided.
