package br.com.elegacy.libraryapi.service.impl;

import java.util.Optional;

import org.springframework.data.domain.Example;
import org.springframework.data.domain.ExampleMatcher;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import br.com.elegacy.libraryapi.exception.BusinessException;
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
		if (bookRepository.existsByIsbn(book.getIsbn())) {
			throw new BusinessException("Isbn already registered");
		}

		return bookRepository.save(book);
	}

	@Override
	public Optional<Book> getById(Long id) {
		return this.bookRepository.findById(id);
	}

	@Override
	public void delete(Book book) {
		if (book == null || book.getId() == null) {
			throw new IllegalArgumentException("Book id cant be null");
		}

		this.bookRepository.delete(book);

	}

	@Override
	public Book update(Book book) {
		if (book == null || book.getId() == null) {
			throw new IllegalArgumentException("Book id cant be null");
		}

		return this.bookRepository.save(book);
	}

	@Override
	public Page<Book> find(Book filter, Pageable pageRequest) {
		Example<Book> example = Example.of(filter,
				ExampleMatcher
						.matching()
						.withIgnoreCase()
						.withIgnoreNullValues()
						.withStringMatcher(ExampleMatcher.StringMatcher.CONTAINING));

		return this.bookRepository.findAll(example, pageRequest);
	}

	@Override
	public Optional<Book> getBookByIsbn(String isbn) {	
		return this.bookRepository.findByIsbn(isbn);
	}

}
