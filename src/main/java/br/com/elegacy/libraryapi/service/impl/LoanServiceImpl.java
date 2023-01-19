package br.com.elegacy.libraryapi.service.impl;

import br.com.elegacy.libraryapi.model.entity.Loan;
import br.com.elegacy.libraryapi.model.repository.LoanRepository;
import br.com.elegacy.libraryapi.service.LoanService;

public class LoanServiceImpl implements LoanService {

	private final LoanRepository loanRepository;

	public LoanServiceImpl(LoanRepository loanRepository) {
		this.loanRepository = loanRepository;
	}

	@Override
	public Loan save(Loan loan) {
		return this.loanRepository.save(loan);
	}

}
