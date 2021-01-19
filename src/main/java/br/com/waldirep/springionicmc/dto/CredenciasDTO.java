package br.com.waldirep.springionicmc.dto;

import java.io.Serializable;

/**
 * Classe que contem email e senha para autenticação do Spring security
 * @author wepbi
 *
 */
public class CredenciasDTO implements Serializable{
	private static final long serialVersionUID = 1L;
	

	private String email;
	
	private String senha;
	
	
	
	public CredenciasDTO() {
		// TODO Auto-generated constructor stub
	}
	

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getSenha() {
		return senha;
	}

	public void setSenha(String senha) {
		this.senha = senha;
	}
	
	
	

}
