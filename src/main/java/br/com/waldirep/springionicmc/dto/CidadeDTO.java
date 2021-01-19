package br.com.waldirep.springionicmc.dto;

import java.io.Serializable;

import br.com.waldirep.springionicmc.domain.Cidade;

public class CidadeDTO implements Serializable{

	private static final long serialVersionUID = 1L;
	
	
	private Integer id;
	
	private String nome;
	
	
	public CidadeDTO() {
		// TODO Auto-generated constructor stub
	}


	/**
	 * Construtor que recebe como parametro uma Cidade
	 * @param obj
	 */
	public CidadeDTO(Cidade obj) {
		id = obj.getId();
		nome = obj.getNome();
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public String getNome() {
		return nome;
	}


	public void setNome(String nome) {
		this.nome = nome;
	}
	
	
	

}
