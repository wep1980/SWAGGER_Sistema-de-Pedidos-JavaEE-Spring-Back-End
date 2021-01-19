package br.com.waldirep.springionicmc.domain;

import java.io.Serializable;
import java.text.NumberFormat;
import java.util.Locale;

import javax.persistence.EmbeddedId;
import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonIgnore;

/**
 * A classe de associação não possuí ID próprio, quem identifica ela são as classes associadas, Produto e Pedido.
 * É criada uma chave composta contendo produto e pedido em uma nova classe : ItemPedidoPK
 * @author Waldir
 *
 */
@Entity
public class ItemPedido implements Serializable{

	private static final long serialVersionUID = 1L;
	
	/**
	 * É instanciada a classe itemPedidoPK como id, nela contém a chave composta
	 * 
	 * ItemPedidoPK id -> É uma peculiaridade do JPA, para os programadores que forem utilizar a classe esse objeto não faz sentido,
	 * Então no CONSTRUTOR ele é substituido por Pedido pedido e Produto produto
	 * 
	 * OBS : Serialização no ItemPedido por possuir uma chave composta(relacão entre Pedido e ItemPedido)
	 * @JsonIgnore private ItemPedidoPK id = new ItemPedidoPK() => Não sera serializado.
	 * O ItemPedido não serializa o Produto nem o Pedido, então não tera @JsonManagedReference e @JsonBackReference
	 * O Pedido serializa os itens dele
	 * 
	 * O importante é que a partir de um ItemPedido eu tenha acesso ao Produto
	 */
	@EmbeddedId // Anotação para chave composta. Foi criada a classe ItemPedidoPK que carrega as chaves segundárias de Produto e Pedido
	@JsonIgnore // Não sera serializado
	private ItemPedidoPK id = new ItemPedidoPK();
	
	private Double desconto;
	private Integer quantidade;
	private Double preco;
	
	
	public ItemPedido() {
		// TODO Auto-generated constructor stub
	}


	public ItemPedido(Pedido pedido, Produto produto, Double desconto, Integer quantidade, Double preco) {
		super();
		id.setPedido(pedido); // OBS acima em ItemPedidoPK id
		id.setProduto(produto); // // OBS acima em ItemPedidoPK id
		this.desconto = desconto;
		this.quantidade = quantidade;
		this.preco = preco;
	}
	
	/**
	 * O nome do método com get na frente permite ser reconheçido pelo JSON e serializado
	 * @return
	 */
	public double getSubtotal() {
		return (preco - desconto) * quantidade;
	}
	
	
	/*-------------------------------------------------------------------------------------------------------------*/
	
	// Métodos GET para ter acesso direto ao pedido e ao produto fora da classe ItemPedido -> Melhora a semântica da classe
	
	@JsonIgnore // Não sera serializado -- Evita assim a referência cíclica
	public Pedido getPedido() {
		return id.getPedido();
	}
	
	
	public Produto getProduto() {
		return id.getProduto();
	}
	
	public void setPedido(Pedido pedido) {
		id.setPedido(pedido);
	}
	
	public void setProduto(Produto produto) {
		id.setProduto(produto);
	}
	
	
	/*-------------------------------------------------------------------------------------------------------------*/


	public ItemPedidoPK getId() {
		return id;
	}


	public void setId(ItemPedidoPK id) {
		this.id = id;
	}


	public Double getDesconto() {
		return desconto;
	}


	public void setDesconto(Double desconto) {
		this.desconto = desconto;
	}


	public Integer getQuantidade() {
		return quantidade;
	}


	public void setQuantidade(Integer quantidade) {
		this.quantidade = quantidade;
	}


	public Double getPreco() {
		return preco;
	}


	public void setPreco(Double preco) {
		this.preco = preco;
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
		ItemPedido other = (ItemPedido) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}


	@Override
	public String toString() {
		NumberFormat nf = NumberFormat.getCurrencyInstance(new Locale("pt", "BR")); // Formata valores em dinheiro
		StringBuilder builder = new StringBuilder();
		builder.append(getProduto().getNome());
		builder.append(", Qte: ");		
		builder.append(getQuantidade());		
		builder.append(", Preço unitário: ");
		builder.append(nf.format(getPreco()));
		builder.append(", Subtotal: ");
		builder.append(nf.format(getSubtotal()));
		builder.append("\n");

		return builder.toString();
	}
	
	
	

}
