package br.com.waldirep.springionicmc.domain;

import java.util.Date;

import javax.persistence.Entity;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonTypeName;

import br.com.waldirep.springionicmc.domain.unums.EstadoPagamento;

/**
 * A chave primaria e herdada da super classe : Pagamento
 * @author Waldir
 *
 */
@Entity
@JsonTypeName("pagamentoComBoleto") // Anotação que define de qual type a classe é
public class PagamentoComBoleto extends Pagamento {
	
	private static final long serialVersionUID = 1L;
	
	@JsonFormat(pattern ="dd/MM/yyyy") // Formata Data
	private Date dataVencimento;
	
	@JsonFormat(pattern ="dd/MM/yyyy") // Formata Data
	private Date dataPagamento;
	
	
	public PagamentoComBoleto() {
		// TODO Auto-generated constructor stub
	}


	public PagamentoComBoleto(Integer id, EstadoPagamento estadoPagamento, Pedido pedido, Date dataVencimento, Date dataPagamento) {
		super(id, estadoPagamento, pedido);
		// TODO Auto-generated constructor stub
		this.dataPagamento = dataPagamento;
		this.dataVencimento = dataVencimento;
	}


	public Date getDataVencimento() {
		return dataVencimento;
	}


	public void setDataVencimento(Date dataVencimento) {
		this.dataVencimento = dataVencimento;
	}


	public Date getDataPagamento() {
		return dataPagamento;
	}


	public void setDataPagamento(Date dataPagamento) {
		this.dataPagamento = dataPagamento;
	}
	
	
	

}
