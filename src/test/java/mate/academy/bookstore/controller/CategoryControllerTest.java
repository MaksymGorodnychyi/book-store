package mate.academy.bookstore.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import mate.academy.bookstore.dto.BookDtoWithoutCategoryIds;
import mate.academy.bookstore.dto.CategoryDto;
import mate.academy.bookstore.dto.CreateCategoryRequestDto;
import mate.academy.bookstore.service.BookService;
import mate.academy.bookstore.service.CategoryService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class CategoryControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private CategoryService categoryService;
    @MockitoBean
    private BookService bookService;

    @Test
    @WithMockUser(roles = "USER")
    void getAll_authenticatedUser_returnsOk() throws Exception {
        CategoryDto dto = new CategoryDto();
        dto.setId(1L);
        dto.setName("Keto");
        Page<CategoryDto> page = new PageImpl<>(List.of(dto));

        when(categoryService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/categories"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].name").value("Keto"));
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteCategory_userRole_returnsForbidden() throws Exception {
        mockMvc.perform(delete("/categories/1"))
                .andExpect(status().isForbidden());
    }

    @Test
    @WithMockUser(roles = "USER")
    void getBooksByCategoryId_authenticatedUser_returnsOk() throws Exception {
        BookDtoWithoutCategoryIds ids = new BookDtoWithoutCategoryIds();
        ids.setTitle("KetoDiet");
        when(bookService.findAllByCategoryId(1L)).thenReturn(List.of(ids));

        mockMvc.perform(get("/categories/1/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].title").value("KetoDiet"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createCategory_adminRole_returnsCreated() throws Exception {
        CreateCategoryRequestDto categoryRequestDto = new CreateCategoryRequestDto();
        CategoryDto categoryDto = new CategoryDto();
        categoryRequestDto.setName("Keto");
        categoryDto.setName("Keto");
        when(categoryService.save(any(CreateCategoryRequestDto.class))).thenReturn(categoryDto);

        mockMvc.perform(post("/categories")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(categoryRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value("Keto"));
    }
}
