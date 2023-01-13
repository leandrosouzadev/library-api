package br.com.elegacy.libraryapi.service;

import java.util.Optional;

import br.com.elegacy.libraryapi.model.entity.Book;

public interface BookService {

	public Book save(Book book);

	public Optional<Book> getById(Long id);

}
