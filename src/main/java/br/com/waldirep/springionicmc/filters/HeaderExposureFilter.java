package br.com.waldirep.springionicmc.filters;

import java.io.IOException;

import javax.servlet.Filter;
import javax.servlet.FilterChain;
import javax.servlet.ServletException;
import javax.servlet.ServletRequest;
import javax.servlet.ServletResponse;
import javax.servlet.http.HttpServletResponse;

import org.springframework.stereotype.Component;

/**
 * Classe que filtra as requisições
 * @author wepbi
 *
 */
@Component
public class HeaderExposureFilter implements Filter{

	/**
	 * Método que vai interceptar todas as requisições e vai expor o header "location" na resposta e encaminha a requisição para o seu ciclo normal
	 */
	@Override
	public void doFilter(ServletRequest request, ServletResponse response, FilterChain chain)
			throws IOException, ServletException {
		// TODO Auto-generated method stub
		
		HttpServletResponse res = (HttpServletResponse) response; // Cast do ServletResponse para se tornar um HttpServletResponse e assim utlizar o metodo addHeader()
		res.addHeader("access-control-expose-headers", "location"); // Liberação de CORS, deixa o cabeçalho exposto. Liberação de recursos vindas de requisições diferentes
		
		chain.doFilter(request, response);
		
	}
	
	

}
