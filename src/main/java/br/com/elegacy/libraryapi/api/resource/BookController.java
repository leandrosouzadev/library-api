package br.com.elegacy.libraryapi.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.elegacy.libraryapi.api.resource.dto.BookDTO;
import br.com.elegacy.libraryapi.model.entity.Book;
import br.com.elegacy.libraryapi.service.BookService;

@RestController
@RequestMapping("/api/books")
public class BookController {

	private BookService bookService;

	public BookController(BookService bookService) {
		this.bookService = bookService;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO create(@RequestBody BookDTO bookDTO) {
		Book book = Book.builder()
				.author(bookDTO.getAuthor())
				.title(bookDTO.getTitle())
				.isbn(bookDTO.getIsbn())
				.build();

		book = bookService.save(book);
		
		return BookDTO.builder()
				.id(book.getId())
				.author(book.getAuthor())
				.title(book.getTitle())
				.isbn(book.getIsbn())
				.build();
	}
}
