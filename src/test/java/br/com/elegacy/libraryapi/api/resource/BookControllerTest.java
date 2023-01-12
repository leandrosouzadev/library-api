package br.com.elegacy.libraryapi.api.resource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockHttpServletRequestBuilder;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.elegacy.libraryapi.api.resource.dto.BookDTO;

@ActiveProfiles("test")
@WebMvcTest
@AutoConfigureMockMvc
class BookControllerTest {

	private static String BOOK_API = "/api/books";

	@Autowired
	private MockMvc mockMvc;

	@Test
	@DisplayName("Should create a successful book.")
	void shouldCreateBook() throws Exception {
		BookDTO bookDTO = BookDTO
				.builder()
				.author("Arthur")
				.title("As aventuras")
				.isbn("001")
				.build();

		String json = new ObjectMapper().writeValueAsString(bookDTO);

		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(BOOK_API)
				.contentType(MediaType.APPLICATION_JSON)
				.accept(MediaType.APPLICATION_JSON)
				.content(json);

		mockMvc.perform(request)
				.andExpect(status().isCreated())
				.andExpect(jsonPath("id").isNotEmpty())
				.andExpect(jsonPath("title").value(bookDTO.getTitle()))
				.andExpect(jsonPath("author").value(bookDTO.getAuthor()))
				.andExpect(jsonPath("isbn").value(bookDTO.getIsbn()));
	}

//	@Test
//	@DisplayName("Shouldn't create an invalid book.")
//	public void shouldNotCreateInvalidBook() {
//
//	}

}
