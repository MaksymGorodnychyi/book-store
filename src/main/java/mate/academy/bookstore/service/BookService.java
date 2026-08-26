package mate.academy.bookstore.service;

import java.util.List;
import mate.academy.bookstore.dto.BookDto;
import mate.academy.bookstore.dto.CreateBookRequestDto;

public interface BookService {
    List<BookDto> findAll();

    BookDto save(CreateBookRequestDto requestDto);

    BookDto findById(Long id);

    void deleteById(Long id);
}
