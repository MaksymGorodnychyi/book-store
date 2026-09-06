package mate.academy.bookstore.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.Getter;
import lombok.Setter;
import mate.academy.bookstore.validation.FieldMatch;

@FieldMatch(field = "password",
        fieldMatch = "repeatPassword",
        message = "passwords must match")
@Getter
@Setter
public class UserRegistrationRequestDto {
    @NotBlank(message = "must not be empty")
    @Email(message = "must be a valid email")
    private String email;
    @NotBlank(message = "must not be empty")
    @Size(min = 8, max = 255, message = "must be between 8 and 255 characters")
    private String password;
    @NotBlank(message = "must not be empty")
    private String repeatPassword;
    @NotBlank(message = "must not be empty")
    private String firstName;
    @NotBlank(message = "must not be empty")
    private String lastName;
    @Size(max = 255, message = "must not exceed 255 characters")
    private String shippingAddress;
}
