package com.yahia.bookstore;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Repository;

@Repository
public class BookRepository {
    private final List<Book> books = new ArrayList<>();
    private long nextId = 1;

    public synchronized Book create(String title, String author, BigDecimal price) {
        Book book = new Book(nextId++, title, author, price);
        books.add(book);
        return book;
    }

    public synchronized Optional<Book> replace(long id, String title, String author, BigDecimal price) {
        for (int index = 0; index < books.size(); index++) {
            if (books.get(index).id() == id) {
                Book replacement = new Book(id, title, author, price);
                books.set(index, replacement);
                return Optional.of(replacement);
            }
        }
        return Optional.empty();
    }

    public synchronized boolean deleteById(long id) {
        return books.removeIf(book -> book.id() == id);
    }

    public synchronized Optional<Book> findById(long id) {
        return books.stream().filter(book -> book.id() == id).findFirst();
    }

    public synchronized List<Book> findAll() {
        return List.copyOf(books);
    }
}
