package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.CreateCartItemRequestDto;
import mate.academy.bookstore.dto.ShoppingCartDto;
import mate.academy.bookstore.dto.UpdateCartItemRequestDto;
import mate.academy.bookstore.model.User;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCart(Long userId);

    ShoppingCartDto addBookToCart(Long userId, CreateCartItemRequestDto requestDto);

    ShoppingCartDto updateCartItemQuantity(Long userId, Long cartItemId,
                                           UpdateCartItemRequestDto requestDto);

    void createShoppingCart(User user);

    void removeCartItem(Long userId, Long cartItemId);
}
