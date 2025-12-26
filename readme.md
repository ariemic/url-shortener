# URL Shortener - Requirements & Learning Guide
https://codefarm0.medium.com/designing-a-url-shortener-tinyurl-cb3bcfe79dd2

## Functional Requirements

**Core Features**
1. **URL Shortening**
    - Accept long URL from user
    - Generate unique short code (e.g., "abc123")
    - Store mapping between short code and original URL
    - Return shortened URL to user

2. **URL Redirection**
    - Accept short code
    - Look up original URL
    - Redirect user to original URL (HTTP 301 or 302)
    - Handle non-existent short codes gracefully

3. **URL Management** (optional but recommended)
    - List all URLs for a user/session
    - Delete/expire URLs after certain time
    - View click statistics for each short URL
    - Custom alias support (user chooses short code)

4. **Validation**
    - Validate input URLs are properly formatted
    - Check URL is reachable (optional)
    - Prevent malicious URLs
    - Handle duplicate long URLs (return existing short code or create new)

**Non-functional Requirements**
- Fast lookups (< 50ms)
- Handle concurrent requests
- Persist data across restarts
- Generate collision-free short codes

## Technical Architecture

**Components You'll Need**

1. **HTTP Server Layer**
    - REST API endpoints (POST /shorten, GET /{shortCode})
    - Request/response handling
    - Error handling and status codes

2. **Business Logic Layer**
    - Short code generation algorithm
    - URL validation logic
    - Collision detection/resolution

3. **Data Access Layer**
    - Store URL mappings
    - Retrieve by short code
    - Query capabilities for analytics

4. **Optional: Caching Layer**
    - Cache frequently accessed URLs
    - Improve read performance

## Scala Features You Should Learn & Use

### 1. **Case Classes & ADTs (Algebraic Data Types)**
Use these for:
- Domain models (URL, ShortCode, UrlMapping)
- Request/response DTOs
- Error types (sealed trait with case classes for different errors)

This teaches you: Type-safe modeling, pattern matching, immutability

### 2. **Option & Either Types**
Use for:
- Handling nullable results (lookup might return None)
- Error handling (Either[Error, Success])
- Validation results

This teaches you: Functional error handling, avoiding null, composing operations

### 3. **For-Comprehensions**
Use for:
- Chaining validations
- Composing database operations
- Handling multiple Option/Either values

This teaches you: Monadic composition, readable sequential logic

### 4. **Traits & Composition**
Use for:
- Repository pattern (trait UrlRepository)
- Service interfaces
- Different implementations (InMemoryRepo, DatabaseRepo)

This teaches you: Abstraction, dependency injection, testability

### 5. **Implicits/Given (Scala 3)**
Use for:
- JSON encoding/decoding
- Type class instances
- Execution contexts for async operations

This teaches you: Type classes, compiler-driven code generation

### 6. **Collections API**
Use for:
- In-memory storage (Map for quick lookups)
- Filtering, transforming data
- Working with lists of URLs

This teaches you: Functional data transformation, immutable collections

### 7. **Futures or IO Monads**
Use for:
- Async database operations
- HTTP requests (if validating URLs)
- Non-blocking request handling

This teaches you: Asynchronous programming, effect management

### 8. **Pattern Matching**
Use for:
- Routing logic
- Error handling
- Parsing request parameters

This teaches you: Exhaustiveness checking, destructuring, control flow

## Recommended Tech Stack Choices

**HTTP Framework** (pick one):
- **http4s** - Pure functional, cats-effect based, modern
- **Akka HTTP** - Battle-tested, actor-based
- **ZIO HTTP** - If you want to learn ZIO ecosystem

**JSON Library** (pick one):
- **Circe** - Most popular, type-safe, automatic derivation
- **Play JSON** - Simple, less type-safe
- **ZIO JSON** - If using ZIO

**Database/Persistence** (pick one):
- **In-Memory Map** (start here - simplest)
- **Doobie** - Pure functional SQL, PostgreSQL/MySQL
- **Slick** - Type-safe queries, more ORM-like
- **Quill** - Compile-time query generation

**Effect System** (optional but educational):
- **Cats Effect** - Industry standard, pure functional
- **ZIO** - Batteries included, easier to start
- **Standard Futures** - Simpler but less powerful

## Implementation Phases

### Phase 1: Basic In-Memory Version
- REST API with two endpoints
- In-memory Map storage
- Simple random short code generation
- Basic validation

**Scala concepts**: Case classes, Option, HTTP library basics, immutable collections

### Phase 2: Better Short Code Generation
- Implement base62 encoding (0-9, a-z, A-Z)
- Use counter or hash-based approach
- Handle collisions properly

**Scala concepts**: String manipulation, algorithms, recursion/tail recursion

### Phase 3: Add Persistence
- Replace Map with database
- Connection pooling
- Transaction handling

**Scala concepts**: IO monad, resource management, database libraries

### Phase 4: Add Features
- Custom aliases
- Expiration dates
- Click tracking/analytics
- User sessions

**Scala concepts**: More complex domain modeling, time handling, state management

### Phase 5: Production Concerns
- Caching layer
- Rate limiting
- Logging and monitoring
- Configuration management

**Scala concepts**: Type-safe configuration, caching strategies, middleware patterns

## Key Design Decisions You'll Face

1. **Short Code Generation Strategy**
    - Random generation vs sequential counter vs hash-based
    - Length of code (trade-off: shorter = fewer possibilities)
    - Character set to use

2. **Storage Strategy**
    - In-memory (fast but lost on restart)
    - Database (persistent but slower)
    - Hybrid (database + cache)

3. **Concurrency Handling**
    - How to handle race conditions in code generation
    - Thread-safe collections vs database constraints

4. **Error Handling Pattern**
    - Exceptions vs Either/Option
    - How to surface errors to API layer

5. **Code Organization**
    - Layered architecture vs vertical slices
    - Where to put validation logic

## Learning Outcomes

By the end, you'll understand:
- HTTP APIs in Scala
- Functional error handling
- Type-safe domain modeling
- Database interaction patterns
- Async/concurrent programming
- Testing strategies (unit tests with ScalaTest/MUnit)
- Configuration and deployment

## Suggested Starting Point

Begin with:
1. Define your domain models (case classes)
2. Create a simple in-memory repository using Map
3. Build HTTP endpoints with your chosen framework
4. Add validation using Either or custom error types
5. Implement short code generation
6. Test everything
7. Then gradually add persistence, caching, features

Want me to clarify any specific aspect or help you choose between the technology options?