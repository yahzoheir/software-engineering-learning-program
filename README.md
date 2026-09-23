# Software Engineering Learning Program

This repository contains the practical work I complete throughout my software engineering learning program.

The goal is to apply the concepts I study through small system-design exercises, database design, and implementation work rather than keeping everything purely theoretical.

Each month is organized separately so the work can be reviewed and navigated easily.

---

## Quick Navigation

### Month 1: Library Management System

- [Requirements](./month-01/library-management-system/design/requirements.md)
- [Design Notes](./month-01/library-management-system/design/design-notes.md)
- [UML and Sequence Diagrams](./month-01/library-management-system/design/diagram.md)
- [Database Relationship Diagram](./month-01/library-management-system/database/er-diagram.md)
- [Database Schema](./month-01/library-management-system/database/schema.sql)
- [Example SQL Queries](./month-01/library-management-system/database/example-queries.sql)

### Month 2: TDD Practice

- [Book Pricing Exercise](./month-02/testing/)

### Month 2: Movie Rental System

- [README](./month-02/movie-rental-system/README.md)
- [Requirements](./month-02/movie-rental-system/docs/requirements.md)
- [Design Notes](./month-02/movie-rental-system/docs/design.md)
- [Testing Notes](./month-02/movie-rental-system/docs/testing.md)

---

## Month 1 - OOP, System Design, and Databases

For Month 1, I started by designing a Library Management System.

The focus of this work is applying concepts such as:

- classes and objects
- encapsulation
- abstraction
- inheritance and composition
- polymorphism
- SOLID principles
- interfaces
- service and repository responsibilities
- relational database design
- primary and foreign keys
- joins
- normalization
- indexing

I plan to use the feedback from this first system before moving on to the next design exercise.

---

## Library Management System

This project includes:

- system requirements
- design notes and reasoning
- UML class diagram
- borrowing sequence diagram
- database relationship diagram
- SQL schema
- indexing decisions
- example SQL queries

---

## Month 2: Unit Testing and TDD

For Month 2, I'm practicing test-driven development and unit testing in Java with JUnit 5, then applying those skills to a second system-design exercise: a Movie Rental System.

The focus of this work is applying concepts such as:

- test-driven development (red, green, refactor)
- JUnit 5
- unit testing and edge cases
- input validation
- encapsulation
- Maven project setup

---

## Book Pricing TDD Exercise

[Open Exercise](./month-02/testing/)

A small TDD kata used to practice the red, green, refactor cycle with JUnit 5, covering discount calculation and its edge cases.

---

## Movie Rental System

[Open Movie Rental System](./month-02/movie-rental-system/README.md)

This project currently includes:

- system requirements
- design notes and reasoning
- Maven project configured with JUnit 5
- a `Movie` class with input validation and unit tests

This project is in progress. Customers, rentals, and returns are not yet implemented.

---

## Repository Structure

```text
software-engineering-learning-program/
│
├── README.md
│
├── month-01/
│   └── library-management-system/
│       ├── design/
│       │   ├── requirements.md
│       │   ├── design-notes.md
│       │   └── diagram.md
│       │
│       ├── database/
│       │   ├── schema.sql
│       │   ├── er-diagram.md
│       │   └── example-queries.sql
│       │
│       └── src/
│
└── month-02/
    ├── testing/
    │   ├── pom.xml
    │   └── src/
    │       ├── main/java/BookPricing.java
    │       └── test/java/BookPricingTest.java
    │
    └── movie-rental-system/
        ├── README.md
        ├── pom.xml
        ├── docs/
        │   ├── requirements.md
        │   ├── design.md
        │   └── testing.md
        │
        └── src/
            ├── main/java/
            │   ├── Movie.java
            │   └── Rental.java
            └── test/java/
                └── MovieTest.java
```
