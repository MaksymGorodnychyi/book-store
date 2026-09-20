package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.CreateCartItemRequestDto;
import mate.academy.bookstore.dto.ShoppingCartDto;
import mate.academy.bookstore.dto.UpdateCartItemRequestDto;

public interface ShoppingCartService {

    ShoppingCartDto getShoppingCart(Long userId);

    ShoppingCartDto addBookToCart(Long userId, CreateCartItemRequestDto requestDto);

    ShoppingCartDto updateCartItemQuantity(Long userId, Long cartItemId,
                                           UpdateCartItemRequestDto requestDto);

    void removeCartItem(Long userId, Long cartItemId);
}
