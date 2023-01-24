package br.com.elegacy.libraryapi;

import java.util.Arrays;
import java.util.List;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.scheduling.annotation.EnableScheduling;

import br.com.elegacy.libraryapi.service.EmailService;

@SpringBootApplication
@EnableScheduling
public class LibraryApiApplication {
	
	@Autowired
	private EmailService emailService;

    @Bean
    ModelMapper modelMapper() {
        return new ModelMapper();
    }
	
    @Bean
    CommandLineRunner runner() {
    	return args -> {
    		List<String> emails = Arrays.asList("f1a1981a79-782ecd@inbox.mailtrap.io");
    		emailService.sendMails("Testando o serviço de emails.", emails);
    		//System.err.println("Emails enviados");
    	};
    }

	public static void main(String[] args) {
		SpringApplication.run(LibraryApiApplication.class, args);
	}

}
