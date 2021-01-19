package br.com.waldirep.springionicmc.dto;

import java.io.Serializable;

import javax.validation.constraints.NotEmpty;

import org.hibernate.validator.constraints.Length;


import br.com.waldirep.springionicmc.domain.Categoria;

/**
 * Classe Data transfers object
 * Seleciona os dados que serão trafegados
 * @author Waldir
 *
 */
public class CategoriaDTO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	
	private Integer id;
	
	@NotEmpty(message ="Preenchimento obrigatório")
	@Length(min = 5, max = 80, message = "O tamanho deve ser entre 5 e 80 caracteres")
	private String nome;
	
	
	public CategoriaDTO() {
		// TODO Auto-generated constructor stub
	}
	
	
	/**
	 * Construtor responsável por instanciar um DTO a partir de um objeto categoria
	 * @param obj
	 */
	public CategoriaDTO(Categoria obj) {
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
