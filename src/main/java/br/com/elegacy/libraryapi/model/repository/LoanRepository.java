package br.com.elegacy.libraryapi.model.repository;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import br.com.elegacy.libraryapi.model.entity.Book;
import br.com.elegacy.libraryapi.model.entity.Loan;

public interface LoanRepository extends JpaRepository<Loan, Long>{

	@Query(value = " select case when (count(l.id) > 0) then true else false end "
			+ " from Loan l where l.book = :book and (l.returned is null or not(l.returned))")
	public boolean existsByBookAndNotReturned(@Param("book") Book book);

	public Page<Loan> findByBookIsbnOrCustomer(String isbn, String customer, Pageable pageRequest);

}
