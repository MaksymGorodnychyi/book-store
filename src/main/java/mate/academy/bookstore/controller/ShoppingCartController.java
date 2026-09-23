package mate.academy.bookstore.controller;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.CreateCartItemRequestDto;
import mate.academy.bookstore.dto.ShoppingCartDto;
import mate.academy.bookstore.dto.UpdateCartItemRequestDto;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.service.ShoppingCartService;
import org.springframework.http.HttpStatus;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.core.Authentication;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

@Tag(name = "Shopping cart management", description = "Endpoints for managing cart")
@RestController
@RequiredArgsConstructor
@RequestMapping("/cart")
public class ShoppingCartController {
    private final ShoppingCartService shoppingCartService;

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Get shoppingCart",
            description = "Get a shoppingCart of the authentication user")
    @GetMapping
    public ShoppingCartDto getShoppingCart(Authentication authentication) {
        return shoppingCartService.getShoppingCart(getUserId(authentication));
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Add a book",
            description = "Add a book to the my Cart")
    @PostMapping
    public ShoppingCartDto addBookToCart(Authentication authentication,
                                   @RequestBody @Valid CreateCartItemRequestDto requestDto) {
        return shoppingCartService.addBookToCart(getUserId(authentication), requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Update a book items",
            description = "Update an item of the book in my cart")
    @PutMapping("/cart-items/{cartItemId}")
    public ShoppingCartDto updateCartItemQuantity(Authentication authentication,
                                       @PathVariable Long cartItemId,
                                       @RequestBody @Valid UpdateCartItemRequestDto requestDto) {
        return shoppingCartService.updateCartItemQuantity(getUserId(authentication),
                cartItemId, requestDto);
    }

    @PreAuthorize("hasRole('USER')")
    @Operation(summary = "Remove a book",
            description = "Remove a book from the shoppingCart")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    @DeleteMapping("/cart-items/{cartItemId}")
    public void removeCartItem(Authentication authentication,
                           @PathVariable Long cartItemId) {
        shoppingCartService.removeCartItem(getUserId(authentication), cartItemId);
    }

    private Long getUserId(Authentication authentication) {
        User user = (User) authentication.getPrincipal();
        return user.getId();
    }
}
