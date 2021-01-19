package br.com.waldirep.springionicmc.domain;

import java.io.Serializable;

import javax.persistence.Embeddable;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;

/**
 * Classe que contem a chave composta
 * @author Waldir
 *
 */
@Embeddable // Anotacçao que indica que essa classe é um sub tipo pois foi criada apenas para comter a chave composta da classe ItemPedido
public class ItemPedidoPK implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne // O ItemPedido tem que conhecer o pedido
	@JoinColumn(name = "pedido_id") // Nome da chave estrangeira
	private Pedido pedido;
	
	@ManyToOne // O ItemPedido tem que conhecer o produto
	@JoinColumn(name = "produto_id") // Nome da chave estrangeira
	private Produto produto;
	
	
	
	public Pedido getPedido() {
		return pedido;
	}
	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
	}
	public Produto getProduto() {
		return produto;
	}
	public void setProduto(Produto produto) {
		this.produto = produto;
	}
	
	
	
	
	@Override
	public int hashCode() {
		final int prime = 31;
		int result = 1;
		result = prime * result + ((pedido == null) ? 0 : pedido.hashCode());
		result = prime * result + ((produto == null) ? 0 : produto.hashCode());
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
		ItemPedidoPK other = (ItemPedidoPK) obj;
		if (pedido == null) {
			if (other.pedido != null)
				return false;
		} else if (!pedido.equals(other.pedido))
			return false;
		if (produto == null) {
			if (other.produto != null)
				return false;
		} else if (!produto.equals(other.produto))
			return false;
		return true;
	}
	
	
	

}
