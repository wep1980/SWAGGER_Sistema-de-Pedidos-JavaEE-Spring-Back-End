package br.com.waldirep.springionicmc.config;

import java.util.Arrays;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpMethod;
import org.springframework.security.config.annotation.authentication.builders.AuthenticationManagerBuilder;
import org.springframework.security.config.annotation.method.configuration.EnableGlobalMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.builders.WebSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configuration.WebSecurityConfigurerAdapter;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.web.cors.CorsConfiguration;
import org.springframework.web.cors.CorsConfigurationSource;
import org.springframework.web.cors.UrlBasedCorsConfigurationSource;

import br.com.waldirep.springionicmc.security.JWTAuthenticationFilter;
import br.com.waldirep.springionicmc.security.JWTAuthorizationFilter;
import br.com.waldirep.springionicmc.security.JWTUtil;

/**
 * Classe que define as configurações de segurança
 * libera e bloqueia por padrão os endpoints
 * 
 * OBS : so ao colocar a dependencia no pom.xml ela ja bloqueia os endpoints
 * @author Waldir
 *
 */
@Configuration
@EnableWebSecurity
@EnableGlobalMethodSecurity(prePostEnabled = true) // Permite criar Autorizações para perfeis especificos
public class SecurityConfig extends WebSecurityConfigurerAdapter {
	
	@Autowired
	private JWTUtil jwtUtil;
	
	@Autowired // Objeto de configuração para acesso ao H2
	private Environment env;
	
	@Autowired
	private UserDetailsService userDetailsService;
	
	// Caminhos que por padrão estão liberados, acesso ao BD H2
	private static final String[] PUBLIC_MATCHERS = {
			"/h2-console/**"
			};
	
	
	    /**
	     * Caminhos que por padrão estão liberados para acesso a TODOS os USUARIOS, apenas para recuperação de dados
	     * produtos e categorias -> todos os usuarios podem consultar os produtos disponives e suas categorias
	     * Estados -> na hora de se cadastrar um novo usuario pode acessar o seu estado
	     */
		private static final String[] PUBLIC_MATCHERS_GET = {
				"/produtos/**", 
				"/categorias/**",
				"/estados/**"
				};
		
		
		/*
		 * Para acesso a todos os ENDPOINTS e preciso estar logado porem um
		 * USUARIO não logado pode se cadastrar no sistema.
		 * USUARIO não logado pode recuperar sua senha.
		 * 
		 * Permite que usuarios não cadastrados utilizem ENDPOINTs 
		 * especificos. Nesse caso somente para cadastro e recuperação de senha.
		 */
		private static final String[] PUBLIC_MATCHERS_POST = {
				"/clientes",
				"/auth/forgot/**"
				};
		
	/**
	 * Liberando o swagger , ferramenta de documentação
	 */
	@Override
	public void configure(WebSecurity web) throws Exception {
		web.ignoring().antMatchers("/v2/api-docs", "/configuration/ui", "/swagger-resources/**", "/configuration/**",
		"/swagger-ui.html", "/webjars/**");
		}	
		
		
	/**
	 * Método que configura as autorizações recebe um HttpSecurity
	 */
	@Override
	protected void configure(HttpSecurity http) throws Exception {
		
		/*
		 * Lógica de negócio para acesso ao BD h2 que pega o profile ativo(test) em application.properties
		 */
		if(Arrays.asList(env.getActiveProfiles()).contains("test")) {
			http.headers().frameOptions().disable();
		}
		
		http.cors().and().csrf().disable(); // como o sistema e stateless e não armazena a autenticação em sessão o csrf foi desabilitado
		http.authorizeRequests()
		.antMatchers(HttpMethod.POST, PUBLIC_MATCHERS_POST).permitAll() //Permite autorização de POST, no caso o vetor tem apenas clientes
		.antMatchers(HttpMethod.GET, PUBLIC_MATCHERS_GET).permitAll() //Permissão apenas para o method GET para os usuarios desta lista
		.antMatchers(PUBLIC_MATCHERS).permitAll() 
		.anyRequest().authenticated(); // Libera o acesso as URLs do vetor e para todo resto exige autenticação
		http.addFilter(new JWTAuthenticationFilter(authenticationManager(), jwtUtil)); // registrando o filtro de autenticação
		http.addFilter(new JWTAuthorizationFilter(authenticationManager(), jwtUtil, userDetailsService)); // registrando o filtro de autorização
		http.sessionManagement().sessionCreationPolicy(SessionCreationPolicy.STATELESS); // Garante que não sera criada sessão de usuario
	}
	
	
	/**
	 * Método do spring security de autenticação que mostra quem é o userDetailsService que esta sendo usado 
	 * e e qual e o algoritmo de codificação da senha, que no caso e o bCryptPasswordEncoder(), metodo que esta
	 * logo abaixo, que ja possuí o @Bean e por isso não existe a necessidade de @Bean tb nesse método
	 */
	@Override
	public void configure(AuthenticationManagerBuilder auth) throws Exception {
		auth.userDetailsService(userDetailsService).passwordEncoder(bCryptPasswordEncoder());
	}
	
	
	/**
	 * Método que configura os Cors
	 *  A anotação com @Bean torna disponivel esse método como componente do sistema
	 * @return
	 */
	@Bean
	CorsConfigurationSource corsConfigurationSource() {
		
		final CorsConfiguration configuration = new CorsConfiguration().applyPermitDefaultValues();
		
		// "OPTIONS" -> Método que os front-ends utilizam para testar a primeira requisição
		configuration.setAllowedMethods(Arrays.asList("POST", "GET", "PUT", "DELETE", "OPTIONS"));
		
		final UrlBasedCorsConfigurationSource source = new UrlBasedCorsConfigurationSource();
		source.registerCorsConfiguration("/**", configuration);
		return source;
	}
    
	/**
	 * Método que retorna a senha criptografada
	 * @return
	 */
	@Bean
	public BCryptPasswordEncoder bCryptPasswordEncoder() {
		return new BCryptPasswordEncoder();
	}
	
}
