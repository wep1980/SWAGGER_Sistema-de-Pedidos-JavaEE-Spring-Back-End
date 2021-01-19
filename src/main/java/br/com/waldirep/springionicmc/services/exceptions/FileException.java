package br.com.waldirep.springionicmc.services.exceptions;

/*
 * Classe de serviço de excessão customizada para upload de arquivos no amazonS3
 */
public class FileException extends RuntimeException {
	private static final long serialVersionUID = 1L;
	
	// Contrutor
	public FileException(String msg) {
		super(msg);
	}
	
	
	// Contrutor que recebe uma menssagem e outra excessao, a causa de algo que aconteceu antes
	public FileException(String msg, Throwable cause) {
		super(msg, cause);
	}

}
