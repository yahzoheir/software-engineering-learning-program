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