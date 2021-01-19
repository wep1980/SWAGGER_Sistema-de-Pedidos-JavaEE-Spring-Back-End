package br.com.waldirep.springionicmc.domain;

import java.io.Serializable;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.Inheritance;
import javax.persistence.InheritanceType;
import javax.persistence.JoinColumn;
import javax.persistence.MapsId;
import javax.persistence.OneToOne;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonTypeInfo;

import br.com.waldirep.springionicmc.domain.unums.EstadoPagamento;

/**
 *  Mapeamento para herança com estratégia, na qual exitem 2 principais : 
 *  
 * @Inheritance(strategy = InheritanceType.JOINED) => TABELÃO OU TABELA UNICA(JOINED) - Vai ter todos os campos das classes de 
 * herança onde ao instanciar uma das classes a outra tera os atributos com valor NULL ** Mais performace **
 * 
 * @Inheritance(strategy = InheritanceType.SINGLE_TABLE) => Gera uma tabela para cada sub-classe e ai quando for pesquisar o pagamento 
 * é necessário fazer a junção(joins). Quando tem muitos atributos nas sub-classes essa se torna a melhor estratégia
 * 
 * @JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type") => Permite a instanciação de subclasses a partid de dados JSON
 * OBS : Na inserssão de um pedido é obrigatório informar o tipo de Pagamento que é uma classe abstrata, então quando for informado pagamentoComCartao sera instanciada a classe respectiva
 * e quando for  pagamentoComBoleto sera instanciada a classe respectiva

 * @author Waldir
 *
 */
@Entity
@Inheritance(strategy = InheritanceType.JOINED) 
@JsonTypeInfo(use = JsonTypeInfo.Id.NAME, include = JsonTypeInfo.As.PROPERTY, property = "@type") // Campo adicional da Classe que se chama @type
public abstract class Pagamento implements Serializable{

	private static final long serialVersionUID = 1L;
	
	@Id // O PAGAMENTO TEM QUE TER O MESMO ID DO PEDIDO CORRESPONDENTE
	private Integer id;
	
	
	/**
	 * OBS: O @JsonManagedReference e o @JsonBackRefence apresentou problemas com o envio de dados Json em requisições.
	 * SOLUÇÃO : @JsonIgnore no lado da associação que não deve ser serializada.
	 * Apague as anotações @JsonManagedReference existentes
       Troque as anotações @JsonBackRefence por @JsonIgnore
	 */
	//@JsonBackReference // Referencia a serialização na classe Pedido atributo pedido
	@JsonIgnore
	@OneToOne
	@JoinColumn(name = "pedido_id") // Coluna correspondente ao ID do pedido
	@MapsId // GARANTE QUE O ID DO PAGAMENTO SEJA O MESMO DO PEDIDO
	private Pedido pedido;
	
	/**
	 * OBS : No construtor foi passado como EstadoPagamento e não integer, 
	 * no setEstadoPagamento esta setando apenas o código this.estadoPagamento = estadoPagamento.getCod();
	 * no getEstadoPagamento() esta sendo chamado o método toEnum() da classe EstadoPagamento que retorna o estado de pagamento
	 * 
	 * internamente o estado pagamento esta sendo armazenado como um Integer, 
	 * mas externamente a classe vai expor o tipo de EstadoPagamento
	 */
	private Integer estadoPagamento;
	
	
	public Pagamento() {
		// TODO Auto-generated constructor stub
	}

    /**
     * OBS : O EstadoPagemnto esta armazenando somente o código => this.estadoPagamento = estadoPagamento.getCod();
     * @param id
     * @param estadoPagamento
     * @param pedido
     */
	public Pagamento(Integer id, EstadoPagamento estadoPagamento, Pedido pedido) {
		super();
		this.id = id;
		this.pedido = pedido;
		// Se o estadoPagamento for igual a null atribui null senão atribui o código
		this.estadoPagamento = (estadoPagamento == null) ? null : estadoPagamento.getCod();
	}


	public Integer getId() {
		return id;
	}


	public void setId(Integer id) {
		this.id = id;
	}

	/**
     * Chamada do método EstadoPagamento.toEnum da classe EstadoPagmento que retorna o estado do pagamento
     * @return
     */
	public EstadoPagamento getEstadoPagamento() {
		return EstadoPagamento.toEnum(estadoPagamento);
	}

	 /**
     * Esta armazenando somente o código => this.estadoPagamento = estadoPagamento.getCod();
     * @param EstadoPagamento
     */
	public void setEstadoPagamento(EstadoPagamento estadoPagamento) {
		this.estadoPagamento = estadoPagamento.getCod();
	}


	public Pedido getPedido() {
		return pedido;
	}


	public void setPedido(Pedido pedido) {
		this.pedido = pedido;
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
		Pagamento other = (Pagamento) obj;
		if (id == null) {
			if (other.id != null)
				return false;
		} else if (!id.equals(other.id))
			return false;
		return true;
	}
	
	
	
	
	
}
