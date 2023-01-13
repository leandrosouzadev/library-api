package br.com.elegacy.libraryapi.service;

import static org.assertj.core.api.Assertions.assertThat;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit.jupiter.SpringExtension;

import br.com.elegacy.libraryapi.exception.BusinessException;
import br.com.elegacy.libraryapi.model.entity.Book;
import br.com.elegacy.libraryapi.model.repository.BookRepository;
import br.com.elegacy.libraryapi.service.impl.BookServiceImpl;

@ExtendWith(SpringExtension.class)
@ActiveProfiles("test")
class BookServiceTest {

	private BookService bookService;

	@MockBean
	private BookRepository bookRepository;

	@BeforeEach
	public void setUp() {
		this.bookService = new BookServiceImpl(bookRepository);
	}

	@Test
	@DisplayName("Should save a book.")
	void shouldSaveBook() {
		// Arrange
		Book book = createValidBook();
		Mockito.when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(false);

		Book bookMock = Book
				.builder()
				.id(1L)
				.isbn("123")
				.author("Fulano")
				.title("As aventuras")
				.build();

		Mockito.when(bookService.save(book)).thenReturn(bookMock);

		// Act
		Book savedBook = bookService.save(book);

		// Assert
		assertThat(savedBook.getId()).isNotNull();
		assertThat(savedBook.getAuthor()).isEqualTo("Fulano");
		assertThat(savedBook.getIsbn()).isEqualTo("123");
		assertThat(savedBook.getTitle()).isEqualTo("As aventuras");
	}

	@Test
	@DisplayName("Shouldn't save a book with duplicate isbn.")
	void shouldNotSaveBookWithDuplicateIsbn() throws Exception {
		// Arrange
		Book book = createValidBook();
		Mockito.when(bookRepository.existsByIsbn(Mockito.anyString())).thenReturn(true);

		// Act
		Throwable exception = Assertions.catchThrowable(() -> bookService.save(book));

		// Assert
		assertThat(exception)
				.isInstanceOf(BusinessException.class)
				.hasMessage("Isbn already registered");

		Mockito.verify(bookRepository, Mockito.never()).save(book);
	}

	private Book createValidBook() {
		Book book = Book
				.builder()
				.isbn("123")
				.author("Fulano")
				.title("As aventuras")
				.build();

		return book;
	}
}
