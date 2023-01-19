package br.com.elegacy.libraryapi.api.resource;

import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.server.ResponseStatusException;

import br.com.elegacy.libraryapi.api.dto.BookDTO;
import br.com.elegacy.libraryapi.model.entity.Book;
import br.com.elegacy.libraryapi.service.BookService;
import jakarta.validation.Valid;

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
	public BookDTO create(@Valid @RequestBody BookDTO bookDTO) {
		Book book = modelMapper.map(bookDTO, Book.class);

		book = bookService.save(book);

		return modelMapper.map(book, BookDTO.class);
	}

	@GetMapping("{id}")
	public BookDTO get(@PathVariable Long id) {
		return bookService
				.getById(id)
				.map(book -> modelMapper.map(book, BookDTO.class))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	public void delete(@PathVariable Long id) {
		Book book = bookService
				.getById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		bookService.delete(book);
	}

	@PutMapping("{id}")
	public BookDTO update(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
		return bookService
				.getById(id).map(book -> {

					book.setAuthor(bookDTO.getAuthor());
					book.setTitle(bookDTO.getTitle());
					book.setIsbn(bookDTO.getIsbn());

					book = bookService.update(book);

					return modelMapper.map(book, BookDTO.class);
				})
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@GetMapping
	public Page<BookDTO> find(BookDTO bookDTO, Pageable pageRequest) {
		Book filter = modelMapper.map(bookDTO, Book.class);
		Page<Book> result = bookService.find(filter, pageRequest);

		List<BookDTO> books = result.getContent()
				.stream()
				.map(entity -> modelMapper.map(entity, BookDTO.class))
				.toList();
		
		return new PageImpl<>(books, pageRequest, result.getTotalElements());
	}
}
