package br.com.waldirep.springionicmc.services;

import org.springframework.security.core.context.SecurityContextHolder;

import br.com.waldirep.springionicmc.security.UserSS;

/**
 * Classe que que captura o USUARIO que estiver logado no sistema
 * @author wepbi
 *
 */
public class UserService {

	/**
	 * Método que que captura um USUARIO logado
	 * 
	 * @return
	 */
	public static UserSS authenticated() {
		try {
			// Retorna o usuario que estiver logado no sistema
			return (UserSS) SecurityContextHolder.getContext().getAuthentication().getPrincipal(); 

		} catch (Exception e) {
			return null;
		}
	}

}
