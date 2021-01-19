package br.com.waldirep.springionicmc.resources.exception;

import java.io.Serializable;

/**
 * Classe auxiliar que mostra a lista de erros como nome do campo e a messagem de erro
 * @author Waldir
 *
 */
public class FieldMessage implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private String fieldName;
	
	private String message;
	
	
	
	public FieldMessage() {
		// TODO Auto-generated constructor stub
	}



	public FieldMessage(String fieldName, String message) {
		super();
		this.fieldName = fieldName;
		this.message = message;
	}



	public String getFieldName() {
		return fieldName;
	}



	public void setFieldName(String fieldName) {
		this.fieldName = fieldName;
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}
	
	
	
	

}
