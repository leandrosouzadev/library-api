package br.com.elegacy.libraryapi.service;

import java.awt.print.Pageable;
import java.util.Optional;

import org.springframework.data.domain.Page;

import br.com.elegacy.libraryapi.model.entity.Book;

public interface BookService {

	public Book save(Book book);

	public Optional<Book> getById(Long id);

	public void delete(Book book);

	public Book update(Book book);

	public Page<Book> find(Book filter, Pageable pageRequest);

}
