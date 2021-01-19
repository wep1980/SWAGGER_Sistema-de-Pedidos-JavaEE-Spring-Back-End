package br.com.waldirep.springionicmc.dto;

import java.io.Serializable;

import javax.validation.constraints.Email;
import javax.validation.constraints.NotEmpty;



/**
 * Classe para receber o email que o usuario informar
 * @author wepbi
 *
 */
public class EmailDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	

	@NotEmpty(message = "Preenchimento obrigatório")
	@Email(message = "Email inválido")
	private String email;
	
	
	
	public EmailDTO() {
		// TODO Auto-generated constructor stub
	}



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}
	
	
	

}
