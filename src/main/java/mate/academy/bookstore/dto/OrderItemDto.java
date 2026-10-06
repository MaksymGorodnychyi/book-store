package mate.academy.bookstore.dto;

import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class OrderItemDto {
    private Long id;
    private Long bookId;
    private int quantity;
}
