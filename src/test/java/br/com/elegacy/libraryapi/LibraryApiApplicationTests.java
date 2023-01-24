package br.com.elegacy.libraryapi;

import static org.assertj.core.api.Assertions.assertThat;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import br.com.elegacy.libraryapi.api.resource.BookController;

@SpringBootTest
class LibraryApiApplicationTests {
	
	@Autowired
	private BookController bookController;

	@Test
	void contextLoads() {
		assertThat(bookController).isNotNull();		
	}

}
