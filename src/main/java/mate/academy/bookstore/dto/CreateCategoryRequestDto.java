package mate.academy.bookstore.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class CreateCategoryRequestDto {

    @NotBlank(message = "must not be empty")
    @Size(max = 255)
    private String name;
    private String description;
}
