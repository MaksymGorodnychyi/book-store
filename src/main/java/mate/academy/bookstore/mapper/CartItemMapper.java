package mate.academy.bookstore.mapper;

import mate.academy.bookstore.dto.CartItemDto;
import mate.academy.bookstore.dto.CreateCartItemRequestDto;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.CartItem;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;

@Mapper(componentModel = "spring")
public interface CartItemMapper {

    @Mapping(source = "book.id", target = "bookId")
    @Mapping(source = "book.title", target = "bookTitle")
    CartItemDto toDto(CartItem cartItem);

    @Mapping(source = "bookId", target = "book", qualifiedByName = "bookFromId")
    @Mapping(target = "id", ignore = true)
    @Mapping(target = "shoppingCart", ignore = true)
    CartItem toModel(CreateCartItemRequestDto requestDto);

    @Named("bookFromId")
    default Book getBookById(Long bookId) {
        Book book = new Book();
        book.setId(bookId);
        return book;
    }
}
