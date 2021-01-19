package br.com.waldirep.springionicmc.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;


@Entity
public class Cidade implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String nome;
	
	/**
	 * OBS: O @JsonManagedReference e o @JsonBackRefence apresentou problemas com o envio de dados Json em requisições.
	 * SOLUÇÃO : @JsonIgnore no lado da associação que não deve ser serializada.
	 * Apague as anotações @JsonManagedReference existentes
       Troque as anotações @JsonBackRefence por @JsonIgnore
	 */
	//@JsonManagedReference // A cidade pode serializar os estados
	@ManyToOne
	@JoinColumn(name = "estado_id") // Nome da chave estrangeira na tabela cidade
	private Estado estado;
	
	
	public Cidade() {
		// TODO Auto-generated constructor stub
	}
	
	

	public Cidade(Integer id, String nome, Estado estado) {
		super();
		this.id = id;
		this.nome = nome;
		this.estado = estado;
	}


	public Estado getEstado() {
		return estado;
	}
	
	public void setEstado(Estado estado) {
		this.estado = estado;
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

	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((id == null) ? 0 : id.hashCode());
		return result;
	}

	@Override
	public boolean equals(Object obj) {
		if (this == obj)
			return true;
		if (obj == null)
			return false;
		if (getClass() != obj.getClass())
			return false;
		Cidade other = (Cidade) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	

}
