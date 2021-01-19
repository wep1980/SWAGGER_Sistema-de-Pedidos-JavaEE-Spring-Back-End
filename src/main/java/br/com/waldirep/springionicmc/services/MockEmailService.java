package br.com.waldirep.springionicmc.services;

import javax.mail.internet.MimeMessage;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.mail.SimpleMailMessage;

/**
 * Classe de implementação de email para teste, so vai mostrar o email no log do servidor
 * @author Waldir
 *
 */
public class MockEmailService extends AbstractEmailService{
	
	/*
	 * static serve para que toda vez que for chamado o MockEmailService não precisar ser chamado outro
	 * é um único para todos
	 */
	private static final Logger LOG = LoggerFactory.getLogger(MockEmailService.class);

	
	/**
	 * Envio de email teste
	 */
	@Override
	public void sendEmail(SimpleMailMessage msg) {
		LOG.info("Simulando envio de email... ");
		LOG.info(msg.toString());
		LOG.info("Email enviado");
	}


    /**
     * Envio de email teste
     */
	@Override
	public void sendHtmlEmail(MimeMessage msg) {
		LOG.info("Simulando envio de email HTML... ");
		LOG.info(msg.toString());
		LOG.info("Email enviado");
		
	}

}
