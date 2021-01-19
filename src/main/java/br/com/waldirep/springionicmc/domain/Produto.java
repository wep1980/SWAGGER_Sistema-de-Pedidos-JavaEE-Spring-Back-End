package br.com.waldirep.springionicmc.domain;

import java.io.Serializable;
import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.JoinTable;
import javax.persistence.ManyToMany;
import javax.persistence.OneToMany;

import com.fasterxml.jackson.annotation.JsonIgnore;

@Entity
public class Produto implements Serializable{
	private static final long serialVersionUID = 1L;

	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	private String nome;
	
	private double preco;
	
	
	/**
	 * OBS: O @JsonManagedReference e o @JsonBackRefence apresentou problemas com o envio de dados Json em requisições.
	 * SOLUÇÃO : @JsonIgnore no lado da associação que não deve ser serializada.
	 * Apague as anotações @JsonManagedReference existentes
       Troque as anotações @JsonBackRefence por @JsonIgnore
	 */
	//@JsonBackReference // Resolve o problema de serialização quando os produtos são buscados pela categoria
	@JsonIgnore
	@ManyToMany /* Muitos para muitos */
	@JoinTable(name = "PRODUTO_CATEGORIA", /* Em relacionamento de * to * e criada uma nova tabela */
	joinColumns = @JoinColumn(name ="produto_id"), /* Nome da chave estrangeira na tabela PRODUTO_CATEGORIA */
	inverseJoinColumns = @JoinColumn(name ="categoria_id")) /* Nome da chave estrangeira na tabela PRODUTO_CATEGORIA */
	private List<Categoria> categorias = new ArrayList<Categoria>();
	
	
	/**
	 * O Set é uma coleção que garante que na havera itens repetidos
	 * 
	 * @JsonIgnore ==> Não sera serializado, Porque o importante é que o ItemPedido tenha o acesso ao Produto
	 */
	@JsonIgnore // Não sera serializado
	@OneToMany(mappedBy = "id.produto") // OBS : Mapeamento feito no *id* da classe ItemPedido e *produto* da classe ItemPedidoPK
	private Set<ItemPedido> itens = new HashSet<ItemPedido>();
	
	
	public Produto() {
		
	}


	public Produto(Integer id, String nome, double preco) {
		super();
		this.id = id;
		this.nome = nome;
		this.preco = preco;
	}
	
	/**
	 * Adiciona os itens do ItemPedido a uma lista de pedidos
	 * 
	 * Percorre todos os ItemPedido na lista de itens que ja existe aqui na classe.
	 * 
	 * OBS :  @JsonIgnore => Se não ignorar sera feita a serialização dos pedidos associados aos Produtos evitando assim a referência
	 * cíclica. Tudo que começa com GET automáticamente e serializado
	 * @return
	 */
	@JsonIgnore
	public List<Pedido> getPedidos(){
		List<Pedido> lista = new ArrayList<Pedido>();
		for (ItemPedido itemPedido : itens) {
			lista.add(itemPedido.getPedido());
		}
		return lista;
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


	public double getPreco() {
		return preco;
	}


	public void setPreco(double preco) {
		this.preco = preco;
	}


	public List<Categoria> getCategorias() {
		return categorias;
	}


	public void setCategorias(List<Categoria> categorias) {
		this.categorias = categorias;
	}
	
	public Set<ItemPedido> getItens() {
		return itens;
	}


	public void setItens(Set<ItemPedido> itens) {
		this.itens = itens;
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
		Produto other = (Produto) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	
	
	

}
