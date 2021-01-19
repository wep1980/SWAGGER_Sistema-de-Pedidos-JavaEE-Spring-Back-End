package br.com.waldirep.springionicmc.security;

import java.io.IOException;
import java.util.ArrayList;
import java.util.Date;

import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.http.HttpServletRequest;
import javax.servlet.http.HttpServletResponse;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.waldirep.springionicmc.dto.CredenciasDTO;

/**
 * Classe de filtro interceptar a requisão de login
 * 
 * @author wepbi
 *
 */
public class JWTAuthenticationFilter extends UsernamePasswordAuthenticationFilter {
	

	private AuthenticationManager authenticationManager;

	private JWTUtil jwtUtil;
	
	
	

	/**
	 * Metodo construtor
	 * Injeção pelo construtor do AuthenticationManager e JWTUtil e JWTAuthenticationFailureHandler
	 * @param authenticationManager
	 * @param jwtUtil
	 */
	public JWTAuthenticationFilter(AuthenticationManager authenticationManager, JWTUtil jwtUtil) {
		setAuthenticationFailureHandler(new JWTAuthenticationFailureHandler()); // Injeção da Classe abaixo para correção de erro na hora do login, incluindo o erro 401
		this.authenticationManager = authenticationManager;
		this.jwtUtil = jwtUtil;
	}

	
	/**
	 * Metodo para tentar autenticar
	 */
	@Override
	public Authentication attemptAuthentication(HttpServletRequest req, HttpServletResponse res) throws AuthenticationException {
		try {
			CredenciasDTO creds = new ObjectMapper().readValue(req.getInputStream(), CredenciasDTO.class); // captura os dados que vem da requisição e converte para o tipo CredenciasDTO
			
			// Token do Spring security
			UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken( creds.getEmail(), creds.getSenha(), new ArrayList<>());
			
			Authentication auth = authenticationManager.authenticate(authToken); // Verifica se o usuario e senha realmente são validos
			return auth;
		}
		catch (IOException e) {
			throw new RuntimeException(e);
		}
	}

	
	
	/**
	 * Se a autenticação ocorrer o método gera um token e acrescentar na resposta da requisição
	 */
	@Override
	protected void successfulAuthentication(HttpServletRequest req, 
											HttpServletResponse res,
											FilterChain chain,
											Authentication auth) throws IOException, ServletException {
		/*
		 * auth.getPrincipal() -> retorna o usuario do spring security, faz um cast de UserSS e pega o email do usuario que fez o login								
		 */
		String username  = ((UserSS) auth.getPrincipal()).getUsername();
		String token = jwtUtil.generateToken(username); // gera o token e passa para o email
		res.addHeader("Authorization", "Bearer " + token); // retorna o token na resposta da requisição acrescentando ele como cabeçalho da resposta
		res.addHeader("access-control-expose-headers", "Authorization"); // Liberação de CORS, deixa o cabeçalho exposto. Liberação de recursos vindas de requisições diferentes
	}
	

	
	
//---------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------------	
	
	
	/**
	 * Implementação de uma classe privada para poder personalizar a autenticação em caso de falha
	 * @author wepbi
	 *
	 */
	private class JWTAuthenticationFailureHandler implements AuthenticationFailureHandler {

		@Override
		public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response,
				AuthenticationException exception) throws IOException, ServletException {

			response.setStatus(401);
			response.setContentType("application/json");
			response.getWriter().append(json());
		}

		
		private String json() {
			long date = new Date().getTime();
			return "{\"timestamp\": " + date + ", "
					+ "\"status\": 401, "
					+ "\"error\": \"Não autorizado\", "
					+ "\"message\": \"Email ou senha inválidos\", "
					+ "\"path\": \"/login\"}";
		}

	}
}
