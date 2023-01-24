package br.com.elegacy.libraryapi.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.time.LocalDate;
import java.util.List;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;

import br.com.elegacy.libraryapi.model.entity.Book;
import br.com.elegacy.libraryapi.model.entity.Loan;

@ActiveProfiles("test")
@DataJpaTest
class LoanRepositoryTest {

	@Autowired
	private LoanRepository loanRepository;

	@Autowired
	private TestEntityManager testEntityManager;

	@Test
	@DisplayName("Should check if there is an unreturned loan for the book")
	void shouldCheckUnreturnedLoanBook() {
		// Arrange
		Loan loan = createAndPersistLoan(LocalDate.now());
		Book book = loan.getBook();

		// Act
		boolean exists = loanRepository.existsByBookAndNotReturned(book);

		// Assert
		assertThat(exists).isTrue();
	}

	@Test
	@DisplayName("Should find loan by book isbn or customer.")
	void shouldFindLoanByBookIsbnOrCustomer() {
		// Arrange
		Loan loan = createAndPersistLoan(LocalDate.now());

		// Act
		Page<Loan> result = loanRepository.findByBookIsbnOrCustomer("123", "Jhon", PageRequest.of(0, 10));

		// Assert
		assertThat(result.getContent()).hasSize(1);
		assertThat(result.getContent()).contains(loan);
		assertThat(result.getPageable().getPageSize()).isEqualTo(10);
		assertThat(result.getPageable().getPageNumber()).isZero();
		assertThat(result.getTotalElements()).isEqualTo(1);
	}
	
	@Test
	@DisplayName("Should find by loan date less than thre days ago and not returned")
	void ShouldfindByLoanDateLessThanAndNotReturned() {
		// Arrange
		Loan loan = createAndPersistLoan(LocalDate.now().minusDays(5));
		
		// Act
		List<Loan> result = loanRepository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));
		
		// Assert
		assertThat(result).hasSize(1).contains(loan);
	}
	
	@Test
	@DisplayName("Should not find by loan date less than thre days ago and not returned")
	void ShouldNotfindByLoanDateLessThanAndNotReturned() {
		// Arrange
		createAndPersistLoan(LocalDate.now());
		
		// Act
		List<Loan> result = loanRepository.findByLoanDateLessThanAndNotReturned(LocalDate.now().minusDays(4));
		
		// Assert
		assertThat(result).isEmpty();
	}

	private Loan createAndPersistLoan(LocalDate loanDate) {
		Book book = Book.builder()
				.title("Adventures")
				.author("Arthur")
				.isbn("123")
				.build();

		Loan loan = Loan.builder()
				.book(book)
				.customer("Jhon")
				.loanDate(loanDate)
				.build();

		testEntityManager.persist(book);
		testEntityManager.persist(loan);

		return loan;
	}
}
