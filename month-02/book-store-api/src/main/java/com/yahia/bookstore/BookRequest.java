package com.yahia.bookstore;

import java.math.BigDecimal;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PositiveOrZero;

public record BookRequest(
        @NotBlank String title,
        @NotBlank String author,
        @NotNull @PositiveOrZero BigDecimal price) {
}
