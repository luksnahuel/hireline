# Workflow

How a coding session works in this repo: where it must stop for the developer, and when its work is done.

## Gates
A gate is a point where the session stops and waits for the developer's explicit yes.

Every gate follows one rule: **a one-way door is the developer's; a two-way door is the session's.** A one-way door is a change that a revert can't undo, or can undo only at a high cost: an applied migration, data sent outside the machine or deleted, a public name that other code starts to use, a choice that forces reprocessing all stored data. A session that meets a one-way door the list below doesn't name stops anyway. A two-way door it decides itself, then reports in its summary.

1. **Plan before code.** Each feature starts with a conversation with the developer: what it does and how it fits. Most features need nothing more. When the developer provides a design doc for an important one, follow it.
2. **Spec before each behavior.** Each new behavior starts as a Gherkin spec (Given / When / Then), approved by the developer before any code exists.
3. **Stop and ask** before anything the agreed plan and the approved spec don't already cover:
   - a table, column or index;
   - a new library;
   - a new public name for a domain concept;
   - sending a new kind of data to an external service, such as adding profile or resume fields to an LLM prompt.

Never add, install or change a skill or plugin. That's a ban, not a gate.

Between gates the session doesn't check in: once the spec is approved, it builds the behavior and decides how to split the work itself.

## Definition of done
A session says a behavior is done only when each check holds, and each one is shown by a command's output in the session, not claimed:
1. `./gradlew test` passes, and its output lists every scenario of the approved spec as a passing end-to-end test.
2. The linter passes.
3. `git diff` shows no existing test weakened: no changed assertion, no removed or skipped test.
4. No special case in the code exists only to make a test pass. No command shows this one, so review checks it.

Then it stops at ready to commit:
- It reports the decisions it made, not just the actions, and what it skipped or didn't check.
- It doesn't start the next behavior.
- Committing and pushing are the developer's call. The behavior is closed when CI is green on the pushed commit.

Open: production and impact checks, once there is a deploy.

## How this harness grows
- A rule is added when a session gets something wrong: the mistake is the evidence. Write the rule as it stands, plus only the why needed to apply it; the story of how it came about goes in the commit message.
- A rule a tool can check goes into the build (formatter, linter, architecture test), not into these docs.