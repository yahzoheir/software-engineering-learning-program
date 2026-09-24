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