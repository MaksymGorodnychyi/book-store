package mate.academy.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.math.BigDecimal;
import java.util.List;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class BookRepositoryTest {
    @Autowired
    private BookRepository bookRepository;
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findAllByCategoryId_existingCategory_returnsBooks() {
        Category category = new Category();
        category.setName("Keto");
        Category savedCategory = categoryRepository.save(category);

        Book book = new Book();
        book.getCategories().add(savedCategory);
        book.setTitle("KetoDiet");
        book.setIsbn("9781234567897");
        book.setAuthor("Test Author");
        book.setPrice(BigDecimal.valueOf(100));
        bookRepository.saveAndFlush(book);

        List<Book> bookList = bookRepository.findAllByCategoryId(savedCategory.getId());

        assertEquals(1, bookList.size());
        assertEquals("KetoDiet", bookList.get(0).getTitle());
    }

    @Test
    void findAllByCategoryId_otherCategory_returnsEmptyList() {
        Category keto = new Category();
        keto.setName("Keto");
        Category savedCategory = categoryRepository.save(keto);

        Book book = new Book();
        book.setTitle("Diets");
        book.getCategories().add(savedCategory);
        book.setIsbn("9781234567897");
        book.setAuthor("Test Author");
        book.setPrice(BigDecimal.valueOf(100));
        bookRepository.saveAndFlush(book);

        Category mediterranean = new Category();
        mediterranean.setName("Mediterranean");
        Category savedMediterranean = categoryRepository.save(mediterranean);

        List<Book> actual = bookRepository.findAllByCategoryId(savedMediterranean.getId());

        assertTrue(actual.isEmpty());
    }
}
