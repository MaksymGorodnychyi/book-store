package mate.academy.bookstore.dto;

import jakarta.validation.constraints.NotNull;
import lombok.Getter;
import lombok.Setter;
import mate.academy.bookstore.model.Status;

@Getter
@Setter
public class UpdateOrderStatusRequestDto {
    @NotNull(message = "must not be empty")
    private Status status;
}
