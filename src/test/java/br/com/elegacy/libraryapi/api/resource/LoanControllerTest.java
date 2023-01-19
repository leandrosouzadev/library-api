package br.com.elegacy.libraryapi.api.resource;

import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.time.LocalDate;
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

import br.com.elegacy.libraryapi.api.dto.LoanDTO;
import br.com.elegacy.libraryapi.model.entity.Book;
import br.com.elegacy.libraryapi.model.entity.Loan;
import br.com.elegacy.libraryapi.service.BookService;
import br.com.elegacy.libraryapi.service.LoanService;

@ActiveProfiles("test")
@WebMvcTest(controllers = LoanController.class)
@AutoConfigureMockMvc
class LoanControllerTest {

	private static String LOAN_API = "/api/loans";

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private BookService bookService;

	@MockBean
	private LoanService loanService;

	@Test
	@DisplayName("Should take out a loan")
	void shouldTakeOutLoan() throws Exception {
		// Given
		LoanDTO loanDTO = LoanDTO.builder()
				.isbn("123")
				.customer("Jhon")
				.build();

		String json = new ObjectMapper().writeValueAsString(loanDTO);

		Book book = Book.builder()
				.id(1L)
				.isbn("123")
				.build();

		BDDMockito.given(bookService.getBookByIsbn("123"))
				.willReturn(Optional.of(book));

		Loan loan = Loan.builder()
				.id(1L)
				.customer("Jhon")
				.book(book)
				.loanDate(LocalDate.now())
				.build();

		BDDMockito.given(loanService.save(Mockito.any(Loan.class))).willReturn(loan);

		// When
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(LOAN_API)
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);

		// Then
		mockMvc.perform(request)
				.andExpect(status().isCreated())
				.andExpect(content().string("1"));
				//.andDo(MockMvcResultHandlers.print());
	}
	
	@Test
	@DisplayName("Should not create a loan with non-existent isbn")
	void shouldNotCreateLoanWithNonExistentIsbn() throws Exception {
		// Given
		LoanDTO loanDTO = LoanDTO.builder()
				.isbn("123")
				.customer("Jhon")
				.build();

		String json = new ObjectMapper().writeValueAsString(loanDTO);
		
		BDDMockito.given(bookService.getBookByIsbn("123"))
		.willReturn(Optional.empty());
		
		// When
		MockHttpServletRequestBuilder request = MockMvcRequestBuilders
				.post(LOAN_API)
				.content(json)
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON);

		// Then
		mockMvc.perform(request)
				.andExpect(status().isBadRequest())
				.andExpect(jsonPath("errors", Matchers.hasSize(1)))
				.andExpect(jsonPath("errors[0]").value("Book not found by informed isbn."));
	}

}
