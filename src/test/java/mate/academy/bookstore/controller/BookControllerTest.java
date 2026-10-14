package mate.academy.bookstore.controller;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.fasterxml.jackson.databind.ObjectMapper;
import java.math.BigDecimal;
import java.util.List;
import java.util.Set;
import mate.academy.bookstore.dto.BookDto;
import mate.academy.bookstore.dto.CreateBookRequestDto;
import mate.academy.bookstore.service.BookService;
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
class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;
    @MockitoBean
    private BookService bookService;

    @Test
    @WithMockUser(roles = "USER")
    void getBookById_authenticatedUser_returnsOk() throws Exception {
        BookDto dto = new BookDto();
        dto.setId(1L);
        dto.setTitle("KetoDiet");

        when(bookService.findById(1L)).thenReturn(dto);

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
    void getAll_authenticatedUser_returnsOk() throws Exception {
        BookDto dto = new BookDto();
        dto.setTitle("KetoDiet");
        Page<BookDto> page = new PageImpl<>(List.of(dto));
        when(bookService.findAll(any(Pageable.class))).thenReturn(page);

        mockMvc.perform(get("/books"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content[0].title").value("KetoDiet"));
    }

    @Test
    @WithMockUser(roles = "ADMIN")
    void createBook_adminRole_returnsCreated() throws Exception {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        createBookRequestDto.setTitle("KetoDiet");
        createBookRequestDto.setPrice(BigDecimal.valueOf(100));
        createBookRequestDto.setAuthor("Test Author");
        createBookRequestDto.setCategoryIds(Set.of(1L));
        createBookRequestDto.setIsbn("9781234567897");
        BookDto dto = new BookDto();
        dto.setTitle("KetoDiet");
        when(bookService.save(any(CreateBookRequestDto.class))).thenReturn(dto);

        mockMvc.perform(post("/books")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(createBookRequestDto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.title").value("KetoDiet"));
    }
}
