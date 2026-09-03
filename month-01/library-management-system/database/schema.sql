CREATE TABLE books (
    id INT PRIMARY KEY,
    title VARCHAR(255) NOT NULL,
    author VARCHAR(255) NOT NULL
);

CREATE TABLE book_copies (
    id INT PRIMARY KEY,
    book_id INT NOT NULL,
    status VARCHAR(20) NOT NULL,

    FOREIGN KEY (book_id)
        REFERENCES books(id)
);

CREATE TABLE members (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE librarians (
    id INT PRIMARY KEY,
    name VARCHAR(100) NOT NULL,
    email VARCHAR(255) NOT NULL UNIQUE
);

CREATE TABLE loans (
    id INT PRIMARY KEY,
    member_id INT NOT NULL,
    book_copy_id INT NOT NULL,
    borrowed_at DATE NOT NULL,
    returned_at DATE,

    FOREIGN KEY (member_id)
        REFERENCES members(id),

    FOREIGN KEY (book_copy_id)
        REFERENCES book_copies(id)
);

CREATE INDEX idx_book_copies_book_id
ON book_copies(book_id);

CREATE INDEX idx_loans_member_id
ON loans(member_id);

CREATE INDEX idx_loans_book_copy_id
ON loans(book_copy_id);