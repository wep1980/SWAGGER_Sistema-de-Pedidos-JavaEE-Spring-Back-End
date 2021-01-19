package br.com.waldirep.springionicmc.domain;

import java.io.Serializable;
import java.text.NumberFormat;
import java.text.SimpleDateFormat;
import java.util.Date;
import java.util.HashSet;
import java.util.Locale;
import java.util.Set;

import javax.persistence.CascadeType;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonFormat;

@Entity
public class Pedido implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Integer id;
	
	@JsonFormat(pattern ="dd/MM/yyyy HH:mm") // Formata Data e Hora
	private Date instante;
	
	
	/**
	 * OBS: O @JsonManagedReference e o @JsonBackRefence apresentou problemas com o envio de dados Json em requisições.
	 * SOLUÇÃO : @JsonIgnore no lado da associação que não deve ser serializada.
	 * Apague as anotações @JsonManagedReference existentes
       Troque as anotações @JsonBackRefence por @JsonIgnore
	 */
	//@JsonManagedReference // Permite que o pagamento seja serializado
	// Um pedido tem um pagamento -- Um pagamento tem um pedido
	@OneToOne(cascade = CascadeType.ALL, mappedBy = "pedido") // Evita o erro de entidade transient na hora de gravar os dados - PECUALIARIDADE DO JPA
	private Pagamento pagamento; // mappedBy = "pedido" => Mapeado na classe pagamento 
	
	/**
	 * @JsonManagedReference => entre o pedido e o cliente existe uma associação de mão dupla. 
	 * No endpoint de pedido tem que mostrar o cliente.
	 * 
	 * @JsonManagedReference ==> A serialização sera feita de um pedido para o cliente
	 *  Na classe Cliente não sera permitido a serialização e precisa ser colocada a anotação @JsonBackReference
	 */
	//@JsonManagedReference
	@ManyToOne // Todo pedido tem 1 endereço -- Um cliente pode fazer vários pedidos
	@JoinColumn(name = "cliente_id") // Nome da chave estrangeira no banco de dados
	private Cliente cliente;
	
	// NA CLASSE ENDEREÇO NÃO TEM MAPEAMENTO PQ A ASSOCIAÇÃO É DIRECIONAL
	@ManyToOne //todo pedido tem 1 cliente
	@JoinColumn(name = "endereco_de_entrega_id") // Nome da chave estrangeira no banco de dados
	private Endereco enderecoDeEntrega;
	
	/**
	 * O Set é uma coleção que garante que na havera itens repetidos
	 * 
	 * Sem colocar nenhuma referência os itens ja serão serializados
	 */
	@OneToMany(mappedBy = "id.pedido") // OBS : Mapeamento feito no *id* da classe ItemPedido e *pedido* da classe ItemPedidoPK
	private Set<ItemPedido> itens = new HashSet<ItemPedido>();
	
	
	public Pedido() {
		// TODO Auto-generated constructor stub
	}


	public Pedido(Integer id, Date instante, Cliente cliente, Endereco enderecoDeEntrega) {
		super();
		this.id = id;
		this.instante = instante;
		this.cliente = cliente;
		this.enderecoDeEntrega = enderecoDeEntrega;
	}

	
	public double getValorTotal() {
		double soma = 0.0;
		for (ItemPedido itemPedido : itens) {
			soma = soma + itemPedido.getSubtotal();
		}
		return soma;
		
	}

	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}


	public Date getInstante() {
		return instante;
	}


	public void setInstante(Date instante) {
		this.instante = instante;
	}


	public Pagamento getPagamento() {
		return pagamento;
	}


	public void setPagamento(Pagamento pagamento) {
		this.pagamento = pagamento;
	}


	public Cliente getCliente() {
		return cliente;
	}


	public void setCliente(Cliente cliente) {
		this.cliente = cliente;
	}


	public Endereco getEnderecoDeEntrega() {
		return enderecoDeEntrega;
	}


	public void setEnderecoDeEntrega(Endereco enderecoDeEntrega) {
		this.enderecoDeEntrega = enderecoDeEntrega;
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
		Pedido other = (Pedido) obj;
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
		
		SimpleDateFormat sdf = new SimpleDateFormat("dd/MM/yyyy hh:mm:ss"); // Formata a data
		
		StringBuilder builder = new StringBuilder();
		builder.append("Pedido número: ");
		builder.append(getId());
		builder.append(", Instante: ");
		builder.append(sdf.format(getInstante()));
		builder.append(", Cliente: ");
		builder.append(getCliente().getNome());
		builder.append(", Situação do pagamento: ");
		builder.append(getPagamento().getEstadoPagamento().getDescricao());
		builder.append("\nDetalhes:\n");
		
		for (ItemPedido itemPedido : itens) {
			builder.append(itemPedido.toString());
		}
		builder.append("Valor total: ");
		builder.append(nf.format(getValorTotal()));

		return builder.toString();
	}
	
	

}
