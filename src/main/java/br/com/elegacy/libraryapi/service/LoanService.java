package br.com.elegacy.libraryapi.service;

import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import br.com.elegacy.libraryapi.api.dto.LoanFilterDTO;
import br.com.elegacy.libraryapi.model.entity.Book;
import br.com.elegacy.libraryapi.model.entity.Loan;

public interface LoanService {

	public Loan save(Loan loan);

	public Optional<Loan> getById(Long id);

	public Loan update(Loan loan);

	public Page<Loan> find(LoanFilterDTO loanFilterDTO, Pageable pageable);

	public Page<Loan> getLoansByBook(Book book, Pageable pageable);
	
	public List<Loan> getAllLateLoans();

}
