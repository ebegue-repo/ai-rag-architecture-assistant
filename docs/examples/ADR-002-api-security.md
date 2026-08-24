# ADR-002: Use OAuth 2.0 and OpenID Connect for API Security

- Status: Accepted
- Date: 2026-08-24
- Decision Makers: Architecture Team
- Tags: security, authentication, authorization, APIs

## Context

The platform exposes APIs consumed by web applications, internal services, and external integration partners.

The existing services use different authentication mechanisms. Some services validate locally managed credentials, while others rely on custom API keys.

This fragmentation makes security policies difficult to enforce consistently and increases the operational cost of managing authentication.

The platform also needs to distinguish between authentication and authorization. Knowing the identity of a caller is not sufficient to determine which operations that caller is allowed to perform.

## Problem

We need a standard authentication and authorization mechanism that can be applied consistently across all APIs.

The solution must support:

- human users;
- service-to-service communication;
- delegated access;
- token-based authentication;
- fine-grained authorization;
- integration with an external identity provider.

The solution should also allow APIs to remain independent from the implementation details of the identity provider.

## Decision

All platform APIs will use OAuth 2.0 for authorization and OpenID Connect for user authentication.

Access tokens will be issued by a central identity provider.

APIs will validate access tokens and enforce authorization based on scopes and roles.

### Authentication

Human users will authenticate through the identity provider using OpenID Connect.

The API will not manage user passwords or credentials directly.

### Service-to-Service Authentication

Services communicating with other services will use OAuth 2.0 client credentials.

Each service will have its own identity and credentials.

Services must not share client credentials.

### Authorization

APIs will use OAuth scopes to control access to resources and operations.

Roles may be used when authorization decisions depend on business responsibilities rather than individual API permissions.

Authorization decisions must be enforced by the service that owns the protected resource.

### Token Validation

APIs will validate access tokens locally where possible.

The following claims should be validated:

- issuer;
- audience;
- expiration;
- signature;
- scopes;
- roles where applicable.

APIs must reject invalid or expired tokens.

### Identity Provider

The identity provider is an external infrastructure component.

Application code must not depend directly on provider-specific SDKs unless there is a concrete requirement that cannot be satisfied through standard OAuth 2.0 and OpenID Connect mechanisms.

## Alternatives Considered

### API Keys

API keys are simple to implement but provide weak identity semantics and limited authorization capabilities.

They also create operational problems around rotation, revocation, and credential sharing.

### Basic Authentication

Basic authentication does not provide an appropriate security model for the platform and would require services to manage credentials directly.

### Custom Authentication Protocol

A custom authentication mechanism would increase implementation and maintenance costs while providing little additional value.

## Consequences

### Positive Consequences

- Consistent authentication across APIs.
- Centralised identity management.
- Standardised service-to-service authentication.
- Fine-grained authorization.
- Reduced application responsibility for credential management.
- Better interoperability with external systems.

### Negative Consequences

- The platform becomes dependent on an identity provider.
- Token validation introduces additional application complexity.
- Developers need to understand OAuth 2.0 and OpenID Connect.
- Incorrect authorization configuration could result in security vulnerabilities.

## Implementation Notes

Spring Security will be used for OAuth 2.0 resource server functionality.

API services should remain responsible for authorization decisions while authentication infrastructure remains centralised.

Security-related configuration must be externalised and must not contain credentials in source control.