package mate.academy.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import java.math.BigDecimal;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateBookRequestDto {
    @NotBlank(message = "must not be empty")
    private String title;
    @NotBlank(message = "must not be empty")
    private String author;
    @NotBlank(message = "must not be empty")
    @Size(min = 13, max = 13, message = "must be exactly 13 characters")
    private String isbn;
    @NotNull(message = "is required")
    @Positive(message = "must be greater than zero")
    private BigDecimal price;
    private String description;
    private String coverImage;
}
