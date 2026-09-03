# Library Management System - Design Notes 

## Initial Design Approach

I first sketched the system on paper before creating the digital version.

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
```

## Indexing Decisions

### Index on `book_copies.book_id`

```sql
CREATE INDEX idx_book_copies_book_id
ON book_copies(book_id);
```

I added this index because the system will often need to find all copies of a specific book when checking availability. Since `book_id` is used for that lookup and for joins with `books`, indexing it should make those searches faster as the table grows.

### Index on `loans.member_id`

```sql
CREATE INDEX idx_loans_member_id
ON loans(member_id);
```

I indexed `loans.member_id` because the system needs to look up a member's active loans, especially when checking the 5-book borrowing limit.

### Index on `loans.book_copy_id`

```sql
CREATE INDEX idx_loans_book_copy_id
ON loans(book_copy_id);
```

I indexed `loans.book_copy_id` because loans are tied to specific physical copies, so this helps when joining loans with book copies or checking a copy's loan history.