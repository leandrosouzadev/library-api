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
import br.com.elegacy.libraryapi.api.dto.LoanDTO;
import br.com.elegacy.libraryapi.model.entity.Book;
import br.com.elegacy.libraryapi.model.entity.Loan;
import br.com.elegacy.libraryapi.service.BookService;
import br.com.elegacy.libraryapi.service.LoanService;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@RestController
@RequestMapping("/api/books")
@RequiredArgsConstructor
@Tag(name = "Book API")
@Slf4j
public class BookController {

	private final BookService bookService;
	private final LoanService loanService;
	private final ModelMapper modelMapper;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	@Operation(description = "Create a book")
	public BookDTO create(@Valid @RequestBody BookDTO bookDTO) {
		log.info(" creating a book for isbn: {}", bookDTO.getIsbn());
		
		Book book = modelMapper.map(bookDTO, Book.class);

		book = bookService.save(book);

		return modelMapper.map(book, BookDTO.class);
	}

	@GetMapping("{id}")
	@Operation(description = "Obtains a book details by id")
	public BookDTO get(@PathVariable Long id) {
		log.info(" obtaining details for book id: {}", id);
		
		return bookService
				.getById(id)
				.map(book -> modelMapper.map(book, BookDTO.class))
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
	}

	@DeleteMapping("{id}")
	@ResponseStatus(HttpStatus.NO_CONTENT)
	@Operation(description = "Deletes a book by id")
	@ApiResponse(responseCode = "204", description = "Book succesfully deleted")
	@ApiResponse(responseCode = "400", description = "Book not found")
	public void delete(@PathVariable Long id) {
		log.info(" delete book of id: {}", id);
		
		Book book = bookService
				.getById(id)
				.orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));

		bookService.delete(book);
	}

	@PutMapping("{id}")
	@Operation(description = "Updates a book")
	public BookDTO update(@PathVariable Long id, @RequestBody BookDTO bookDTO) {
		log.info(" update book of id: {}", id);
		
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
	@Operation(description = "Find books by params")
	public Page<BookDTO> find(BookDTO bookDTO, Pageable pageRequest) {
		Book filter = modelMapper.map(bookDTO, Book.class);
		Page<Book> result = bookService.find(filter, pageRequest);

		List<BookDTO> books = result.getContent()
				.stream()
				.map(entity -> modelMapper.map(entity, BookDTO.class))
				.toList();

		return new PageImpl<>(books, pageRequest, result.getTotalElements());
	}

	@GetMapping("{id}/loans")
	public Page<LoanDTO> loansByBook(@PathVariable Long id, Pageable pageable) {
		Book book = bookService.getById(id).orElseThrow(() -> new ResponseStatusException(HttpStatus.NOT_FOUND));
		Page<Loan> result = loanService.getLoansByBook(book, pageable);

		List<LoanDTO> list = result.getContent()
				.stream()
				.map(loan -> {
					Book loanBook = loan.getBook();
					BookDTO bookDTO = modelMapper.map(loanBook, BookDTO.class);
					LoanDTO loanDTO = modelMapper.map(loan, LoanDTO.class);
					loanDTO.setBook(bookDTO);
					return loanDTO;
				}).toList();

		return new PageImpl<>(list, pageable, result.getTotalElements());
	}
}
