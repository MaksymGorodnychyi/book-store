package mate.academy.bookstore.controller;

import static org.springframework.security.test.web.servlet.setup.SecurityMockMvcConfigurers.springSecurity;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.Set;
import mate.academy.bookstore.AbstractIntegrationTest;
import mate.academy.bookstore.dto.CreateBookRequestDto;
import org.junit.jupiter.api.BeforeAll;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.jdbc.Sql;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;
import org.springframework.web.context.WebApplicationContext;

class BookControllerTest extends AbstractIntegrationTest {
    protected static MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @BeforeAll
    static void beforeAll(@Autowired WebApplicationContext webApplicationContext) {
        mockMvc = MockMvcBuilders
                .webAppContextSetup(webApplicationContext)
                .apply(springSecurity())
                .build();
    }

    @Test
    @WithMockUser(roles = "USER")
    @Sql(scripts = "classpath:database/books/add-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getBookById_authenticatedUser_returnsOk() throws Exception {
        mockMvc.perform(get("/books/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.title").value("KetoDiet"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void delete_userRole_returnsForbidden() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void delete_adminRole_returnsNoContent() throws Exception {
        mockMvc.perform(delete("/books/1"))
                .andExpect(status().isNoContent());
    }

    @Test
    @WithMockUser(roles = "USER")
    @Sql(scripts = "classpath:database/books/add-books.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-books.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void getAll_authenticatedUser_returnsOk() throws Exception {
        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("KetoDiet"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    @Sql(scripts = "classpath:database/books/add-category.sql",
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD)
    @Sql(scripts = "classpath:database/books/remove-category.sql",
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD)
    void createBook_adminRole_returnsCreated() throws Exception {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("KetoDiet");
        createBookRequestDto.setPrice(BigDecimal.valueOf(100));
        createBookRequestDto.setAuthor("Test Author");
        createBookRequestDto.setCategoryIds(Set.of(1L));
        createBookRequestDto.setIsbn("9781234567897");

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("KetoDiet"));
    }
}
