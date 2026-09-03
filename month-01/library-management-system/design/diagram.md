# Library Management System - Diagrams

## UML Class Diagram

```mermaid
classDiagram

class Book {
    private int id
    private String title
    private String author
}

class BookCopy {
    private int id
    private BookStatus status
}

class BookStatus {
    <<enumeration>>
    AVAILABLE
    BORROWED
}

class Member {
    private int id
    private String name
    private String email
}

class Librarian {
    private int id
    private String name
    private String email
}

class Loan {
    private int id
    private LocalDate borrowedAt
    private LocalDate returnedAt
}

class LibraryService {
    public borrowBook(int memberId, int bookId) Loan
    public returnBook(int loanId) void
    public searchBooks(String query) List~Book~
    public getCurrentLoans(int memberId) List~Loan~
}

class BookRepository {
    <<interface>>
    public search(String query) List~Book~
    public findAvailableCopy(int bookId) BookCopy
    public save(Book book) void
    public saveCopy(BookCopy copy) void
    public deleteCopy(int copyId) void
}

class LoanRepository {
    <<interface>>
    public save(Loan loan) void
    public findById(int id) Loan
    public findActiveByMember(int memberId) List~Loan~
    public findAll() List~Loan~
    public countActiveLoans(int memberId) int
}

Book "1" --> "*" BookCopy : has
BookCopy --> BookStatus : has status

Member "1" --> "*" Loan : borrows
BookCopy "1" --> "*" Loan : used in

LibraryService ..> BookRepository : depends on
LibraryService ..> LoanRepository : depends on

Member ..> LibraryService : uses
Librarian ..> LibraryService : manages through
```


## Why I Used Repository Interfaces

I used `BookRepository` and `LoanRepository` so that the main library logic does not need to know how the data is actually stored.

For example, `LibraryService` only needs to know that it can search for books, find an available copy, save a loan, or check how many active loans a member has. It should not be responsible for writing SQL queries or knowing whether the data is stored.

Using interfaces lets me define the operations the system needs without tying the business logic to one specific implementation.

I also kept books and loans in separate repositories because they represent different responsibilities. `BookRepository` handles books and physical copies, while `LoanRepository` handles borrowing records.

This keeps the code more organized and makes the storage implementation easier to replace or test later without changing `LibraryService`.


## Borrow Book Sequence Diagram

```mermaid
sequenceDiagram
    actor Member
    participant LibraryService
    participant LoanRepository
    participant BookRepository

    Member->>LibraryService: borrowBook(memberId, bookId)

    LibraryService->>LoanRepository: countActiveLoans(memberId)
    LoanRepository-->>LibraryService: active loan count

    LibraryService->>BookRepository: findAvailableCopy(bookId)
    BookRepository-->>LibraryService: available BookCopy

    LibraryService->>BookRepository: mark copy as BORROWED
    LibraryService->>LoanRepository: save new Loan

    LibraryService-->>Member: borrowing successful
```

### Alternative outcomes

Before completing the borrowing process, the service also checks:

- If the member already has 5 active loans, the borrowing request is rejected.
- If no physical copy of the requested book is available, the system reports that the book is unavailable.