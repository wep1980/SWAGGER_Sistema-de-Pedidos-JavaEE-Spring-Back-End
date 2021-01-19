package br.com.waldirep.springionicmc.services.exceptions;

/*
 * Classe de excessão customizada
 */
public class AuthorizationException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	// Contrutor
	public AuthorizationException(String msg) {
		super(msg);
	}
	
	
	// Contrutor que recebe uma menssagem e outra excessao, a causa de algo que aconteceu antes
	public AuthorizationException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
