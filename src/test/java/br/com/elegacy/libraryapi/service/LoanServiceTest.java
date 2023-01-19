package br.com.elegacy.libraryapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.elegacy.libraryapi.exception.BusinessException;
import br.com.elegacy.libraryapi.model.entity.Book;
import br.com.elegacy.libraryapi.model.entity.Loan;
import br.com.elegacy.libraryapi.model.repository.LoanRepository;
import br.com.elegacy.libraryapi.service.impl.LoanServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class LoanServiceTest {

	private LoanService loanService;

	@MockBean
	private LoanRepository loanRepository;

	@BeforeEach
	public void setUp() {
		this.loanService = new LoanServiceImpl(loanRepository);
	}

	@Test
	@DisplayName("Should save a loan")
	void shouldSaveLoan() {
		// Arrange
		Book book = Book.builder()
				.id(1L)
				.build();

		String customer = "Jhon";

		Loan savingLoan = Loan.builder()
				.book(book)
				.customer(customer)
				.loanDate(LocalDate.now())
				.build();

		Loan savedLoan = Loan.builder()
				.id(1L)
				.book(book)
				.customer(customer)
				.loanDate(LocalDate.now())
				.build();

		Mockito.when(loanRepository.existsByBookAndNotReturned(book)).thenReturn(false);
		Mockito.when(loanRepository.save(savingLoan)).thenReturn(savedLoan);

		// Act
		Loan loan = loanService.save(savingLoan);

		// Assert
		assertThat(loan.getId()).isEqualTo(savedLoan.getId());
		assertThat(loan.getBook().getId()).isEqualTo(savedLoan.getBook().getId());
		assertThat(loan.getCustomer()).isEqualTo(savedLoan.getCustomer());
		assertThat(loan.getLoanDate()).isEqualTo(savedLoan.getLoanDate());
	}

	@Test
	@DisplayName("Should not save an unreturned book")
	void shouldNotSaveAnUnreturnedBook() {
		// Arrange
		Book book = Book.builder()
				.id(1L)
				.build();

		String customer = "Jhon";

		Loan savingLoan = Loan.builder()
				.book(book)
				.customer(customer)
				.loanDate(LocalDate.now())
				.build();
		
		Mockito.when(loanRepository.existsByBookAndNotReturned(book)).thenReturn(true);

		// Act
		Throwable exception = catchThrowable(() -> loanService.save(savingLoan));

		// Assert
		assertThat(exception)
				.isInstanceOf(BusinessException.class)
				.hasMessage("Book already loaned.");
		
		verify(loanRepository, never()).save(savingLoan);

	}

}
