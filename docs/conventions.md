# Conventions

What the code looks like, and why.

The goal: code that is cheap to change. High cohesion and low coupling keep a change local; readability makes it understood. Every principle below serves this goal; when no rule covers a case, decide by it.

## Principles

1. **Simple design** (Kent Beck's four rules, in priority order):
   1. Passes the tests.
   2. Reveals intention.
   3. No duplication of knowledge: merge code only when it is the same knowledge and changes for the same reason. Code that looks alike but changes for different reasons stays separate.
   4. Fewest elements (YAGNI): no class, interface, config option or extension point that the first three rules don't need, and nothing built for a need nobody has today.

   Within these rules, KISS: the most direct solution wins. No indirection, generality or cleverness that a direct solution doesn't need.
2. **Explicitness over magic.** What the code does and how it's wired is written where a reader can find it. It forbids behavior that happens through conventions, scanning or library defaults the reader can't see in the code.
3. **Code speaks the domain.** Code models the domain: it names the domain's concepts and enacts its rules. A name that describes mechanism where a domain concept exists fails this, even when the result is right.
4. **One level of abstraction.** A function either orchestrates or does detail work (parsing, persistence, formatting, protocol), never both. For example, a use case doesn't build SQL, parse JSON or speak HTTP.
5. **No surprises** (Principle of Least Astonishment). Code behaves as its name, shape and layer suggest. A function that reads like a query has no side effects.
6. **A type earns its place with a rule.** Avoid primitive obsession.
7. **Rich domain model.** Behavior that belongs to a domain object lives in that object, not in the use case.
8. **Tidy first, as its own commit.** When messy structure holds a change back, make the small behavior-preserving cleanup before the behavior change and commit it separately. Review and revert stay cheap, and the diff stays honest about structural vs behavioral change. Do it when `cost(tidying) + cost(change after tidying) < cost(change without tidying)`; skip it when the payoff is speculative. After the behavior change, tidy what it made messy, again as its own commit. A tidying that would pay off but isn't needed for this change isn't done: name it in the report, never in a TODO. Nothing unrelated to the task goes in the diff.
9. **Explicit failures.** No swallowed exceptions and no generic fallbacks. An error says what failed and keeps its cause, so transient and permanent errors stay distinguishable.
10. **No comments.** A comment is a smell: rename or extract until the code says it. The only exception is a why the code can't show, such as a constraint from outside the system.
11. **Readability first.** Optimize for the reader: when two options work, pick the one that needs less context to understand.
