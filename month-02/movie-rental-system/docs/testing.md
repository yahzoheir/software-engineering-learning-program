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