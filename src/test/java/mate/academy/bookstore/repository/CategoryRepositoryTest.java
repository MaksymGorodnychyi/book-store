package mate.academy.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.util.TestUtil;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findById_SavedCategory_ShouldReturnCategory() {
        Category savedCategory = categoryRepository.save(TestUtil.createCategory(null, "Keto"));

        Optional<Category> optionalCategory = categoryRepository.findById(savedCategory.getId());

        assertTrue(optionalCategory.isPresent());
        assertEquals("Keto", optionalCategory.get().getName());
    }
}
