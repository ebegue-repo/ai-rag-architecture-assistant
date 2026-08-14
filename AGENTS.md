# AI RAG Architecture Assistant - Agent Instructions

## Project

This is a personal portfolio project demonstrating a production-oriented
RAG application for querying architecture documentation.

## Engineering principles

- Prefer simple solutions over unnecessary abstractions.
- Keep changes small and focused.
- Do not introduce new dependencies without explaining why they are needed.
- Write tests for non-trivial behaviour.
- Prefer explicit, readable Java code.
- Follow standard Spring Boot conventions.
- Do not generate large amounts of code without first explaining the approach.
- Preserve existing architecture and conventions unless there is a clear reason
  to change them.

## Development workflow

- Work on feature branches.
- Do not commit directly to `main` or `develop`.
- Keep pull requests focused on one feature or change.
- Run the relevant tests before considering a change complete.

## Architecture

The application is intended to evolve into a RAG-based architecture assistant.

The initial implementation should remain deliberately simple.
Do not introduce vector databases, LLM integration, embeddings or document
processing until they are required by a specific feature.

## Agent workflow

Before making significant changes:

1. Inspect the relevant project documentation.
2. Explain the proposed approach and affected files.
3. Identify important technical decisions and trade-offs.
4. Wait for user approval before making significant architectural changes.
5. Keep changes focused on the requested task.
6. Run relevant tests after implementation.