# Incremental design

These notes were written by the AI assistant at my request to document the setup, implementation choices, and decisions discussed during the learning sessions. They are an implementation record, not a claim that I independently designed or wrote every part of the project.

## Repository conventions observed

Independent Maven projects live under month-02. Existing projects use JUnit 5 and docs alongside source. The pricing exercise targets Java 21; Movie Rental targets Java 22. The root ignore file already excludes target/. No existing Spring Boot conventions were found.

## Checkpoint 1 decisions

- New folder: month-02/book-store-api; no changes to existing projects.
- Java 21 compilation target, compiled and tested with the installed JDK 22.
- Spring Boot 3.5.16, Maven, Spring MVC, and Boot-managed JUnit 5 dependencies.
- Package com.yahia.bookstore, following the pricing exercise's com.yahia Maven group.
- Application bootstrap plus a controller. Defer service/repository abstractions until behavior needs them.
- GET /books returns 200 and an empty JSON array. An empty collection is successful, rather than a missing resource.

## Checkpoint 2 decisions

- Book record: numeric server-generated id, title, author, BigDecimal price.
- Separate validated creation request, controller for HTTP, service for the use case, and concrete in-memory repository for storage. No repository interface is needed yet.
- Validation happens before the service runs. Blank/missing text and missing/negative price return 400. Zero is valid.
- Repository operations are synchronized and list responses are immutable snapshots.
- POST returns 201 and the created book. A Location header is deferred until fetching an individual book is implemented.
- Tests use a fresh application context per invocation to isolate stored state.

## Checkpoint 3 decisions

- GET /books/{id} uses the numeric ID to find a single book through controller, service, and repository.
- The repository and service return Optional<Book> so an absent result is explicit.
- The controller maps a present result to 200 with JSON and an absent result to 404 with an empty body.
- Reading a missing book does not change stored data. Structured error responses remain a later decision.

## Checkpoint 4 decisions

- PUT /books/{id} replaces all editable fields of an existing book and keeps its ID. Success returns 200 with the updated book.
- Renamed CreateBookRequest to BookRequest because POST and PUT require the same complete set of fields and validation.
- Invalid or incomplete input returns 400 before storage changes. A valid request for a missing ID returns 404 with an empty body and does not create a book or consume an ID.
- Replacement runs in one synchronized repository operation, leaving other books unchanged.
- Repeating the same request keeps the same values, ID, and collection size.

## Checkpoint 5 decisions

- DELETE /books/{id} removes only the matching book through controller, service, and repository.
- A successful deletion returns 204 with no response body. An unknown or already deleted ID returns 404.
- The synchronized repository operation returns whether a book was removed. Deletion does not reset the ID sequence.
- Deleting the final book leaves a valid empty collection, returned by GET /books as 200 with [].

## Proposed next checkpoints

- Decide whether PATCH is needed before adding it.
- Add a Location header to successful POST responses, with a test following it to the created book.
- Persistence, structured error responses, authentication remain separate decisions.

ISBN uniqueness, currency, stock, ordering, pagination, database choice, and authentication policy remain undecided. No orders, payments, or customer accounts are included yet.


## Checkpoint 6 decisions

- Use JaCoCo for coverage reporting and enforce the Month 2 minimum as 70% overall production-code line coverage.
- Keep all production classes in scope with no custom exclusions. Report branch coverage as supplementary evidence.
- Run the report and threshold check with Maven verify; use clean verify for a fresh measurement.
