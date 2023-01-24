package br.com.elegacy.libraryapi.service.impl;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.elegacy.libraryapi.api.dto.LoanFilterDTO;
import br.com.elegacy.libraryapi.exception.BusinessException;
import br.com.elegacy.libraryapi.model.entity.Book;
import br.com.elegacy.libraryapi.model.entity.Loan;
import br.com.elegacy.libraryapi.model.repository.LoanRepository;
import br.com.elegacy.libraryapi.service.LoanService;

@Service
public class LoanServiceImpl implements LoanService {

	private final LoanRepository loanRepository;

	public LoanServiceImpl(LoanRepository loanRepository) {
		this.loanRepository = loanRepository;
	}

	@Override
	public Loan save(Loan loan) {
		if (loanRepository.existsByBookAndNotReturned(loan.getBook())) {
			throw new BusinessException("Book already loaned.");
		}

		return this.loanRepository.save(loan);
	}

	@Override
	public Optional<Loan> getById(Long id) {
		return this.loanRepository.findById(id);
	}

	@Override
	public Loan update(Loan loan) {
		return this.loanRepository.save(loan);
	}

	@Override
	public Page<Loan> find(LoanFilterDTO loanFilterDTO, Pageable pageable) {
		return this.loanRepository.findByBookIsbnOrCustomer(loanFilterDTO.getIsbn(), loanFilterDTO.getCustomer(),
				pageable);
	}

	@Override
	public Page<Loan> getLoansByBook(Book book, Pageable pageable) {
		return this.loanRepository.findByBook(book, pageable);
	}

	@Override
	public List<Loan> getAllLateLoans() {
		final Integer loanDays = 4;
		LocalDate threDaysAgo = LocalDate.now().minusDays(loanDays);
		return this.loanRepository.findByLoanDateLessThanAndNotReturned(threDaysAgo);
	}

}
