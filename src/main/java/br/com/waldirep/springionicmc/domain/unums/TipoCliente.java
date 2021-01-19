package br.com.waldirep.springionicmc.domain.unums;

public enum TipoCliente {
	
	PESSOAFISICA(1, "Pessoa Física"),
	PESSOAJURIDICA(2, "Pessoa Jurídica");
	
	
	private int codigo;
	private String descricao;
	
	
	private TipoCliente(int codigo, String descricao) {
		this.codigo = codigo;
		this.descricao = descricao;
	}


	public int getCodigo() {
		return codigo;
	}


	public String getDescricao() {
		return descricao;
	}
	
	
	
	/**
	 * Método que retorna se é pessoa física ou jurídica
	 * OBS : Método estatico pode ser executado sem instanciar o objeto
	 */
	public static TipoCliente toEnum(Integer cod) {
		
		if(cod == null) {
			return null;
		}
		for (TipoCliente tipoCliente : TipoCliente.values()) {
			if(cod.equals(tipoCliente.getCodigo())) {
				return tipoCliente;
			}
		}
		
		throw new IllegalArgumentException("Id inválido: " + cod);
	}
	

}
