package br.com.elegacy.libraryapi.service.impl;

import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import br.com.elegacy.libraryapi.service.EmailService;
import lombok.NonNull;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class EmailServiceImpl implements EmailService {

	@NonNull
	private final JavaMailSender javaMailSender;

	@Value("${application.mail.default-remetent}")
	private String remetent;

	@Override
	public void sendMails(String message, List<String> mailsList) {
		String[] mails = mailsList.toArray(new String[mailsList.size()]);

		SimpleMailMessage simpleMailMessage = new SimpleMailMessage();		
		simpleMailMessage.setFrom(remetent);
		simpleMailMessage.setSubject("Book with overdue loan.");
		simpleMailMessage.setText(message);
		simpleMailMessage.setTo(mails);

		javaMailSender.send(simpleMailMessage);
	}

}
