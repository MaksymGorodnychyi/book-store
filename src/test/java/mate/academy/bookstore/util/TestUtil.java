package mate.academy.bookstore.util;

import java.math.BigDecimal;
import mate.academy.bookstore.dto.BookDto;
import mate.academy.bookstore.dto.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.dto.CategoryDto;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;

public final class TestUtil {
    private TestUtil() {
    }

    public static Book createBook(Long id, String title) {
        Book book = new Book();
        book.setId(id);
        book.setTitle(title);
        book.setAuthor("Test Author");
        book.setIsbn("9781234567897");
        book.setPrice(BigDecimal.valueOf(100));
        return book;
    }

    public static BookDto createBookDto(Book book) {
        BookDto bookDto = new BookDto();
        bookDto.setId(book.getId());
        bookDto.setTitle(book.getTitle());
        return bookDto;
    }

    public static Category createCategory(Long id, String name) {
        Category category = new Category();
        category.setId(id);
        category.setName(name);
        return category;
    }

    public static BookDtoWithoutCategoryIds createBookDtoWithoutCategoryIds(Book book) {
        BookDtoWithoutCategoryIds dto = new BookDtoWithoutCategoryIds();
        dto.setId(book.getId());
        dto.setTitle(book.getTitle());
        return dto;
    }

    public static CategoryDto createCategoryDto(Category category) {
        CategoryDto dto = new CategoryDto();
        dto.setId(category.getId());
        dto.setName(category.getName());
        return dto;
    }
}
