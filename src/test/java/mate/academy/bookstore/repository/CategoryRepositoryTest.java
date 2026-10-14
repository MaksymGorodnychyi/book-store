package mate.academy.bookstore.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.Optional;
import mate.academy.bookstore.model.Category;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

@DataJpaTest
class CategoryRepositoryTest {
    @Autowired
    private CategoryRepository categoryRepository;

    @Test
    void findById_savedCategory_returnsCategory() {
        Category category = new Category();
        category.setName("Keto");
        Category savedCategory = categoryRepository.save(category);

        Optional<Category> optionalCategory = categoryRepository.findById(savedCategory.getId());

        assertTrue(optionalCategory.isPresent());
        assertEquals("Keto", optionalCategory.get().getName());
    }
}
