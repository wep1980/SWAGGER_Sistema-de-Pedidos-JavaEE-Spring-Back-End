package br.com.waldirep.springionicmc.security;

import java.util.Date;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;

@Component // Anotação que serve para essa classe ser injetada como componente
public class JWTUtil {
	
	@Value("${jwt.secret}") // Pegando o valor do jwt.secret no application.properties -- Palavra secreta que sera embaralhada com o token
	private String secret;
	
	
	@Value("${jwt.expiration}") // Pegando o valor do jwt.expiration no application.properties -- Tempo de expirassão do Token
	private Long expiration;
	
	
	/**
	 * Método que gera o token
	 * 
	 * @param email
	 * @return
	 */
	public String generateToken(String username) {
		return Jwts.builder() // Metodo da biblioteca JWT que gera o token
				.setSubject(username) // recebe o usuario que veio de argumento
				.setExpiration(new Date(System.currentTimeMillis() + expiration)) // Setando o tempo de expiração do token que pega o horario atual do sistema e soma com o definido por nós que se encontra no application.properties
				.signWith(SignatureAlgorithm.HS512, secret.getBytes()) // Tipo de assinatura do token( Pega um array de bytes ), que recebe um tipo de algoritmo qe junta com o secret, que é a palavra secreta
				.compact(); // Junta tudo
	}


	/**
	 * Método que valida o token
	 * @param token
	 * @return
	 */
	public boolean tokenValindo(String token) {
		Claims claims = getClaims(token); // getClaims() -> Tipo do JWT que armazena as revindicações do token (O usuario que esta acessando o endpoint com token esta revindicando algumas coisas)
		
		if(claims != null) { // se tiver claims 
			String username = claims.getSubject(); // retorna o usuario
			Date expirationDate = claims.getExpiration(); // retorna a data expiração
			Date now = new Date(System.currentTimeMillis()); // Pegando a data atual para comparar com a data do token e ver se esta valida
			if(username != null && expirationDate != null && now.before(expirationDate)) { // se exister um nome, se exister a data e o instante atual for anterior a data de expiração
				return true;
			}
		}
		return false;
	}


	private Claims getClaims(String token) {
	   
		try {
			 /*
		     * Recupera os claims(REVINDICAÇÕES) a partir de um token
		     */
			return Jwts.parser().setSigningKey(secret.getBytes()).parseClaimsJws(token).getBody();
			
		} catch (Exception e) {
            return null;
      }
	}


	public String getUsername(String token) {
		Claims claims = getClaims(token); // getClaims() -> Tipo do JWT que armazena as revindicações do token (O usuario que esta acessando o endpoint com token esta revindicando algumas coisas)
		if(claims != null) { // se tiver claims 
			return claims.getSubject(); // retorna o usuario
		}
		return null;
	}

}
