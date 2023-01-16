package br.com.elegacy.libraryapi.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

import br.com.elegacy.libraryapi.model.entity.Book;

@ActiveProfiles("test")
@DataJpaTest
class BookRepositoryTest {

	@Autowired
	private TestEntityManager testEntityManager;

	@Autowired
	private BookRepository bookRepository;

	@Test
	@DisplayName("Should return true when there is a book with informed isbn")
	void shouldReturnTrueWhenIsbnExists() {
		// Arrange
		String isbn = "123";

		Book book = createNewBook(isbn);

		testEntityManager.persist(book);

		// Act
		boolean exists = bookRepository.existsByIsbn(isbn);

		// Assert
		assertThat(exists).isTrue();
	}

	@Test
	@DisplayName("It should return false when there is no book with informed isbn")
	void shouldReturnFalseWhenIsbnDoesntExists() {
		// Arrange
		String isbn = "123";

		// Act
		boolean exists = bookRepository.existsByIsbn(isbn);

		// Assert
		assertThat(exists).isFalse();
	}

	@Test
	@DisplayName("Should get a book by id.")
	void shouldGetBookById() {
		// Arrange
		Book book = createNewBook("123");
		testEntityManager.persist(book);

		// Act
		Optional<Book> foundBook = bookRepository.findById(book.getId());

		// Assert
		assertThat(foundBook).isPresent();
	}

	@Test
	@DisplayName("Should save a book.")
	void shouldSaveBook() {
		// Arrange
		Book book = createNewBook("123");

		// Act
		Book savedBook = bookRepository.save(book);

		// Assert
		assertThat(savedBook.getId()).isNotNull();
	}

	@Test
	@DisplayName("Should delete a book.")
	void shouldDeleteBook() {
		// Arrange
		Book book = createNewBook("123");
		testEntityManager.persist(book);
		Book foundBook = testEntityManager.find(Book.class, book.getId());

		// Act
		bookRepository.delete(foundBook);
		Book deletedBook = testEntityManager.find(Book.class, foundBook.getId());

		// Assert
		assertThat(deletedBook).isNull();
	}

	private Book createNewBook(String isbn) {
		Book book = Book
				.builder()
				.isbn(isbn)
				.author("Fulano")
				.title("As aventuras")
				.build();
		return book;
	}
}
