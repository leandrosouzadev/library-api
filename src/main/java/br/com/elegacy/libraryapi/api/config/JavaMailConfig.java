package br.com.elegacy.libraryapi.api.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.JavaMailSenderImpl;

@Configuration
public class JavaMailConfig {
    
	@Bean
    JavaMailSender javaMailSender() {
        return new JavaMailSenderImpl();
    }
}
