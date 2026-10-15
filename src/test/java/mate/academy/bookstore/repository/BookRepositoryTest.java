package mate.academy.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.List;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.util.TestUtil;
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
    void findAllByCategoryId_ExistingCategory_ShouldReturnBooks() {
        Category savedCategory = categoryRepository.save(TestUtil.createCategory(null, "Keto"));

        Book book = TestUtil.createBook(null, "KetoDiet");
        book.getCategories().add(savedCategory);
        bookRepository.saveAndFlush(book);
        List<Book> bookList = bookRepository.findAllByCategoryId(savedCategory.getId());

        assertEquals(1, bookList.size());
        assertEquals("KetoDiet", bookList.get(0).getTitle());
    }

    @Test
    void findAllByCategoryId_OtherCategory_ShouldReturnEmptyList() {
        Category savedKeto = categoryRepository.save(TestUtil.createCategory(null, "Keto"));

        Book book = TestUtil.createBook(null, "Diet");
        book.getCategories().add(savedKeto);
        bookRepository.saveAndFlush(book);

        Category savedMediterranean = categoryRepository.save(TestUtil
                .createCategory(null, "Mediterranean"));
        List<Book> actual = bookRepository.findAllByCategoryId(savedMediterranean.getId());

        assertTrue(actual.isEmpty());
    }
}
