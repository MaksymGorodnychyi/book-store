package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.CreateOrderRequestDto;
import mate.academy.bookstore.dto.OrderDto;
import mate.academy.bookstore.dto.OrderItemDto;
import mate.academy.bookstore.dto.UpdateOrderStatusRequestDto;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.service.OrderService;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Order management", description = "Endpoints for managing orders")
@RestController
@RequiredArgsConstructor
@RequestMapping("/orders")
public class OrderController {
    private final OrderService orderService;

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Create an order",
            description = "Creates an order from the user's shopping cart")
    @ResponseStatus(HttpStatus.CREATED)
    @PostMapping
    public OrderDto createOrder(Authentication authentication,
                                @RequestBody @Valid CreateOrderRequestDto requestDto) {
        return orderService.createOrder(getUserId(authentication), requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order history",
            description = "Get a paginated list of the history of orders")
    @GetMapping
    public Page<OrderDto> getOrderHistory(Authentication authentication,
                                          Pageable pageable) {
        return orderService.getOrderHistory(getUserId(authentication), pageable);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order items",
            description = "Get all items of the order")
    @GetMapping("/{orderId}/items")
    public List<OrderItemDto> getOrderItems(Authentication authentication,
                                            @PathVariable Long orderId) {
        return orderService.getOrderItems(getUserId(authentication), orderId);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get order item",
            description = "Get a specific item of order")
    @GetMapping("/{orderId}/items/{itemId}")
    public OrderItemDto getOrderItem(Authentication authentication,
                                     @PathVariable Long orderId,
                                     @PathVariable Long itemId) {
        return orderService.getOrderItem(getUserId(authentication), orderId, itemId);
    }

    @PreAuthorize("hasRole('ADMIN')")
    @Operation(summary = "Update order status",
            description = "Change the status of order")
    @PatchMapping("/{id}")
    public OrderDto updateOrderStatus(@PathVariable Long id,
                                      @RequestBody
                                      @Valid UpdateOrderStatusRequestDto requestDto) {
        return orderService.updateOrderStatus(id, requestDto);
    }

    private Long getUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
