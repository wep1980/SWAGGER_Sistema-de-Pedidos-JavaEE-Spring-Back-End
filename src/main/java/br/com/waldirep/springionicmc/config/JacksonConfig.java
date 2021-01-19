package br.com.waldirep.springionicmc.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.converter.json.Jackson2ObjectMapperBuilder;

import com.fasterxml.jackson.databind.ObjectMapper;

import br.com.waldirep.springionicmc.domain.PagamentoComBoleto;
import br.com.waldirep.springionicmc.domain.PagamentoComCartao;

/**
 * Classe com código padrão, o que muda são as subclasses que precisam ser registradas de acordo com cada projeto
 * @author Waldir
 *
 */
@Configuration
public class JacksonConfig {
	// https://stackoverflow.com/questions/41452598/overcome-can-not-construct-instance-ofinterfaceclass-without-hinting-the-pare
	
	@Bean // A anotação com @Bean torna disponivel esse método como componente do sistema
	public Jackson2ObjectMapperBuilder objectMapperBuilder() {
		Jackson2ObjectMapperBuilder builder = new Jackson2ObjectMapperBuilder() {
			public void configure(ObjectMapper objectMapper) {
				objectMapper.registerSubtypes(PagamentoComCartao.class); // Aqui e o ponto de alteração
				objectMapper.registerSubtypes(PagamentoComBoleto.class); // Aqui e o ponto de alteração
				super.configure(objectMapper);
			}
		};
		return builder;
	}
}
