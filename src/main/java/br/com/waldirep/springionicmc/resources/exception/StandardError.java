package br.com.waldirep.springionicmc.resources.exception;

import java.io.Serializable;

/**
 * Classe auxiliar Error padrão, informa detalhes do erro como : status exemplo(400, 200, 403) - msg exemplo(Objeto não encontrado)
 * timeStamp = hora e data
 * message
 * path
 * 
 * OBS: Classe criada com padrão de erros do Spring
 * 
 * 
 * @author Waldir
 *
 */
public class StandardError implements Serializable{

	private static final long serialVersionUID = 1L;
	

	private Long timestamp;
	private Integer status;
	private String error;
	private String message;
	private String path;
	
	
	
	public StandardError(Long timestamp, Integer status, String error, String message, String path) {
		super();
		this.timestamp = timestamp;
		this.status = status;
		this.error = error;
		this.message = message;
		this.path = path;
	}



	public Long getTimestamp() {
		return timestamp;
	}



	public void setTimestamp(Long timestamp) {
		this.timestamp = timestamp;
	}



	public Integer getStatus() {
		return status;
	}



	public void setStatus(Integer status) {
		this.status = status;
	}



	public String getError() {
		return error;
	}



	public void setError(String error) {
		this.error = error;
	}



	public String getMessage() {
		return message;
	}



	public void setMessage(String message) {
		this.message = message;
	}



	public String getPath() {
		return path;
	}



	public void setPath(String path) {
		this.path = path;
	}


}
