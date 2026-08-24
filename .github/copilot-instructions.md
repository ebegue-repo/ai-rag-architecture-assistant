# GitHub Copilot Instructions

## Project

This is a personal portfolio project demonstrating a production-oriented RAG application for querying architecture documentation using natural language.

The application is built with Java and Spring Boot and will evolve incrementally from a simple document-processing application into a RAG-based architecture assistant.

## Engineering Principles

- Prefer simple, readable solutions over unnecessary abstractions.
- Keep changes small, focused, and easy to review.
- Follow standard Java and Spring Boot conventions.
- Prefer composition and clear responsibilities over complex inheritance hierarchies.
- Do not introduce a dependency unless there is a clear technical reason.
- Preserve existing behaviour unless the requested change requires otherwise.
- Do not introduce speculative features or infrastructure.
- Do not over-engineer for future requirements.
- Prefer explicit, understandable code over clever implementations.

## Architecture

- Follow the architectural direction documented in `docs/ARCHITECTURE.md`.
- Keep domain and application logic independent from infrastructure where practical.
- Avoid coupling core business logic directly to external services.
- Introduce abstractions only when they provide a concrete architectural or testing benefit.
- Do not introduce LLMs, embeddings, vector databases, external AI services, or other infrastructure unless they are required by the feature being implemented.
- When an architectural decision is unclear, explain the trade-offs before making a significant change.

## Development Workflow

- Before making significant changes, inspect the relevant existing code and documentation.
- Prefer incremental implementation over large generated changes.
- For non-trivial behaviour, add or update unit tests.
- Run the relevant tests after making changes.
- Do not modify unrelated files.
- Do not change build configuration or dependencies unless required by the task.
- Do not generate large amounts of code without first explaining the proposed approach.

## Testing

- Tests should verify behaviour rather than implementation details.
- Prefer focused unit tests for domain and application logic.
- Include relevant edge cases.
- Keep tests readable and maintainable.
- Do not weaken or remove tests merely to make an implementation pass.

## Code Quality

- Use meaningful names.
- Keep methods and classes focused on a single responsibility.
- Avoid unnecessary comments. Prefer self-explanatory code.
- Handle invalid input explicitly where appropriate.
- Avoid premature optimisation.
- Follow the existing project conventions when they are consistent with these instructions.

## Agent Behaviour

When asked to implement a feature:

1. Inspect the relevant code and documentation first.
2. Briefly explain the proposed approach and the files that need to change.
3. Implement the smallest complete solution that satisfies the requirement.
4. Add or update tests for the relevant behaviour.
5. Run the relevant tests.
6. Summarise what changed and mention any assumptions or trade-offs.

Do not make unrelated improvements unless explicitly requested.