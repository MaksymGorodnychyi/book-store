package mate.academy.bookstore.mapper;

import mate.academy.bookstore.dto.OrderDto;
import mate.academy.bookstore.model.Order;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

@Mapper(componentModel = "spring", uses = OrderItemMapper.class)
public interface OrderMapper {

    @Mapping(source = "user.id", target = "userId")
    OrderDto toDto(Order order);
}
