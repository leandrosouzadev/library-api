package br.com.elegacy.libraryapi.service.impl;

import java.awt.print.Pageable;
import java.util.Optional;

import org.springframework.data.domain.Page;
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
		if(book == null || book.getId() == null) {
			throw new IllegalArgumentException("Book id cant be null");
		}
		
		this.bookRepository.delete(book);
		
	}

	@Override
	public Book update(Book book) {
		if(book == null || book.getId() == null) {
			throw new IllegalArgumentException("Book id cant be null");
		}
		
		return this.bookRepository.save(book);
	}

	@Override
	public Page<Book> find(Book filter, Pageable pageRequest) {
		// TODO Auto-generated method stub
		return null;
	}

}
