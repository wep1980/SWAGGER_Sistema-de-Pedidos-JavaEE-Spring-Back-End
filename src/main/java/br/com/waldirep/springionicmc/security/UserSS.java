package br.com.waldirep.springionicmc.security;

import java.util.Collection;
import java.util.Set;
import java.util.stream.Collectors;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.waldirep.springionicmc.domain.unums.Perfil;

/**
 * Classe de usuario para autenticação do spring security
 * @author wepbi
 *
 */
public class UserSS implements UserDetails{
	private static final long serialVersionUID = 1L;
	

	private Integer id;
	
	private String email;
	
	private String senha;
	
	private Collection<? extends GrantedAuthority> authorities;
	

	
	public UserSS() {
		// TODO Auto-generated constructor stub
	}
	

	/**
	 * O contrutor não vai receber uma lista de Collection<? extends GrantedAuthority> e sim uma lista de Perfis Set<Perfil> perfis
	 * 
	 * @param id
	 * @param email
	 * @param senha
	 * @param authorities
	 */
	public UserSS(Integer id, String email, String senha, Set<Perfil> perfis) {
		super();
		this.id = id;
		this.email = email;
		this.senha = senha;
		/*
		 * Convertendo o Set<Perfil> perfis para Collection<? extends GrantedAuthority> authorities com expressão LAMBDA
		 */
		this.authorities = perfis.stream().map(x -> new SimpleGrantedAuthority(x.getDescricao())).collect(Collectors.toList());
	}



	public Integer getId() {
		return id;
	}
	
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return authorities;
	}

	
	@Override
	public String getPassword() {
		return senha;
	}

	@Override
	public String getUsername() {
		return email;
	}

	
	/**
	 * Por padrão o método retorna que a conta não esta expirada
	 */
	@Override
	public boolean isAccountNonExpired() {
		return true;
	}
	

	/**
	 * Por padrão o método retorna que a conta não esta bloqueada
	 */
	@Override
	public boolean isAccountNonLocked() {
		return true;
	}
	

	/**
	 * Por padrão o método retorna que as credencias não estão expiradas
	 */
	@Override
	public boolean isCredentialsNonExpired() {
		return true;
	}
	

	/**
	 * Por padrão o método retorna que o usuario esta ativo
	 */
	@Override
	public boolean isEnabled() {
		return true;
	}


	/**
	 * Método que testa se o USUARIO que esta logando tem um dado perfil
	 * 
	 * getAuthorities().contains() -> Acessa a lista de perfis
	 * (new SimpleGrantedAuthority(perfil.getDescricao()) -> Converte o perfil para o tipo GrantedAuthority do SpringSecurity
	 * @param perfil
	 * @return
	 */
	public boolean hasRole(Perfil perfil) {
		return getAuthorities().contains(new SimpleGrantedAuthority(perfil.getDescricao()));
	}

}
