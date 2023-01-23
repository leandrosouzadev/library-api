package br.com.elegacy.libraryapi.service.impl;

import java.util.Optional;

import org.springframework.stereotype.Service;

import br.com.elegacy.libraryapi.exception.BusinessException;
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
		// TODO Auto-generated method stub
		return Optional.empty();
	}

	@Override
	public Loan update(Loan loan) {
		// TODO Auto-generated method stub
		return null;
	}

}
