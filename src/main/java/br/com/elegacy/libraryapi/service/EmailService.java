package br.com.elegacy.libraryapi.service;

import java.util.List;

public interface EmailService {

	public void sendMails(String message, List<String> mailsList);

}
