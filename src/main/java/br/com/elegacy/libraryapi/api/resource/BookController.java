package br.com.elegacy.libraryapi.api.resource;

import org.modelmapper.ModelMapper;
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
	private ModelMapper modelMapper;

	public BookController(BookService bookService, ModelMapper modelMapper) {
		this.bookService = bookService;
		this.modelMapper = modelMapper;
	}

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO create(@RequestBody BookDTO bookDTO) {
		Book book = modelMapper.map(bookDTO, Book.class);

		book = bookService.save(book);
		
		return modelMapper.map(book, BookDTO.class);
	}
}
