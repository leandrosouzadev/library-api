package br.com.elegacy.libraryapi.service.impl;

import org.springframework.stereotype.Service;

import br.com.elegacy.libraryapi.model.entity.Book;
import br.com.elegacy.libraryapi.model.repository.BookRepository;
import br.com.elegacy.libraryapi.service.BookService;

@Service
public class BookServiceImpl implements BookService {

	private BookRepository bookRepository;

	public BookServiceImpl(BookRepository bookRepository) {		
		this.bookRepository = bookRepository;
	}

	@Override
	public Book save(Book book) {
		return bookRepository.save(book);
	}

}
