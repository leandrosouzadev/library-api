package br.com.elegacy.libraryapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.elegacy.libraryapi.model.entity.Book;
import br.com.elegacy.libraryapi.model.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long>{

	public boolean existsByBookAndNotReturned(Book book);

}
