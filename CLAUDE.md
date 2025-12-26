# CLAUDE.md

This file provides guidance to Claude Code (claude.ai/code) when working with code in this repository.

## Project Overview

URL Shortener service built in Scala 3.3.7 as a learning project. The goal is to implement a production-quality URL shortening service while learning functional programming concepts in Scala.

## Build System

- **Build tool**: sbt 1.11.7
- **Scala version**: 3.3.7

### Common Commands

```bash
# Compile the project
sbt compile

# Run the application
sbt run

# Run tests
sbt test

# Run a specific test
sbt "testOnly *YourTestClass"

# Launch sbt interactive shell
sbt

# Clean build artifacts
sbt clean

# Continuous compilation (watches for file changes)
sbt ~compile

# Continuous testing
sbt ~test
```

## Project Architecture

This is a phased learning project designed to progressively introduce Scala functional programming concepts:

### Planned Implementation Phases

1. **Phase 1 - Basic In-Memory**: REST API with in-memory Map storage, simple random short code generation, basic validation
2. **Phase 2 - Short Code Generation**: Base62 encoding, counter/hash-based approach, collision handling
3. **Phase 3 - Persistence**: Database integration, connection pooling, transactions
4. **Phase 4 - Features**: Custom aliases, expiration, analytics, sessions
5. **Phase 5 - Production**: Caching, rate limiting, logging, configuration

### Core Components (Planned)

**HTTP Server Layer** - REST endpoints for shortening URLs and redirecting
- POST /shorten - Create short URL
- GET /{shortCode} - Redirect to original URL

**Business Logic Layer** - Short code generation, URL validation, collision detection

**Data Access Layer** - URL mapping storage and retrieval

### Key Technical Decisions

**Short Code Generation Strategy**:
- Options: Random generation vs sequential counter vs hash-based
- Trade-offs: Code length vs collision probability
- Character set: Base62 (0-9, a-z, A-Z) recommended

**Storage Strategy**:
- Start: In-memory (fast, simple to implement)
- Later: Database with caching (persistent, production-ready)

**Error Handling**:
- Use Either/Option types (functional approach)
- Avoid exceptions for control flow
- Create sealed trait ADTs for error types

## Scala Features to Emphasize

When working in this codebase, prioritize these Scala 3 features:

- **Case classes & ADTs** for domain modeling (URL, ShortCode, UrlMapping, errors)
- **Option & Either** for null-safety and error handling
- **For-comprehensions** for composing validations and operations
- **Traits** for repository pattern and dependency injection
- **Pattern matching** for routing, error handling, request parsing
- **Immutable collections** (Map, List, etc.)
- **Futures or IO monads** for async operations

## Code Organization Guidelines

- Use layered architecture: HTTP layer → Service layer → Data layer
- Keep domain models immutable (case classes)
- Define repository traits for testability
- Use companion objects for factory methods and utilities
- Validation logic should return Either[Error, Success]