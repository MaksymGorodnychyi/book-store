package mate.academy.bookstore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class UserLoginRequestDto {
    @NotBlank(message = "must not be empty")
    @Email(message = "must be a valid email")
    private String email;
    @NotBlank(message = "must not be empty")
    @Size(min = 8, max = 255, message = "must be between 8 and 255 characters")
    private String password;
}
