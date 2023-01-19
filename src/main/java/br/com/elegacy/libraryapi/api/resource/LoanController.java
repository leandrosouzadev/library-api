package br.com.elegacy.libraryapi.api.resource;

import java.time.LocalDate;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.elegacy.libraryapi.api.dto.LoanDTO;
import br.com.elegacy.libraryapi.model.entity.Book;
import br.com.elegacy.libraryapi.model.entity.Loan;
import br.com.elegacy.libraryapi.service.BookService;
import br.com.elegacy.libraryapi.service.LoanService;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/loans")
@RequiredArgsConstructor
public class LoanController {
	
	private final LoanService loanService;
	private final BookService bookService;

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public long create(@RequestBody LoanDTO loanDTO) {
		Book book = bookService.getBookByIsbn(loanDTO.getIsbn()).get();
		
		Loan entity = Loan.builder()
				.book(book)
				.customer(loanDTO.getCustomer())
				.loanDate(LocalDate.now())
				.build();
		
		entity = loanService.save(entity);
		
		return entity.getId();
	}
}
