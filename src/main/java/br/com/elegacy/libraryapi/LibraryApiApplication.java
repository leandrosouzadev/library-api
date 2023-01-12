package br.com.elegacy.libraryapi;

import org.modelmapper.ModelMapper;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;

@SpringBootApplication
public class LibraryApiApplication {

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
	

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}

}
