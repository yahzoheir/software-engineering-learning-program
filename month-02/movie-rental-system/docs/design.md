# Movie Rental System Design

### Class Responsibilities And Relations

## Movie

Represents a movie in the catalogue and tracks its available copies.

## Customer

Represents a registered customer.

## Customer Active Rentals

Each Customer stores a collection of their active rentals using an
ArrayList<Rental>.

This allows the system to access a customer's current rentals directly.
The collection's size provides the active-rental count, so a separate
count field is unnecessary.

RentalService coordinates updates to this collection:

- After a successful rental, add the rental to the customer's collection.
- After a successful return, remove the rental from the collection.
- If an operation is rejected, leave the collection unchanged.

The customer's collection and the main rental map reference the same
Rental objects. Returned rentals remain in the main map for history
but are removed from the customer's active collection.

## Rental

Records the relationship between a customer and a rented movie, including whether it has been returned.

Rental records remain available after a return to preserve history.

Each rental has a unique ID that identifies a single rental transaction.

A customer may rent the same movie again after returning it. Each
transaction needs its own ID so the system can process the correct
return and preserve rental history.

Individual physical copies are not identified separately. Movie
inventory is tracked using an available-copy count.

## RentalService

Coordinates renting and returning movies and enforces the rules for those operations.

The customer's rental limit is checked here because it depends on the customer's active rentals. A Movie is responsible for its own inventory and should not need to know about customer rental history.



## In-Memory Storage

RentalService stores movies, customers, and rentals in separate HashMaps.

Each map uses the record's unique integer ID as its key and the
corresponding object as its value. This provides O(1) average lookup
when finding a movie, customer, or rental by ID.

The data exists only while the program is running. It is not
persisted between runs.

## Renting Flow

Before creating a rental, RentalService checks that:

1. The customer exists.
2. The movie exists.
3. At least one copy of the movie is available.
4. The customer has fewer than 3 active rentals.

If any check fails, the operation is rejected without changing inventory
or rental records.

After all checks pass, RentalService:

1. Creates a Rental with a unique ID and returned set to false.
2. Adds it to the main rental map.
3. Adds the same Rental object to the customer's active rentals.
4. Decreases the movie's available-copy count by one.

## Returning Flow

Before accepting a return, RentalService checks that:

1. The rental exists.
2. The rental has not already been returned.

If either check fails, the operation is rejected without changing
inventory or rental records.

After both checks pass, RentalService:

1. Marks the rental as returned.
2. Removes it from the customer's active rentals.
3. Increases the movie's available-copy count by one.

The rental remains in the main rental map to preserve history.

Rejecting duplicate returns prevents the same copy from being added
back to available inventory more than once.

## ID Generation

RentalService maintains separate integer counters for movies,
customers, and rentals.

Each counter starts at 1 and increases whenever a new record is created.
The service assigns IDs automatically.

IDs are unique within each record type. A movie and a customer may
have the same numeric ID because they are stored in separate maps.

Counters reset when the application restarts, along with the
in-memory data.

## Protecting Movie Inventory

Movie keeps its available-copy count private and does not expose
a general setter.

Inventory changes through two methods:

- rentCopy(): decreases available copies by one. Rejects the operation
  if no copies are available.
- returnCopy(): increases available copies by one.

Movie also rejects a negative initial available-copy count.

RentalService checks the full rental or return operation before
calling these methods. It ensures that returnCopy() is called only
for an existing rental that has not already been returned.

This keeps inventory changes controlled while allowing Movie to
protect its own state.

## Protecting Customer Active Rentals

Customer keeps its active-rental collection private.

The collection is updated through controlled methods:

- addActiveRental(Rental rental): adds an active rental.
- removeActiveRental(Rental rental): removes a rental after a return.

Code viewing the customer's active rentals receives a read-only view,
so it cannot directly add or remove entries.

RentalService coordinates these updates with the main rental map
and movie inventory. It checks the rental limit before adding a rental.

## Protecting Rental Records

Rental keeps its fields private.

Its ID, customer reference, and movie reference are assigned during
creation and cannot be replaced afterward.

A new rental starts with returned set to false. The status changes
through markReturned(), which rejects the operation if the rental
has already been returned.

There is no general setter for the returned status, so a completed
rental cannot be made active again.

RentalService coordinates this status change with the customer's
active rentals and the movie's available-copy count.

## Title and Name Validation

Movie validates its title during construction.
Customer validates its name during construction.

Null, empty, and whitespace-only values are rejected with
IllegalArgumentException and a clear message.

Validation happens before the object is added to the service's maps.