package br.com.elegacy.libraryapi.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertDoesNotThrow;
import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mockito;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.Example;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
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

	@Test
	@DisplayName("Should get a book by id.")
	void shouldGetBookById() {
		// Arrange
		Long id = 1L;
		Book book = createValidBook();
		book.setId(id);

		Mockito.when(bookRepository.findById(id)).thenReturn(Optional.of(book));

		// Act
		Optional<Book> foundBook = bookService.getById(id);

		// Asssert
		assertThat(foundBook).isPresent();
		assertThat(foundBook.get().getId()).isEqualTo(id);
		assertThat(foundBook.get().getAuthor()).isEqualTo(book.getAuthor());
		assertThat(foundBook.get().getIsbn()).isEqualTo(book.getIsbn());
		assertThat(foundBook.get().getTitle()).isEqualTo(book.getTitle());
	}

	@Test
	@DisplayName("Should return empty when book id does not exist.")
	void shouldReturnEmptyWhenBookIdDoesNotExist() {
		// Arrange
		Long id = 1L;

		Mockito.when(bookRepository.findById(id)).thenReturn(Optional.empty());

		// Act
		Optional<Book> foundBook = bookService.getById(id);

		// Asssert
		assertThat(foundBook).isEmpty();
	}

	@Test
	@DisplayName("Should delete a book.")
	void shouldDeleteBook() {
		// Arrange
		Book book = Book.builder().id(1L).build();

		// Act
		assertDoesNotThrow(() -> bookService.delete(book));

		// Assert
		Mockito.verify(bookRepository, Mockito.times(1)).delete(book);
	}

	@Test
	@DisplayName("Should throw exception when deleting a non-existent book.")
	void shouldThrowExceptionWhenDeletingNonExistentBook() {
		// Arrange
		Book book = new Book();

		// Act
		assertThrows(IllegalArgumentException.class, () -> bookService.delete(book));

		// Assert
		Mockito.verify(bookRepository, Mockito.never()).delete(book);
	}

	@Test
	@DisplayName("Should delete a book.")
	void shouldUpdateBook() {
		// Arrange
		Long id = 1L;
		Book updatingBook = Book.builder().id(id).build();

		Book updatedBook = createValidBook();
		updatedBook.setId(id);

		Mockito.when(bookRepository.save(updatingBook)).thenReturn(updatedBook);

		// Act
		Book book = bookService.update(updatingBook);

		// Assert
		assertThat(book.getId()).isEqualTo(updatedBook.getId());
		assertThat(book.getTitle()).isEqualTo(updatedBook.getTitle());
		assertThat(book.getIsbn()).isEqualTo(updatedBook.getIsbn());
		assertThat(book.getAuthor()).isEqualTo(updatedBook.getAuthor());
	}

	@Test
	@DisplayName("Should throw exception when updating a non-existent book.")
	void shouldThrowExceptionWhenUpdatingNonExistentBook() {
		// Arrange
		Book book = new Book();

		// Act
		assertThrows(IllegalArgumentException.class, () -> bookService.update(book));

		// Assert
		Mockito.verify(bookRepository, Mockito.never()).save(book);
	}

	@Test
	@DisplayName("Should find by filter")
	void shouldFindByFilter() {
		// Arrange
		Book book = createValidBook();
		PageRequest pageRequest = PageRequest.of(0, 10);
		List<Book> books = Arrays.asList(book);

		Page<Book> page = new PageImpl<Book>(books, pageRequest, 1);
		Mockito.when(bookRepository.findAll(Mockito.<Example<Book>>any(), Mockito.any(PageRequest.class)))
				.thenReturn(page);

		// Act
		Page<Book> result = bookService.find(book, pageRequest);

		// Assert
		assertThat(result.getTotalElements()).isEqualTo(1);
		assertThat(result.getContent()).isEqualTo(books);
		assertThat(result.getPageable().getPageNumber()).isZero();
		assertThat(result.getPageable().getPageSize()).isEqualTo(10);
	}

	@Test
	@DisplayName("Should get a book by isbn")
	void shouldGetBookByIsbn() {
		// Arrange
		String isbn = "1230";		
		Book book = Book.builder()
				.id(1L)
				.isbn(isbn)
				.build();

		Mockito.when(bookRepository.findByIsbn(isbn)).thenReturn(Optional.of(book));
		
		// Act
		Optional<Book> foundBook = bookService.getBookByIsbn(isbn);

		// Assert
		assertThat(foundBook).isPresent();
		assertThat(foundBook.get().getId()).isEqualTo(1L);
		assertThat(foundBook.get().getIsbn()).isEqualTo(isbn);
		
		verify(bookRepository, times(1)).findByIsbn(isbn);
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
