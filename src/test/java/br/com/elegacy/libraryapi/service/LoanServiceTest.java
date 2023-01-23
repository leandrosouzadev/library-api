package br.com.elegacy.libraryapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.catchThrowable;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;

import java.time.LocalDate;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.elegacy.libraryapi.api.dto.LoanFilterDTO;
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

	@Test
	@DisplayName("Should get loan information by id")
	void shouldGetLoanInformationById() {
		// Arrange
		Long id = 1L;

		Loan loan = createLoan();
		loan.setId(id);

		Mockito.when(loanRepository.findById(id)).thenReturn(Optional.of(loan));

		// Act
		Optional<Loan> result = loanService.getById(id);

		// Assert
		assertThat(result).isPresent();
		assertThat(result.get().getId()).isEqualTo(id);
		assertThat(result.get().getCustomer()).isEqualTo(loan.getCustomer());
		assertThat(result.get().getBook()).isEqualTo(loan.getBook());
		assertThat(result.get().getLoanDate()).isEqualTo(loan.getLoanDate());

		verify(loanRepository).findById(id);
	}

	@Test
	@DisplayName("Should update a loan")
	void shouldUpdateLoan() {
		// Arrange
		Long id = 1L;

		Loan loan = createLoan();
		loan.setId(id);
		loan.setReturned(true);

		Mockito.when(loanRepository.save(loan)).thenReturn(loan);

		// Act
		Loan updatedLoan = loanService.update(loan);

		// Assert
		assertThat(updatedLoan.getReturned()).isTrue();
		verify(loanRepository).save(loan);
	}

	@Test
	@DisplayName("Should find loans by filter")
	void shouldFindLoansByFilter() {
		// Arrange
		LoanFilterDTO loanFilterDTO = LoanFilterDTO.builder().customer("Jhon").isbn("321").build();

		Loan loan = createLoan();
		loan.setId(1L);

		PageRequest pageRequest = PageRequest.of(0, 10);
		List<Loan> loans = Arrays.asList(loan);

		Page<Loan> page = new PageImpl<Loan>(loans, pageRequest, loans.size());
		Mockito.when(loanRepository.findByBookIsbnOrCustomer(
				Mockito.anyString(),
				Mockito.anyString(),
				Mockito.any(PageRequest.class)))
				.thenReturn(page);

		// Act
		Page<Loan> result = loanService.find(loanFilterDTO, pageRequest);

		// Assert
		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getContent()).isEqualTo(loans);
		assertThat(result.getPageable().getPageNumber()).isZero();
		assertThat(result.getPageable().getPageSize()).isEqualTo(10);
	}

	private Loan createLoan() {
		Book book = Book.builder()
				.id(1L)
				.build();

		String customer = "Jhon";

		Loan savingLoan = Loan.builder()
				.book(book)
				.customer(customer)
				.loanDate(LocalDate.now())
				.build();

		return savingLoan;
	}
}
