# Testing reports

These reports were written by the AI assistant at my request to record its implementation steps, test commands, failures, and results. They describe work performed by the assistant during the learning sessions, rather than steps I independently carried out. The checkpoints are a record of the workflow, not separate Git commits.

## Checkpoint 1 testing evidence

Performed on October 20, 2026, with Oracle JDK 22.0.2 and Maven 3.9.16. The compilation target is Java 21; execution on a JDK 21 runtime has not been verified.

1. Created the application bootstrap and BookCollectionTest before the controller.
2. First Maven invocation was accidentally run from the repository root, which has no pom.xml. This was an invocation error, not the TDD red result.
3. Ran Maven from book-store-api. One test ran and failed: expected HTTP 200, received 404. There was no controller for /books. This is the observed red result.
4. Added the controller returning an empty list. Maven ran one test successfully: zero failures, errors, or skips.
5. Simplified the response signature to List<?> instead of defining a fieldless placeholder book record. Reran the test after this refactor.

Test command used here:

```sh
mvn -B -ntp -Dmaven.repo.local=/private/tmp/book-store-maven-cache test
```

The temporary dependency cache avoids writing outside approved project locations. The normal developer command is mvn test.

The test uses the full Spring context and MockMvc, with real routing and JSON serialization. It is not a live-server network test. At checkpoint 1, no coverage measurement, CRUD behavior, persistence, or authentication was claimed. The Month 2 coverage target remains a later checkpoint; one passing endpoint test does not establish that target.

No Movie Rental files were changed. No commits or pushes were performed. The existing untracked month-02-history.bundle was preserved.

## Checkpoint 2: create and list books

Performed on October 20, 2026 with the same JDK and Maven versions.

- Wrote creation, listing, validation, and duplicate-title tests before implementing POST.
- Red result: 11 tests ran, 10 failed because POST returned 405 instead of 201. The original empty-list test passed. Invalid-input tests were blocked at their valid-book setup at this stage.
- Implemented creation, storage, generated IDs, and validation.
- Green result: 11 tests passed, zero failures, errors, or skips.
- Cases cover positive and zero price, blank and missing text fields, negative and missing price, malformed JSON, duplicate titles with distinct IDs, and preservation of an existing book after rejected input.
- Tests use full application context and MockMvc; no live network test or coverage measurement was performed.
- No commits or pushes were performed.

## Checkpoint 3: lookup by ID

Performed on October 20, 2026 using the same JDK and Maven versions.

- Added tests for retrieving the second of two books and requesting an unknown ID without changing the collection.
- Initial runs stalled while Mockito tried to attach an agent. Switching mock makers alone did not resolve this. The unused mock-maker configuration was removed.
- These integration tests do not use mocks. Explicit dependency-injection and context-cleanup test listeners avoid the unused Mockito listeners and preserve context isolation.
- Red result after resolving the runner issue: 13 tests ran, one failed. The existing-book request returned 404 instead of 200. The missing-book test already passed because the route did not yet exist; that alone was not evidence of implemented lookup behavior.
- Added repository lookup, service delegation, and the controller route.
- Green result: 13 tests passed with zero failures, errors, or skips.
- Used the previous Maven command with -o to use cached dependencies. No live-server test or coverage measurement was performed.
- No commits or pushes were performed. Movie Rental was not modified.

## Checkpoint 4: replace an existing book with PUT

Performed on October 20, 2026 using the same JDK and Maven versions and cached Maven dependencies.

- Added BookReplacementTest before implementing PUT.
- Red result: 23 tests ran, 10 failed. PUT returned 405 because the method was not supported. All 13 previous tests passed.
- Implemented replacement through controller, service, and repository. Renamed the validated request record to BookRequest for use by POST and PUT.
- Green result: 23 tests passed, zero failures, errors, or skips.
- Tested positive and zero prices, complete replacement while retaining the ID, persisted changes visible through GET, unchanged neighboring books, and repeated identical requests.
- Tested missing fields, blank title/author, negative price, and malformed JSON returning 400 while preserving the original book.
- Tested a valid PUT to a missing ID returning 404 without changing existing data or consuming a generated ID.
- No live-server test or coverage measurement was performed. No commits or pushes were performed. Movie Rental was not modified.

## Checkpoint 5: delete a book

Performed on October 20, 2026 using the same JDK and Maven versions and cached Maven dependencies.

- Added three deletion tests before implementing DELETE.
- Red result: 26 tests ran, three failed because DELETE returned 405. All 23 previous tests passed.
- Implemented deletion through controller, service, and repository.
- Green result: 26 tests passed with zero failures, errors, or skips.
- Verified 204 with an empty body, subsequent GET and repeated DELETE returning 404, preservation of another book, and rejection of an unknown ID without changing existing data.
- Verified that deleting the last book leaves GET /books returning 200 with [] and that the next created book receives a new ID.
- No live-server test or coverage measurement was performed. No commits or pushes were performed. Movie Rental was not modified.

## Checkpoint 6: coverage measurement and threshold

Performed on October 21, 2026 with Oracle JDK 22.0.2 and Maven 3.9.16.

- Added JaCoCo 0.8.13, matching the existing Movie Rental plugin version without changing that project.
- prepare-agent collects execution data during tests. report and check run during verify.
- Enforced a minimum LINE COVEREDRATIO of 0.70 at BUNDLE scope: overall production code in book-store-api. No custom exclusions are configured. JaCoCo's built-in generated-code filters still apply.
- Ran `mvn -B -ntp -Dmaven.repo.local=/private/tmp/book-store-maven-cache clean verify` successfully.
- All 26 tests passed with zero failures, errors, or skips. The 70% line coverage check passed.
- Overall line coverage: 36 covered of 38, or 94.74%. Branch coverage: 10 covered of 10, or 100%. Branch coverage is reported but is not a separate enforced threshold.
- Controller, service, and repository each have 100% measured line coverage. The two uncovered lines are the main entry point in BookStoreApplication, which the MockMvc tests do not call. No test was added solely to raise the percentage.
- HTML report: target/site/jacoco/index.html. Machine-readable results: target/site/jacoco/jacoco.xml and jacoco.csv. These are ignored build outputs and can be regenerated.
- This confirms execution coverage, not complete correctness. Validation comes from framework annotations, so high coverage of the request record alone does not prove every validation case was asserted. Existing behavior tests remain necessary.
- No new behavior or TDD cycle is claimed for this configuration step. No live-server test was performed. No commits or pushes were performed. Movie Rental was not modified.
