package mate.academy.bookstore.repository.book.spec;

import lombok.RequiredArgsConstructor;
import mate.academy.bookstore.dto.BookSearchParametersDto;
import mate.academy.bookstore.model.Book;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Component;

@RequiredArgsConstructor
@Component
public class BookSpecificationBuilder {
    private final SpecificationProviderManager<Book> specificationProviderManager;

    public Specification<Book> build(BookSearchParametersDto params) {
        Specification<Book> spec = Specification.unrestricted();

        if (params.titles() != null && params.titles().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("title")
                    .getSpecification(params.titles()));
        }

        if (params.authors() != null && params.authors().length > 0) {
            spec = spec.and(specificationProviderManager
                    .getSpecificationProvider("author")
                    .getSpecification(params.authors()));
        }

        return spec;
    }
}
