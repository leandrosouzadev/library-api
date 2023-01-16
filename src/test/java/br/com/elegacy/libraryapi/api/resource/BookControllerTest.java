package br.com.elegacy.libraryapi.api.resource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.awt.print.Pageable;
import java.util.Arrays;
import java.util.Optional;

import org.hamcrest.Matchers;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.BDDMockito;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.elegacy.libraryapi.api.resource.dto.BookDTO;
import br.com.elegacy.libraryapi.exception.BusinessException;
import br.com.elegacy.libraryapi.model.entity.Book;
import br.com.elegacy.libraryapi.service.BookService;

@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
class BookControllerTest {

	private static String BOOK_API = "/api/books";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService;

	@Test
	@DisplayName("Should create a successful book.")
	void shouldCreateBook() throws Exception {
		BookDTO bookDTO = createNewBookDTO();

		Book savedBook = Book
				.builder()
				.id(10l)
				.author("Arthur")
				.title("As aventuras")
				.isbn("001")
				.build();

		BDDMockito.given(bookService.save(Mockito.any(Book.class))).willReturn(savedBook);

		String json = new ObjectMapper().writeValueAsString(bookDTO);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);

		mockMvc.perform(request)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id").value(10l))
				.andExpect(jsonPath("title").value(bookDTO.getTitle()))
				.andExpect(jsonPath("author").value(bookDTO.getAuthor()))
				.andExpect(jsonPath("isbn").value(bookDTO.getIsbn()));
	}

	@Test
	@DisplayName("Shouldn't create an invalid book.")
	void shouldNotCreateInvalidBook() throws Exception {
		String json = new ObjectMapper().writeValueAsString(new BookDTO());

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);

		mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("errors", Matchers.hasSize(3)));
	}

	@Test
	@DisplayName("Shouldn't create a book with duplicate isbn.")
	void shouldNotCreateBookWithDuplicateIsbn() throws Exception {
		BookDTO bookDTO = createNewBookDTO();
		String json = new ObjectMapper().writeValueAsString(bookDTO);
		String errorMessage = "Isbn already registered";

		BDDMockito.given(bookService.save(Mockito.any(Book.class)))
				.willThrow(new BusinessException(errorMessage));

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);

		mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("errors", Matchers.hasSize(1)))
				.andExpect(jsonPath("errors[0]").value(errorMessage));
	}

	@Test
	@DisplayName("Should get details from a book.")
	void shouldGetBookDetail() throws Exception {
		// Given
		Long id = 1l;

		Book book = createNewBook();
		book.setId(id);

		BDDMockito.given(bookService.getById(id)).willReturn(Optional.of(book));

		// When
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(BOOK_API.concat("/").concat(id.toString()))
				.accept(MediaType.APPLICATION_JSON);

		// Then
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").value(id))
				.andExpect(jsonPath("title").value(book.getTitle()))
				.andExpect(jsonPath("author").value(book.getAuthor()))
				.andExpect(jsonPath("isbn").value(book.getIsbn()));
	}

	@Test
	@DisplayName("Should return resource not found when wanted book does not exist.")
	void shouldReturnResourceNotFoundWhenThereIsNoBook() throws Exception {
		// Given
		BDDMockito.given(bookService.getById(Mockito.anyLong())).willReturn(Optional.empty());

		// When
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(BOOK_API.concat("/").concat("1"))
				.accept(MediaType.APPLICATION_JSON);

		// Then
		mockMvc.perform(request)
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Should delete a book.")
	void shouldDeleteBook() throws Exception {
		// Given
		BDDMockito.given(bookService.getById(Mockito.anyLong())).willReturn(Optional.of(Book.builder().id(1L).build()));

		// When
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(BOOK_API.concat("/").concat("1"))
				.accept(MediaType.APPLICATION_JSON);

		// Then
		mockMvc.perform(request)
				.andExpect(status().isNoContent());
	}

	@Test
	@DisplayName("Should return resource not found when there is no book to delete.")
	void shouldReturnNotFoundWhenThereIsNoBookDelete() throws Exception {
		// Given
		BDDMockito.given(bookService.getById(Mockito.anyLong())).willReturn(Optional.empty());

		// When
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.delete(BOOK_API.concat("/").concat("1"))
				.accept(MediaType.APPLICATION_JSON);

		// Then
		mockMvc.perform(request)
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Should update a book.")
	void shouldUpdateBook() throws Exception {
		// Given
		Long id = 1L;
		String json = new ObjectMapper().writeValueAsString(createNewBook());
		Book updatingBook = Book.builder()
				.id(1L)
				.title("Same title")
				.author("Some author")
				.isbn("321")
				.build();

		Book updatedBook = Book.builder()
				.id(id)
				.author("Arthur")
				.title("As aventuras")
				.isbn("001")
				.build();

		BDDMockito.given(bookService.getById(id)).willReturn(Optional.of(updatingBook));
		BDDMockito.given(bookService.update(updatingBook)).willReturn(updatedBook);

		// When
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(BOOK_API.concat("/").concat("1"))
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);

		// Then
		mockMvc.perform(request)
				.andExpect(status().isOk())
				.andExpect(jsonPath("id").value(id))
				.andExpect(jsonPath("title").value(createNewBook().getTitle()))
				.andExpect(jsonPath("author").value(createNewBook().getAuthor()))
				.andExpect(jsonPath("isbn").value(createNewBook().getIsbn()));
	}

	@Test
	@DisplayName("Should return resource not found when there is no book to update.")
	void shouldReturnNotFoundWhenThereIsNotBookUpdate() throws Exception {
		// Given
		String json = new ObjectMapper().writeValueAsString(createNewBook());

		BDDMockito
				.given(bookService.getById(Mockito.anyLong()))
				.willReturn(Optional.empty());

		// When
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.put(BOOK_API.concat("/").concat("1"))
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);

		// Then
		mockMvc.perform(request)
				.andExpect(status().isNotFound());
	}

	@Test
	@DisplayName("Should find books by filter")
	void shouldFindBooksByFilter() throws Exception {
		// Given
		Long id = 1L;

		Book book = Book.builder()
				.id(id)
				.title(createNewBook().getTitle())
				.author(createNewBook().getAuthor())
				.isbn(createNewBook().getIsbn())
				.build();

		BDDMockito.given(bookService.find(Mockito.any(Book.class), Mockito.any(Pageable.class)))
		.willReturn(new PageImpl<Book>(Arrays.asList(book), PageRequest.of(0, 100), 1));
		
		String queryString = String.format("?title=%s&author=%s&page=0&size=100", 
				book.getTitle(), book.getAuthor());
		
		// When
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.get(BOOK_API.concat(queryString))
				.accept(MediaType.APPLICATION_JSON);
		
		// Then
		mockMvc.perform(request)
		.andExpect(status().isOk())
		.andExpect(jsonPath("content", Matchers.hasSize(1)))
		.andExpect(jsonPath("totalElements").value(1))
		.andExpect(jsonPath("pageable.pageSize").value(100))
		.andExpect(jsonPath("pageable.pageNumber").value(0))
		
		;
	}

	private Book createNewBook() {
		Book book = Book
				.builder()
				.author("Arthur")
				.title("As aventuras")
				.isbn("001")
				.build();

		return book;
	}

	private BookDTO createNewBookDTO() {
		BookDTO bookDTO = BookDTO
				.builder()
				.author("Arthur")
				.title("As aventuras")
				.isbn("001")
				.build();

		return bookDTO;
	}
}
