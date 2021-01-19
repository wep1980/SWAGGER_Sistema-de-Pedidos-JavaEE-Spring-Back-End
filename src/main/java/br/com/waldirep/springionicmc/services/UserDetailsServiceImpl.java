package br.com.waldirep.springionicmc.services;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.waldirep.springionicmc.domain.Cliente;
import br.com.waldirep.springionicmc.repositories.ClienteRepository;
import br.com.waldirep.springionicmc.security.UserSS;

/**
 * Classe do spring security que permite a busca de um usuario pelo nome
 * @author wepbi
 *
 */
@Service // Torna um serviço do framework para poder ser injetado
public class UserDetailsServiceImpl implements UserDetailsService{

	@Autowired
	private ClienteRepository repo;
	
	
	/**
	 * Método que busca um usuario pelo userName que no caso e o email
	 */
	@Override
	public UserDetails loadUserByUsername(String email) throws UsernameNotFoundException {
		
		Cliente cli = repo.findByEmail(email);
		if(cli == null) {
			throw new UsernameNotFoundException(email);
		}
		return new UserSS(cli.getId(), cli.getEmail(), cli.getSenha(), cli.getPerfis());
	}

}

