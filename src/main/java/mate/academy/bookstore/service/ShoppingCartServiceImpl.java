package mate.academy.bookstore.service;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.CreateCartItemRequestDto;
import mate.academy.bookstore.dto.ShoppingCartDto;
import mate.academy.bookstore.dto.UpdateCartItemRequestDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.CartItemMapper;
import mate.academy.bookstore.mapper.ShoppingCartMapper;
import mate.academy.bookstore.model.CartItem;
import mate.academy.bookstore.model.ShoppingCart;
import mate.academy.bookstore.model.User;
import mate.academy.bookstore.repository.BookRepository;
import mate.academy.bookstore.repository.CartItemRepository;
import mate.academy.bookstore.repository.ShoppingCartRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class ShoppingCartServiceImpl implements ShoppingCartService {
    private final ShoppingCartRepository shoppingCartRepository;
    private final ShoppingCartMapper shoppingCartMapper;
    private final BookRepository bookRepository;
    private final CartItemMapper cartItemMapper;
    private final CartItemRepository cartItemRepository;

    @Override
    @Transactional(readOnly = true)
    public ShoppingCartDto getShoppingCart(Long userId) {
        return shoppingCartMapper.toDto(getShoppingCartEntity(userId));
    }

    @Override
    @Transactional
    public ShoppingCartDto addBookToCart(Long userId, CreateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = getShoppingCartEntity(userId);

        if (!bookRepository.existsById(requestDto.getBookId())) {
            throw new EntityNotFoundException("Book not found with id: " + requestDto.getBookId());
        }

        shoppingCart.getCartItems().stream()
                .filter(i -> i.getBook().getId().equals(requestDto.getBookId()))
                .findFirst()
                .ifPresentOrElse(
                        e -> e.setQuantity(
                                e.getQuantity() + requestDto.getQuantity()),
                        () -> {
                            CartItem addCartItem = cartItemMapper.toModel(requestDto);
                            shoppingCart.addCart(addCartItem);
                        });
        shoppingCartRepository.save(shoppingCart);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public ShoppingCartDto updateCartItemQuantity(Long userId,
                                                  Long cartItemId,
                                                  UpdateCartItemRequestDto requestDto) {
        ShoppingCart shoppingCart = getShoppingCartEntity(userId);

        CartItem item = cartItemRepository.findByIdAndShoppingCartId(cartItemId,
                        shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found in"
                        + " your shoppingCart with id: " + cartItemId));

        item.setQuantity(requestDto.getQuantity());
        cartItemRepository.save(item);
        return shoppingCartMapper.toDto(shoppingCart);
    }

    @Override
    @Transactional
    public void createShoppingCart(User user) {
        ShoppingCart shoppingCart = new ShoppingCart();
        shoppingCart.setUser(user);
        shoppingCartRepository.save(shoppingCart);
    }

    @Override
    @Transactional
    public void removeCartItem(Long userId, Long cartItemId) {
        ShoppingCart shoppingCart = getShoppingCartEntity(userId);

        CartItem item = cartItemRepository
                .findByIdAndShoppingCartId(cartItemId, shoppingCart.getId())
                .orElseThrow(() -> new EntityNotFoundException("Item not found in "
                        + "your shoppingCart with id: " + cartItemId));

        shoppingCart.removeCartItem(item);
    }

    private ShoppingCart getShoppingCartEntity(Long userId) {
        return shoppingCartRepository.findByUserId(userId)
                .orElseThrow(() ->
                        new EntityNotFoundException("Cart"
                                + " not found by userId: " + userId));
    }
}
