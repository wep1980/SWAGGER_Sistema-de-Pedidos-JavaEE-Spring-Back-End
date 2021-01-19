package br.com.waldirep.springionicmc.security;

import java.io.IOException;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.www.BasicAuthenticationFilter;

/**
 * Classe de filtro para verificar se o token e valido
 * @author wepbi
 *
 */
public class JWTAuthorizationFilter extends BasicAuthenticationFilter {

	
	private JWTUtil jwtUtil;
	
	private UserDetailsService userDetailsService;
	
	
	/**
	 * Construtor
	 * @param authenticationManager
	 * @param jwtUtil
	 * @param userDetailsService
	 */
	public JWTAuthorizationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil, UserDetailsService userDetailsService) {
		super(authenticationManager);
		this.jwtUtil = jwtUtil;
		this.userDetailsService = userDetailsService; // Extrai o usuario do token e buscar no banco de dados pelo email para saber se ele e valido
	}
	
	
	/**
	 * intercepta a requisição e verifica se o usuario esta autorizado
	 * Metodo que executa algo antes de deixar a requisição continuar
	 */
	@Override
	protected void doFilterInternal(HttpServletRequest request, 
									HttpServletResponse response, 
									FilterChain chain) throws IOException, ServletException {
		
		String header = request.getHeader("Authorization"); //Pegando o cabeçalho da requisição
		if(header != null && header.startsWith("Bearer ")) { // Se exister cabeçalho e exister a palavara Bearer
			
			/*
			 * getAuthentication(request, header.substring(7)) -> Recebe o token como arguemnto 
			 * retirando o prefixo Bearer com substring(7), que diminui os 7 primeiros caracteres que é o,
			 * Bearer+espaço
			 */
			UsernamePasswordAuthenticationToken auth = getAuthentication( header.substring(7));
			if(auth != null) { // se esta tudo certo com token
				SecurityContextHolder.getContext().setAuthentication(auth); // Libera o acesso ao filtro
			}
		}
		chain.doFilter(request, response); // Libera o processo depois da verificação
	}


	/**
	 * 
	 * @param request
	 * @param token
	 * @return
	 */
	private UsernamePasswordAuthenticationToken getAuthentication(String token) {
		
		if(jwtUtil.tokenValindo(token)) {
			String username = jwtUtil.getUsername(token); // Pega o username dentro do token
			UserDetails user = userDetailsService.loadUserByUsername(username); // Busca no banco de dados
			return new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities()); // retorna o nome do user e as autorizações
		}
	
		return null;
	}

	
}
