-- Find available physical copies of a book
SELECT *
FROM book_copies
WHERE book_id = 1
  AND status = 'AVAILABLE';


-- View a member's current loans
SELECT
    l.id,
    b.title,
    bc.id AS copy_id,
    l.borrowed_at
FROM loans l
JOIN book_copies bc
    ON l.book_copy_id = bc.id
JOIN books b
    ON bc.book_id = b.id
WHERE l.member_id = 1
  AND l.returned_at IS NULL;


-- Count a member's active loans
SELECT COUNT(*) AS active_loans
FROM loans
WHERE member_id = 1
  AND returned_at IS NULL;


-- View all active loans
SELECT
    l.id,
    m.name AS member_name,
    b.title,
    bc.id AS copy_id,
    l.borrowed_at
FROM loans l
JOIN members m
    ON l.member_id = m.id
JOIN book_copies bc
    ON l.book_copy_id = bc.id
JOIN books b
    ON bc.book_id = b.id
WHERE l.returned_at IS NULL;


-- view the loan history of a copy of a book
SELECT
    l.id,
    m.name AS member_name,
    l.borrowed_at,
    l.returned_at
FROM loans l
JOIN members m
    ON l.member_id = m.id
WHERE l.book_copy_id = 1
ORDER BY l.borrowed_at DESC;