package mate.academy.bookstore;

import java.math.BigDecimal;
import mate.academy.bookstore.model.Book;
import mate.academy.bookstore.service.BookService;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class BookStoreApplication {

    public static void main(String[] args) {
        SpringApplication.run(BookStoreApplication.class, args);
    }

    @Bean
    public CommandLineRunner commandLineRunner(BookService bookService) {
        return args -> {
            Book book = new Book();
            book.setIsbn("978-3-16-148410-0");
            book.setPrice(BigDecimal.valueOf(30));
            book.setTitle("MediterraneanCookBook");
            book.setAuthor("Jenny");

            bookService.save(book);
            System.out.println(bookService.findAll());
        };
    }
}
