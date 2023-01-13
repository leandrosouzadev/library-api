package br.com.elegacy.libraryapi.api.resource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

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
