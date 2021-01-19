package br.com.waldirep.springionicmc.config;

import java.text.ParseException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;

import br.com.waldirep.springionicmc.services.DBService;
import br.com.waldirep.springionicmc.services.EmailService;
import br.com.waldirep.springionicmc.services.SmtpEmailService;

/**
 * Classe para configurações especificas para o profile de Test
 * @author Waldir
 *
 */
@Configuration
@Profile("prod")// Indica que todos os Beans que estiverem dentro dessa classe vão ser ativados apenas quando o profile de dev estiver ativo no application.properties
public class ProdConfig {
	
	@Autowired
	private DBService dbService;
	
	/*
	 * Pega o valor da chave spring.jpa.hibernate.ddl-auto do arquivo application properties
	 */
	@Value("${spring.jpa.hibernate.ddl-auto}")
	private String strategy;
	
	/**
	 * Método responsavel por instanciar o banco de dados no profile de dev
	 * 
	 * Regra de negócio => so sera instanciado um banco de dados se no arquivo application.properties na 
	 * chave spring.jpa.hibernate.ddl-auto estiver com o valor create
	 * 
	 * @return
	 * @throws ParseException 
	 */
	@Bean // A anotação com @Bean torna disponivel esse método como componente do sistema
	public boolean intantiateDatabase() throws ParseException {
		
		if(!"create".equals(strategy)) { 
			return false;
		}
		dbService.instantiateTestDatabase();
		return true;
	}
	
	
	/*
	 * Bean para retornar SMTP emailService
	 *  A anotação com @Bean torna disponivel esse método como componente do sistema
	 */
	@Bean 
	public EmailService emailService() {
		return new SmtpEmailService();
	}

}
