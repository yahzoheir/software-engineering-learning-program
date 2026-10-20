package com.yahia.bookstore;

import java.util.List;
import java.util.Optional;
import org.springframework.stereotype.Service;

@Service
public class BookService {
    private final BookRepository repository;

    public BookService(BookRepository repository) {
        this.repository = repository;
    }

    public Book create(BookRequest request) {
        return repository.create(request.title(), request.author(), request.price());
    }

    public Optional<Book> replace(long id, BookRequest request) {
        return repository.replace(id, request.title(), request.author(), request.price());
    }

    public boolean deleteById(long id) {
        return repository.deleteById(id);
    }

    public Optional<Book> findById(long id) {
        return repository.findById(id);
    }

    public List<Book> findAll() {
        return repository.findAll();
    }
}
