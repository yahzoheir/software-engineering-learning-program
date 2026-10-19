## Movie unit tests

Movie is tested with JUnit 5.

Constructor tests verify that:
- Valid values are preserved and accessible through getters.
- Negative available copies are rejected.
- Zero available copies are accepted.
- Null, empty, and whitespace-only titles are rejected.

Rental inventory tests verify that:
- Renting a copy decreases availability by one.
- Renting the last available copy is allowed.
- Renting with zero available copies throws IllegalStateException
  and leaves availability unchanged.
- Returning a copy increases availability by one.

New behaviors were developed through Red-Green-Refactor cycles.
Additional boundary tests were added to verify existing behavior.

## Customer unit tests

Customer is tested with JUnit 5.

Constructor tests verify that:
- Valid values are preserved and accessible through getters.
- Null, empty, and whitespace-only names are rejected.

Active rental tests verify that:
- A newly created customer has no active rentals.
- The list returned by getActiveRentals() cannot be modified directly.
- Adding a rental stores it in the customer's active rentals.
- Removing a rental takes it out of the customer's active rentals.
- Removing one rental preserves the customer's other active rentals.
- Removing a rental that is not present leaves the list unchanged.

## Rental unit tests

Rental is tested with JUnit 5.

Constructor tests verify that:
- The id, customer, and movie passed to the constructor are stored
  and accessible through getters.
- A newly created rental is not returned.

Return status tests verify that:
- markReturned() changes an active rental to returned.
- markReturned() on an already returned rental throws
  IllegalStateException and leaves it returned.

## RentalService unit tests

RentalService is tested with JUnit 5.

Customer registration tests verify that:
- Registering a customer creates it with an id and name.
- Consecutive registrations receive consecutive ids.
- Null, empty, and whitespace-only names are rejected.
- A rejected registration does not use up an id.

Customer lookup tests verify that:
- getCustomerById() returns the registered customer.
- getCustomerById() rejects an unknown id.

Movie registration tests verify that:
- addMovie() creates a movie with an id, title, and available copies.
- Consecutive additions receive consecutive ids.
- Null, empty, and whitespace-only titles are rejected.
- Negative available copies are rejected.
- A rejected addition does not use up an id.
- Zero available copies are accepted.

Movie lookup tests verify that:
- getMovieById() returns the added movie.
- getMovieById() rejects an unknown id.

Rental creation tests verify that:
- rentMovie() creates a rental for the given customer and movie.
  The test uses different customer and movie ids so swapped arguments
  would be caught. The earlier version of this test passed the
  arguments in the wrong order and only passed because both ids were 1.
- Renting decreases the movie's available copies by one.
- The rental is added to the customer's active rentals, and it is the
  same object that rentMovie() returned.
- Consecutive rentals receive consecutive ids.

Rental rejection tests verify that:
- An unknown customer or an unknown movie throws
  IllegalArgumentException.
- A movie with no available copies throws IllegalStateException.
- A customer with 3 active rentals cannot rent a fourth. The third
  rental is allowed, and the fourth throws IllegalStateException.
- A rejected rental leaves available copies and the customer's active
  rentals unchanged, and does not use up a rental id.

Testing decisions for rentMovie():
- The unknown customer and unknown movie errors come from the existing
  lookup methods. The no-copies and rental-limit errors use
  IllegalStateException, matching Movie.rentCopy() and
  Rental.markReturned().
- The limit test rents three different movies, because the requirements
  do not say whether a customer may hold the same movie twice at once.
- The rental map has no getter yet, so rejected rentals are checked
  through the customer's active rentals, the movie's available copies,
  and the next rental id.

rentMovie() was developed through a Red-Green cycle. The first version,
which only created a rental, was committed before the new tests.
- Red: 22 RentalServiceTest tests ran, 4 failed. The new tests for
  decreasing copies, adding to the customer's active rentals, rejecting
  a movie with no copies, and rejecting a fourth rental failed because
  the behavior was missing. The other new tests already passed because
  the existing code covered them.
- Green: after adding the availability check, the rental limit check,
  and the inventory and customer updates, all 22 RentalServiceTest
  tests passed. The full suite ran 46 tests with 0 failures.
- No refactoring was needed.

Rental lookup tests verify that:
- getRentalById() returns the rental created by rentMovie().
- getRentalById() rejects an unknown id.

Rental return tests verify that:
- returnMovie() marks the rental as returned.
- Returning a rental increases the movie's available copies by one.
- The rental is removed from the customer's active rentals.
- The returned rental stays in the service and can still be found with
  getRentalById(), so rental history is preserved.
- Returning one rental leaves the customer's other rentals and the other
  movie's available copies unchanged.

Return rejection tests verify that:
- An unknown rental id throws IllegalArgumentException.
- Returning an already returned rental throws IllegalStateException.
- A rejected return leaves available copies, the customer's active
  rentals, and the rental's returned status unchanged. In particular, a
  second return does not add a second copy back to inventory.

Rental slot workflow test verifies that:
- A customer with 3 active rentals is rejected on a fourth rental.
- After returning one rental, the fourth rental succeeds.
- The customer ends with 3 active rentals, the returned movie has its
  copy back, and the newly rented movie has none available.
This checks that rentMovie() and returnMovie() work together.

Testing decisions for returnMovie():
- Unknown rental ids use IllegalArgumentException, like the other
  lookups. An already returned rental uses IllegalStateException, like
  Rental.markReturned().
- getRentalById() was added because the rentals map has no other way to
  be read. It also lets the tests check that returned rentals stay in
  the service as history.
- The new methods were first added as empty stubs (getRentalById()
  returned null, returnMovie() did nothing) so the tests could compile
  and fail for the right reasons.

returnMovie() was developed through a Red-Green cycle.
- Red: with the stubs, 56 tests ran, 46 passed, and the 10 new tests
  failed: 9 failures and 1 error. The error was the slot workflow test,
  where the fourth rental was still rejected because the return did not
  free the slot. The other failures were a null rental from the stub, no
  exception for an unknown or already returned rental, and no change to
  the rental, inventory, or customer's active rentals.
- Green: after implementing getRentalById() and returnMovie(), all 32
  RentalServiceTest tests passed. The full suite ran 56 tests with 0
  failures.
- No refactoring was needed.


## Integration tests

RentalWorkflowIntegrationTest is tested with JUnit 5.

These are in-memory integration tests. Each test creates a RentalService
and checks how RentalService, Movie, Customer, and Rental work together
across several steps. They use no database and no HTTP API.

Workflow tests verify that:
- Two customers competing for the last copy of a movie are handled
  correctly. The second customer is rejected while the first holds the
  copy, and succeeds after the first returns it.
- A customer can rent the same movie again after returning it. The
  first rental stays in history as returned, and the second rental has
  a different id and is the only active rental.
- Returning the middle one of three rentals keeps the other two in the
  customer's active rentals in their original order, and restores a
  copy only for the returned movie.
- Rejected rentals and a rejected return leave other customers, other
  movies, and existing rentals unchanged.
- A rejected rental creates no rental record, for both a movie with no
  available copies and a customer with 3 active rentals. getRentalById()
  rejects the id that the rejected rental would have used. Rental ids
  stay consecutive, and after one rental is returned the next
  successful rental receives id 4.

Results: all 5 integration tests passed, and the full suite ran 61
tests with 0 failures.

These tests passed as soon as they were written because rentMovie() and
returnMovie() already existed, so there was no Red phase. They add
coverage of multi-step behavior and do not drive new functionality.


## Test coverage

Coverage is measured with JaCoCo version 0.8.13, configured in pom.xml.
The plugin attaches to the test run and writes a report during the test
phase. No minimum coverage threshold is enforced yet, so the build does
not fail on coverage.

Command to run the tests and generate the report:
- mvn clean test

The HTML report is written to target/site/jacoco/index.html. The target
folder is ignored by Git, so reports are not committed.

Results from mvn clean test:
- Full test suite: 61 tests passed, 0 failed, 0 skipped.
- Line coverage: 100% (89/89).
- Branch coverage: 100% (26/26).
- Method coverage: 100% (26/26).
- Instruction coverage: 100% (351/351).
- Movie, Customer, Rental, and RentalService are each fully covered.

The syllabus target of at least 70% coverage was exceeded.

Coverage shows which code the tests execute. It does not show that the
assertions are strong enough or that the behavior is correct. For
example, the first rentMovie() test passed with its arguments swapped
because both ids were 1, and it would have counted as covered. Some
behavior is also outside the documented requirements and is untested:
Customer.addActiveRental() accepts a null or duplicate rental,
Movie.returnCopy() has no upper limit, and a customer may hold the
same movie twice at once. The tests were written for the documented
business rules, not to raise the coverage number.
