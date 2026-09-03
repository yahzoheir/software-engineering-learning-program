# Library Management System — Design Notes

## Initial Design Approach

I first sketched the system on paper before creating the digital version. (unfortunately I lost that sketch but I just did everything cleanly from the beginning and represented it here.)

My goal was to identify the main entities, understand their responsibilities, and separate the business logic from the data itself.

## Book and BookCopy

I separated `Book` from `BookCopy`.

`Book` represents the general book information:

- title
- author

`BookCopy` represents one physical copy owned by the library.

This separation is useful because the library can own multiple copies of the same book while each individual copy can have its own availability status.

For example:

```text
Book: Clean Code
    ├── Copy 1 — AVAILABLE
    ├── Copy 2 — BORROWED
    └── Copy 3 — AVAILABLE