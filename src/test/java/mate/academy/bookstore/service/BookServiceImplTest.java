package mate.academy.bookstore.service;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import mate.academy.bookstore.dto.BookDto;
import mate.academy.bookstore.dto.BookSearchParametersDto;
import mate.academy.bookstore.dto.CreateBookRequestDto;
import mate.academy.bookstore.dto.UpdateBookRequestDto;
import mate.academy.bookstore.exception.EntityNotFoundException;
import mate.academy.bookstore.mapper.BookMapper;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.model.Category;
import mate.academy.bookstore.repository.BookRepository;
import mate.academy.bookstore.repository.CategoryRepository;
import mate.academy.bookstore.repository.book.spec.BookSpecificationBuilder;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;

@ExtendWith(MockitoExtension.class)
class BookServiceImplTest {

    @Mock
    private BookRepository bookRepository;
    @Mock
    private BookMapper bookMapper;
    @Mock
    private BookSpecificationBuilder bookSpecificationBuilder;
    @Mock
    private CategoryRepository categoryRepository;

    @InjectMocks
    private BookServiceImpl bookService;

    @Test
    void findById_existingId_returnsDto() {
        Book book = new Book();
        book.setId(1L);
        BookDto expected = new BookDto();
        expected.setId(1L);

        when(bookRepository.findById(1L)).thenReturn(Optional.of(book));
        when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.findById(1L);
        assertEquals(expected, actual);
    }

    @Test
    void findById_missingId_throwsException() {
        when(bookRepository.findById(42L)).thenReturn(Optional.empty());
        EntityNotFoundException ex = assertThrows(EntityNotFoundException.class,
                () -> bookService.findById(42L));

        assertEquals("Can't find book by id: 42", ex.getMessage());
    }

    @Test
    void deleteById_anyId_callsRepositoryOnce() {
        bookService.deleteById(7L);

        verify(bookRepository).deleteById(7L);
    }

    @Test
    void findAll_withPageable_returnsPageOfDtos() {
        Pageable pageable = PageRequest.of(0, 10);
        Book book = new Book();
        BookDto dto = new BookDto();
        Page<Book> bookPage = new PageImpl<>(List.of(book));

        when(bookRepository.findAll(pageable)).thenReturn(bookPage);
        when(bookMapper.toDto(book)).thenReturn(dto);

        Page<BookDto> bookDtoPage = bookService.findAll(pageable);

        assertEquals(1, bookDtoPage.getTotalElements());
        assertEquals(dto, bookDtoPage.getContent().get(0));
    }

    @Test
    void update_existingId_returnsUpdatedDto() {
        Book book = new Book();
        BookDto expected = new BookDto();
        UpdateBookRequestDto requestDto = new UpdateBookRequestDto();

        when(bookRepository.findById(3L)).thenReturn(Optional.of(book));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.update(3L, requestDto);

        assertEquals(expected, actual);
        verify(bookMapper).updateEntityFromDto(book, requestDto);
    }

    @Test
    void update_missingId_neverSaves() {
        when(bookRepository.findById(99L)).thenReturn(Optional.empty());

        assertThrows(EntityNotFoundException.class,
                () -> bookService.update(99L, new UpdateBookRequestDto()));
        verify(bookRepository, never()).save(any());
    }

    @Test
    void save_noCategoryIds_returnsDto() {
        CreateBookRequestDto createBookRequestDto = new CreateBookRequestDto();
        Book book = new Book();
        BookDto expected = new BookDto();

        when(bookMapper.toModel(createBookRequestDto)).thenReturn(book);
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.save(createBookRequestDto);

        assertEquals(expected, actual);
        verifyNoInteractions(categoryRepository);
    }

    @Test
    void save_allCategoriesFound_returnsDto() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setCategoryIds(Set.of(10L, 20L));
        Book book = new Book();
        BookDto expected = new BookDto();

        when(bookMapper.toModel(requestDto)).thenReturn(book);
        when(categoryRepository.findAllById(Set.of(10L, 20L)))
                .thenReturn(List.of(new Category(), new Category()));
        when(bookRepository.save(book)).thenReturn(book);
        when(bookMapper.toDto(book)).thenReturn(expected);

        BookDto actual = bookService.save(requestDto);

        assertEquals(expected, actual);
    }

    @Test
    void save_someCategoriesMissing_throwsAndNeverSaves() {
        CreateBookRequestDto requestDto = new CreateBookRequestDto();
        requestDto.setCategoryIds(Set.of(10L, 20L));

        when(bookMapper.toModel(requestDto)).thenReturn(new Book());
        when(categoryRepository.findAllById(Set.of(10L, 20L)))
                .thenReturn(List.of(new Category()));

        assertThrows(EntityNotFoundException.class,
                () -> bookService.save(requestDto));

        verify(bookRepository, never()).save(any());
    }

    @Test
    void search_validParams_returnsPageOfDtos() {
        BookSearchParametersDto searchParametersDto = new BookSearchParametersDto(
                new String[]{"Kobzar"},
                new String[]{"Shevchenko"});
        Pageable pageable = PageRequest.of(0, 5);
        Book book = new Book();
        BookDto dto = new BookDto();
        @SuppressWarnings("unchecked")
        Specification<Book> specification = mock(Specification.class);
        Page<Book> page = new PageImpl<>(List.of(book));

        when(bookSpecificationBuilder.build(searchParametersDto)).thenReturn(specification);
        when(bookRepository.findAll(specification, pageable)).thenReturn(page);
        when(bookMapper.toDto(book)).thenReturn(dto);

        Page<BookDto> actual = bookService.search(searchParametersDto, pageable);

        assertEquals(1, actual.getTotalElements());
        assertEquals(dto, actual.getContent().get(0));
    }
}
