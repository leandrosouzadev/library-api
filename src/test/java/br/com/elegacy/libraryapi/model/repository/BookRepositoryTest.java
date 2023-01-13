package br.com.elegacy.libraryapi.model.repository;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ActiveProfiles;

@ActiveProfiles("test")
@DataJpaTest
public class BookRepositoryTest {

	@Autowired
	private TestEntityManager testEntityManager;
	
	@Autowired
	private BookRepository bookRepository;
	
	@Test
	@DisplayName("should return true when there is a book with informed isbn")
	public void shouldReturnTrueWhenIsbnExists() {
		// Arrange
		String isbn = "123";
		
		// Act
		boolean exists = bookRepository.existsByIsbn(isbn);
		
		// Assert
		assertThat(exists).isTrue();
	}
}
