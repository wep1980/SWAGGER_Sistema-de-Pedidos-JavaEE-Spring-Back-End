package br.com.waldirep.springionicmc.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;
import java.util.stream.Collectors;

import javax.persistence.CascadeType;
import javax.persistence.CollectionTable;
import javax.persistence.Column;
import javax.persistence.ElementCollection;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

import br.com.waldirep.springionicmc.domain.unums.Perfil;
import br.com.waldirep.springionicmc.domain.unums.TipoCliente;

/*
 * Por padrão o springData não permite a exclusão de objetos relacionados que possuem relação  ( de 1 para * )
 * para permitir a exclusão de relacionamentos com * é utlizada a referencia cascade
 */
@Entity
public class Cliente implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String nome;
	
	@Column(unique = true) // Náo tera repetiçoes neste campo
	private String email;
	
	private String cpfOuCnpj;
	
	/**
	 * OBS : No construtor foi passado como TipoCliente e não integer, 
	 * no setTipoCliente esta setando apenas o código this.tipoCliente = tipoCliente.getCodigo()
	 * no getTipoCliente esta sendo chamado o método toEnum() da classe TipoCliente que retorna o tipo de cliente 
	 * 
	 * internamente o tipo cliente esta sendo armazenado como um Integer, 
	 * mas externamente a classe vai expor o tipo de TipoCliente
	 */
	private Integer tipoCliente;
	
	
	@JsonIgnore // Para não aparecer no JSON na hora de recuperar um cliente
	private String senha;
	

	/**
	 * OBS: O @JsonManagedReference e o @JsonBackRefence apresentou problemas com o envio de dados Json em requisições.
	 * SOLUÇÃO : @JsonIgnore no lado da associação que não deve ser serializada.
	 * Apague as anotações @JsonManagedReference existentes
       Troque as anotações @JsonBackRefence por @JsonIgnore
       
       cascade = CascadeType.ALL => instrução do JPA que indica o comportamento em cascata onde toda operação 
       que modificar um cliente vai poder reflitir em cascata nos endereços. Se o cliente for apagado os endereços também serão
       automaticamente
	 */
	//@JsonManagedReference // O cliente pode serializar os endereços
	@OneToMany(mappedBy = "cliente", cascade = CascadeType.ALL) // Mapeado na tabela endereço variável cliente
	private List<Endereco> enderecos = new ArrayList<Endereco>();
	
	
	/**
	 * Nesse projeto decidimos por não criar uma classe para telefones
	 * O telefone é apenas uma string que contém um número, se tivesse mais atributos o ideal seria criar uma classe
	 * Esta sendo criada uma coleção de strings associadas ao cliente do tipo Set que não aceita repetição
	 */
	@ElementCollection // Mapeamento da entidade no banco de dados como entidade fraca
	@CollectionTable(name = "TELEFONE") // Nome da tabela no banco de dados para mapeamento
	private Set<String> telefones = new HashSet<String>(); 
	
	
	/**
	 * fetch=FetchType.EAGER -> Garante que sempre que for buscado um cliente do BD junto sera trazido os perfis tb
	 */
	@ElementCollection(fetch=FetchType.EAGER)
	@CollectionTable(name="PERFIS") // Nome da tabela no banco de dados para mapeamento
	private Set<Integer> perfis = new HashSet<>(); // Aqui e armazenado apenas numeros inteiros
	
	
	/**
	 * OBS: O @JsonManagedReference e o @JsonBackRefence apresentou problemas com o envio de dados Json em requisições.
	 * SOLUÇÃO : @JsonIgnore no lado da associação que não deve ser serializada.
	 * Apague as anotações @JsonManagedReference existentes
       Troque as anotações @JsonBackRefence por @JsonIgnore
	 */
	// Um cliente pode fazer vários pedidos -- Um pedido tem um cliente
	//@JsonBackReference // Referencia a serialização na classe Pedido atributo cliente
	@OneToMany(mappedBy = "cliente") // Mapeado na classe Pedido pelo atributo cliente
	@JsonIgnore
	private List<Pedido> pedidos = new ArrayList<Pedido>();
	
	/*
	// Implementação provisória para teste
	private String imageUrl; */
	
	
	public Cliente() {
		addPerfil(Perfil.CLIENTE); // Por padrão todos os usuarios do sistema terão o perfil de cliente
	}


    /**
     * OBS : O tipoCliente esta armazenando somente o código => this.tipoCliente = tipoCliente.getCodigo();
     * 
     * O construtor com argumentos não é necessario, ele e feito apenas para a instanciar um cliente em uma linha só
     * 
     * @param id
     * @param nome
     * @param email
     * @param cpfOuCnpj
     * @param tipoCliente
     */
	public Cliente(Integer id, String nome, String email, String cpfOuCnpj, TipoCliente tipoCliente, String senha) {
		super();
		this.id = id;
		this.nome = nome;
		this.email = email;
		this.cpfOuCnpj = cpfOuCnpj;
		// Se o Cliente for igual a null atribui null senão atribui o código
		this.tipoCliente = (tipoCliente == null) ? null : tipoCliente.getCodigo();
		this.senha = senha;
		addPerfil(Perfil.CLIENTE); // Por padrão todos os usuarios do sistema terão o perfil de cliente
	}
	
	
	/**
	 * Metodo que adiciona o perfil ao cliente
	 * @param perfil
	 */
	public void addPerfil(Perfil perfil) {
		perfis.add(perfil.getCod());
	}
	
	
	/*
	 * Retorna os perfeis corresponde ao cliente
	 * OBS :NDa coleção de perfis e armazenado somente numero inteiro
	 * 
	 * Converte o numero inteiro para o perfil equivalente através de uma expressão LAMBDA
	 * e utilizando o metodo toEnum da classe Perfil
	 */
	public Set<Perfil> getPerfis() {
		return perfis.stream().map(x -> Perfil.toEnum(x)).collect(Collectors.toSet());
	}
	

	public List<Pedido> getPedidos() {
		return pedidos;
	}
	
	
	public void setPedidos(List<Pedido> pedidos) {
		this.pedidos = pedidos;
	}
	

	public String getSenha() {
		return senha;
	}
	
	
	public void setSenha(String senha) {
		this.senha = senha;
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



	public String getEmail() {
		return email;
	}



	public void setEmail(String email) {
		this.email = email;
	}



	public String getCpfOuCnpj() {
		return cpfOuCnpj;
	}



	public void setCpfOuCnpj(String cpfOuCnpj) {
		this.cpfOuCnpj = cpfOuCnpj;
	}


    /**
     * Chamada do método TipoCliente.toEnum da classe TipoCliente que retorna o tipo de cliente
     * @return
     */
	public TipoCliente getTipoCliente() {
		return TipoCliente.toEnum(tipoCliente);
	}


    /**
     * Esta armazenando somente o código => this.tipoCliente = tipoCliente.getCodigo();
     * @param tipoCliente
     */
	public void setTipoCliente(TipoCliente tipoCliente) {
		this.tipoCliente = tipoCliente.getCodigo();
	}



	public List<Endereco> getEnderecos() {
		return enderecos;
	}



	public void setEnderecos(List<Endereco> enderecos) {
		this.enderecos = enderecos;
	}



	public Set<String> getTelefones() {
		return telefones;
	}



	public void setTelefones(Set<String> telefones) {
		this.telefones = telefones;
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
		Cliente other = (Cliente) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	/* public String getImageUrl() {
		return imageUrl;
	}


	public void setImageUrl(String imageUrl) {
		this.imageUrl = imageUrl;
	} */
	
	
	

}
