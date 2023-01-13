package br.com.elegacy.libraryapi.model.repository;

import org.springframework.data.jpa.repository.JpaRepository;

import br.com.elegacy.libraryapi.model.entity.Book;

public interface BookRepository extends JpaRepository<Book, Long> {

	public boolean existsByIsbn(String isbn);

}
