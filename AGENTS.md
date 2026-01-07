# AGENTS.md

## Scope
- Multi-module Maven demo for Chronicle Queue and related libraries.
- Keep changes minimal and preserve public APIs unless explicitly requested.

## Build and test
- Preferred full check:
  - `mkdir -p logs`
  - `mvn verify -l logs/mvn-verify.log`
- Module-scoped example:
  - `mvn -pl <module> -am test -l logs/mvn-test-<module>.log`
- Test example:
  - `mvn -Dtest=ClassName test -l logs/mvn-test.log`
- YAML scenario tests (only when updating expected outputs):
  - `mvn test -Dregress.tests -l logs/mvn-regress.log`
- Review logs:
  - `rg -n '^\[(WARNING|ERROR)\]|SLF4J\(W\)|\bWARNING:|\bwarning:' logs/mvn-verify.log`
- Do not commit logs/.

## Repo map
- Primary docs: `README.adoc`, `architecture.adoc`, `usage-and-tests.adoc`, `reference.adoc`, and per-module `README.adoc` files.

## Constraints
- Java baseline: 8 (avoid newer language features).
- Source files must stay ISO-8859-1 (code points 0-255). Prefer ASCII; avoid smart quotes and non-breaking spaces.
- Treat warnings as defects; keep logs clean.
- Avoid extra allocations or synchronisation on hot paths.
- Never commit secrets or credentials; document security trade-offs in Javadoc or `.adoc` files.

## Docs and review checklist
- Keep documentation, tests, and code synchronised.
- For large mechanical changes, declare the transformation rule and keep it consistent.
- Add clarifying comments only when intent is non-obvious.

## References
- `OpenHFT/docs/Company-Wide-Tagging.adoc` for tagging and decision record templates.
