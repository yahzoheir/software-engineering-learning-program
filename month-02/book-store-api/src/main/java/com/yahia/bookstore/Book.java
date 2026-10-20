package com.yahia.bookstore;

import java.math.BigDecimal;

public record Book(long id, String title, String author, BigDecimal price) {
}
