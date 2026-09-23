# Movie Rental System: Requirements

## Purpose

Build a system for managing a movie catalogue, customers, rentals, and returns. The project applies software design principles and Month 2 testing practices through a working implementation.

## Initial Features

- Add movies to the catalogue and record their available copies.
- Register customers.
- Rent an available movie to a customer.
- Return a rented movie.
- View movie availability and a customer’s active rentals.

## Business Rules

### Movie Inventory

- Each movie has a unique identifier, a title, and an available-copy count.
- The available-copy count cannot be negative.
- A movie is available for rental when at least one copy is available.

### Rentals

- A customer can have at most **3 active rentals**.
- Both the customer and movie must exist before a rental can be created.
- A successful rental creates a rental record and decreases available copies by one.
- A rejected rental must not change inventory or create a rental record.

### Returns

- A return must refer to an existing, active rental.
- A successful return marks the rental as returned and increases available copies by one.
- A rental cannot be returned twice.
- Returned rentals remain in the system as rental history.

## Invalid Actions

The system must reject invalid inputs and actions with a clear explanation, including:

- Negative inventory.
- Renting for an unknown customer or an unknown movie.
- Renting an unavailable movie.
- Renting when the customer has reached the rental limit.
- Returning a rental that does not exist or has already been returned.
- Creating a movie with a null, empty, or whitespace-only title.
- Registering a customer with a null, empty, or whitespace-only name.

## Outside the Initial Scope

- Rental pricing, payments, and late fees.
- Reservations.
- Authentication.
- A database, REST API, or graphical interface.
