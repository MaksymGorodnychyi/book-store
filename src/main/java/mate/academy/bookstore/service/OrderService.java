package mate.academy.bookstore.service;

import java.util.List;
import mate.academy.bookstore.dto.CreateOrderRequestDto;
import mate.academy.bookstore.dto.OrderDto;
import mate.academy.bookstore.dto.OrderItemDto;
import mate.academy.bookstore.dto.UpdateOrderStatusRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface OrderService {

    OrderDto createOrder(Long userId, CreateOrderRequestDto requestDto);

    Page<OrderDto> getOrderHistory(Long userId, Pageable pageable);

    List<OrderItemDto> getOrderItems(Long userId, Long orderId);

    OrderItemDto getOrderItem(Long userId, Long orderId, Long itemId);

    OrderDto updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto requestDto);
}
