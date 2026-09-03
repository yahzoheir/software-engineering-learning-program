# Library Management System - Database Relationships

```mermaid
flowchart TD

    BOOKS[BOOKS<br/>id PK<br/>title<br/>author]

    BOOK_COPIES[BOOK_COPIES<br/>id PK<br/>book_id FK<br/>status]

    MEMBERS[MEMBERS<br/>id PK<br/>name<br/>email]

    LIBRARIANS[LIBRARIANS<br/>id PK<br/>name<br/>email]

    LOANS[LOANS<br/>id PK<br/>member_id FK<br/>book_copy_id FK<br/>borrowed_at<br/>returned_at]

    BOOKS -->|one book has many copies| BOOK_COPIES
    MEMBERS -->|one member has many loans| LOANS
    BOOK_COPIES -->|one copy can have many loans over time| LOANS
```

## Relationship Notes

- `book_copies.book_id` references `books.id`.
- `loans.member_id` references `members.id`.
- `loans.book_copy_id` references `book_copies.id`.
- Librarians are stored separately because this version does not track which librarian handled each loan.