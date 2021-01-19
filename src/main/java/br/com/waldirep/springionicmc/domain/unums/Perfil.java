package br.com.waldirep.springionicmc.domain.unums;

/*
 * Classe que define o perfil de cada usuario
 */
public enum Perfil {
	

	ADMIN(1, "ROLE_ADMIN"),
	CLIENTE(2, "ROLE_CLIENTE");
	

	private int cod;

	private String descricao;
	
	

	private Perfil(int cod, String descricao) {
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
	 * Método que retorna o perfil do usuario : Admin ou cliente
	 * OBS : Método estatico pode ser executado sem instanciar o objeto e de qualquer lugar.
	 * 
	 * Pega o numero inteiro e converte para o perfil equivalente
	 */
	public static Perfil toEnum(Integer cod) {
		if (cod == null) {
			return null;
		}
		for (Perfil x : Perfil.values()) {
			if (cod.equals(x.getCod())) {
				return x;
			}
		}
		throw new IllegalArgumentException("Id inválido: " + cod);
	}
	
	
}
