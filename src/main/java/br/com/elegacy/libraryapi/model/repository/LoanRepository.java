package br.com.elegacy.libraryapi.model.repository;

import java.time.LocalDate;
import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.elegacy.libraryapi.model.entity.Book;
import br.com.elegacy.libraryapi.model.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long> {

	@Query(value = "select case when (count(l.id) > 0) then true else false end "
			+ " from Loan l where l.book = :book and (l.returned is null or not(l.returned))")
	public boolean existsByBookAndNotReturned(@Param("book") Book book);

	@Query(value = "select l from Loan as l join l.book as b where b.isbn = :isbn or l.customer = :customer")
	public Page<Loan> findByBookIsbnOrCustomer(@Param("isbn") String isbn, @Param("customer") String customer,
			Pageable pageable);

	public Page<Loan> findByBook(Book book, Pageable pageable);

	@Query(value = "select l from Loan l where l.loanDate <= :threDaysAgo and (l.returned is null or not(l.returned))")
	public List<Loan> findByLoanDateLessThanAndNotReturned(@Param("threDaysAgo") LocalDate threDaysAgo);

}
