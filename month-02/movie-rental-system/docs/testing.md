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