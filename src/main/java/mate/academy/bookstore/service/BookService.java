package mate.academy.bookstore.service;

import mate.academy.bookstore.dto.BookDto;
import mate.academy.bookstore.dto.BookSearchParametersDto;
import mate.academy.bookstore.dto.CreateBookRequestDto;
import mate.academy.bookstore.dto.UpdateBookRequestDto;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

public interface BookService {
    Page<BookDto> findAll(Pageable pageable);

    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    void deleteById(Long id);

    BookDto update(Long id, UpdateBookRequestDto requestDto);

    Page<BookDto> search(BookSearchParametersDto parametersDto, Pageable pageable);
}
