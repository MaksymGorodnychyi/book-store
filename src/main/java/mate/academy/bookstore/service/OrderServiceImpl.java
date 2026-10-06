package mate.academy.bookstore.service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.CreateOrderRequestDto;
import mate.academy.bookstore.dto.OrderDto;
import mate.academy.bookstore.dto.OrderItemDto;
import mate.academy.bookstore.dto.UpdateOrderStatusRequestDto;
import mate.academy.bookstore.exception.DataProcessingException;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.OrderItemMapper;
import mate.academy.bookstore.mapper.OrderMapper;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.Order;
import mate.academy.bookstore.model.OrderItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.Status;
import mate.academy.bookstore.repository.OrderItemRepository;
import mate.academy.bookstore.repository.OrderRepository;
import mate.academy.bookstore.repository.ShoppingCartRepository;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class OrderServiceImpl implements OrderService {
    private final OrderRepository orderRepository;
    private final OrderMapper orderMapper;
    private final OrderItemMapper orderItemMapper;
    private final OrderItemRepository orderItemRepository;
    private final ShoppingCartRepository shoppingCartRepository;

    @Override
    @Transactional
    public OrderDto createOrder(Long userId, CreateOrderRequestDto requestDto) {
        ShoppingCart shoppingCart = shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Cart not found by userId: " + userId));
        if (shoppingCart.getCartItems().isEmpty()) {
            throw new DataProcessingException("Can't place an order in empty cart");
        }
        Order order = new Order();
        order.setUser(shoppingCart.getUser());
        order.setStatus(Status.PENDING);
        order.setOrderDate(LocalDateTime.now());
        order.setShippingAddress(requestDto.getShippingAddress());
        BigDecimal total = BigDecimal.ZERO;
        for (CartItem cartItem : shoppingCart.getCartItems()) {
            OrderItem orderItem = new OrderItem();
            orderItem.setBook(cartItem.getBook());
            orderItem.setQuantity(cartItem.getQuantity());
            orderItem.setPrice(cartItem.getBook().getPrice());
            order.addOrderItem(orderItem);

            BigDecimal cartItemCost = cartItem.getBook().getPrice()
                    .multiply(BigDecimal.valueOf(cartItem.getQuantity()));
            total = total.add(cartItemCost);
        }
        order.setTotal(total);

        Order savedOrder = orderRepository.save(order);
        shoppingCart.getCartItems().clear();
        return orderMapper.toDto(savedOrder);
    }

    @Override
    @Transactional(readOnly = true)
    public Page<OrderDto> getOrderHistory(Long userId, Pageable pageable) {
        return orderRepository.findByUserId(userId, pageable)
                .map(orderMapper::toDto);

    }

    @Override
    @Transactional(readOnly = true)
    public List<OrderItemDto> getOrderItems(Long userId, Long orderId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order not found with id: " + orderId));
        return order.getOrderItems().stream()
                .map(orderItemMapper::toDto)
                .toList();
    }

    @Override
    @Transactional(readOnly = true)
    public OrderItemDto getOrderItem(Long userId, Long orderId, Long itemId) {
        Order order = orderRepository.findByIdAndUserId(orderId, userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order not found with id: " + orderId));

        OrderItem item = orderItemRepository.findByIdAndOrderId(itemId, orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Item not found with Id: " + itemId));

        return orderItemMapper.toDto(item);
    }

    @Override
    @Transactional
    public OrderDto updateOrderStatus(Long orderId, UpdateOrderStatusRequestDto requestDto) {
        Order order = orderRepository.findById(orderId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Order not found with id: " + orderId));
        order.setStatus(requestDto.getStatus());
        return orderMapper.toDto(orderRepository.save(order));
    }
}
