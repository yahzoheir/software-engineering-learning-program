# Movie Rental System

A Java Maven project for practicing requirements, design, and testing on a movie rental system.

It is an in-memory Java application. It has no database, REST API, or user interface. Data exists only while the program is running.

## Implemented Functionality

- Add movies with a title and a number of available copies.
- Register customers.
- Look up movies, customers, and rentals by id.
- Rent an available movie to a customer.
- Return a rented movie.
- Check a movie's available copies and a customer's active rentals.

## Main Classes

- `Movie`: stores a title and available copies, and protects its own inventory.
- `Customer`: stores a name and the customer's active rentals.
- `Rental`: records a customer renting a movie and whether it has been returned.
- `RentalService`: stores movies, customers, and rentals, assigns ids, and enforces the renting and returning rules.

## Business Rules

- Available copies cannot be negative.
- Movie titles and customer names cannot be null, empty, or whitespace only.
- A customer can have at most 3 active rentals.
- The customer and the movie must exist, and the movie must have an available copy.
- A successful rental decreases available copies by one.
- A return must refer to an existing rental that has not already been returned.
- A successful return increases available copies by one.
- Returned rentals stay in the system as history.
- A rejected rental or return changes nothing.

## Requirements

- Java 22
- Apache Maven

## Build and Test

From this folder, run:

```text
mvn clean test
```

This compiles the project, runs all tests, and generates the JaCoCo coverage report.

The HTML coverage report is written to `target/site/jacoco/index.html`.

## Testing

The project has 61 JUnit 5 tests:

- Unit tests for `Movie`, `Customer`, `Rental`, and `RentalService`.
- 5 in-memory integration tests in `RentalWorkflowIntegrationTest` that check how the classes work together across rent and return workflows.

JaCoCo reports 100% line and branch coverage. Coverage shows which code the tests run, not that the behavior is correct.

## Documentation

- [Requirements](./docs/requirements.md)
- [Design Notes](./docs/design.md)
- [Testing Notes](./docs/testing.md)
