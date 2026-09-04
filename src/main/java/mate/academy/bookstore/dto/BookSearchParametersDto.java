package mate.academy.bookstore.dto;

import jakarta.validation.constraints.NotBlank;

public record BookSearchParametersDto(
        String @NotBlank(message = "must not be empty") [] titles,
        String @NotBlank(message = "must not be empty") [] authors) {
}
