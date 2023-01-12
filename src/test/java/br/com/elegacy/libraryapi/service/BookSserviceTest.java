package br.com.elegacy.libraryapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.elegacy.libraryapi.model.entity.Book;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class BookSserviceTest {

	private BookService bookService;

	@Test
	@DisplayName("Should save a book.")
	void shouldSaveBook() {
		// Arrange
		Book book = Book
				.builder()
				.isbn("123")
				.author("Fulano")
				.title("As aventuras")
				.build();

		// Act
		Book savedBook = bookService.save(book);

		// Assert
		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
		assertThat(savedBook.getIsbn()).isEqualTo("As aventuras");
	}

}
