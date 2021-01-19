package br.com.waldirep.springionicmc.domain.unums;

public enum EstadoPagamento {
	
	PENDENTE(1, "Pendente"),
	QUITADO(2, "Quitado"),
	CANCELADO(3, "Cancelado");

	
	private int cod;
	private String descricao;
	
	
	
	private EstadoPagamento(int cod, String descricao) {
		this.cod = cod;
		this.descricao = descricao;
	}
	
	
	
	public int getCod() {
		return cod;
	}
	
	
	public String getDescricao() {
		return descricao;
	}
	
	
	/**
	 * Método que retorna se o estado do pagamento : pendente, quitado ou cancelado
	 * OBS : Método estatico pode ser executado sem instanciar o objeto e de qualquer lugar
	 */
	public static EstadoPagamento toEnum(Integer cod) {
		if(cod == null) {
			return null;
		}
		for (EstadoPagamento ep : EstadoPagamento.values()) {
			if(cod.equals(ep.getCod())) {
				return ep;
			}
		}
		throw new IllegalArgumentException("Id inválido: " + cod);
	}
}
