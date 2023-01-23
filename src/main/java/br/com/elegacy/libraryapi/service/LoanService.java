package br.com.elegacy.libraryapi.service;

import java.util.Optional;

import br.com.elegacy.libraryapi.model.entity.Loan;

public interface LoanService {

	public Loan save(Loan loan);

	public Optional<Loan> getById(Long id);

	public Loan update(Loan loan);

}
