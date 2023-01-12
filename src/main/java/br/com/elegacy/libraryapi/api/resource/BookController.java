package br.com.elegacy.libraryapi.api.resource;

import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;

import br.com.elegacy.libraryapi.api.resource.dto.BookDTO;

@RestController
@RequestMapping("/api/books")
public class BookController {

	@PostMapping
	@ResponseStatus(HttpStatus.CREATED)
	public BookDTO create(@RequestBody BookDTO bookDTO) {
				
		return bookDTO;
	}
}
