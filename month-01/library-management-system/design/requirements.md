# Library Management System - Requirements

## Overview

The Library Management System is a simple system for managing physical books, their individual copies, members, and what can be done with them.

The goal of this exercise is to apply object-oriented design and relational database concepts I learned into  a practical system.

## Users

The system has two user types:

- Member
- Librarian

## Member Capabilities

A member can:

- Search for books
- Borrow a book
- Return a borrowed book
- View their current loans

## Librarian Capabilities

A librarian can:

- Add a new book
- Add physical copies of a book
- Remove physical copies
- View current loans

## Borrowing Rules

- A member can have multiple active loans.
- A member can have at most 5 active loans at the same time.
- A book can have multiple physical copies.
- A member can borrow a book if at least one physical copy is available.

## Database 

The system uses a relational database design compatible with systems such as PostgreSQL or MySQL.