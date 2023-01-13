package br.com.elegacy.libraryapi.exception;

public class BusinessException extends RuntimeException {

	private static final long serialVersionUID = -1845022912936087657L;

	public BusinessException(String message) {
		super(message);
	}

}
