package br.com.waldirep.springionicmc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.waldirep.springionicmc.services.DBService;
import br.com.waldirep.springionicmc.services.EmailService;
import br.com.waldirep.springionicmc.services.MockEmailService;

/**
 * Classe para configurações especificas para o profile de Test
 * @author Waldir
 *
 */
@Configuration
@Profile("test")// Indica que todos os Beans que estiverem dentro dessa classe vão ser ativados apenas quando o profile de test estiver ativo no application.properties
public class TestConfig {
	
	@Autowired
	private DBService dbService;
	
	
	/**
	 * Método responsavel por instanciar o banco de dados no profile de test
	 * @return
	 * @throws ParseException 
	 */
	@Bean // A anotação com @Bean torna disponivel esse método como componente do sistema
	public boolean intantiateDatabase() throws ParseException {
		dbService.instantiateTestDatabase();
		return true;
	}
	
	/**
	 * Retorna uma instancia de MockEmailService
	 * @return
	 */
	@Bean // A anotação com @Bean torna disponivel esse método como componente do sistema
	public EmailService emailService() {
		return new MockEmailService();
	}

}
