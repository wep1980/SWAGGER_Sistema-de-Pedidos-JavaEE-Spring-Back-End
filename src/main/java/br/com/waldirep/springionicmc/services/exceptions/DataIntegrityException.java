package br.com.waldirep.springionicmc.services.exceptions;

/*
 * Classe de excessão customizada
 */
public class DataIntegrityException extends RuntimeException {

	private static final long serialVersionUID = 1L;
	
	// Contrutor
	public DataIntegrityException(String msg) {
		super(msg);
	}
	
	
	// Contrutor que recebe uma menssagem e outra excessao, a causa de algo que aconteceu antes
	public DataIntegrityException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
