package br.com.waldirep.springionicmc.config;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.service.ApiInfo;
import springfox.documentation.service.Contact;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@Configuration
@EnableSwagger2
public class SwaggerConfig {
	
	
	
	
	
	
	
	@Bean // Responsavel por criar o obj Docket
	public Docket api() {
		return new Docket(DocumentationType.SWAGGER_2)
				
				
				
				.select()
				.apis(RequestHandlerSelectors.basePackage("br.com.waldirep.springionicmc.resources")) // pacotes que o swagger vai rastreia para gerar documentação
				.paths(PathSelectors.any())
				.build() // Criaa instancia e devolve
	            .apiInfo(apiInfo());
	}
	
	
	
	
	private ApiInfo apiInfo() {
		return new ApiInfo("API Projeto Spring-REST-iONIC",
		                   "Esta API é utilizada no Projeto SpringBoot-REST-iONIC do Dev Waldir escouto pereira",
		                   "Versão 1.0",
		                   "https://github.com/wep1980/Sistema-de-Pedidos-JavaEE-Spring-Back-End",
		new Contact("Waldir escouto pereira", "https://www.linkedin.com/in/wepdev/", "wepcienciadacomputacao@gmail.com"),
		            "Projeto didático",
		            "https://github.com/wep1980/Sistema-de-Pedidos-JavaEE-Spring-Back-End",
		Collections.emptyList() // Vendor Extensions
		);
	}
	
	

	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
}
