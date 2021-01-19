package br.com.waldirep.springionicmc.services;



import javax.mail.internet.MimeMessage;

import org.springframework.mail.SimpleMailMessage;

import br.com.waldirep.springionicmc.domain.Cliente;
import br.com.waldirep.springionicmc.domain.Pedido;

/**
 * Padrão STRATEGY => O serviço de email foi criado como interface por vai ser criado o EmailMock e o EmaiSmtp
 * para ter as 2 implementações de forma flexivel
 * @author Waldir
 *
 */
public interface EmailService {
	
	// Método para email de confirmação de pedido - versão de texto plano
	void sendOrderConfirmationEmail(Pedido obj);
	
	// Enviar email - versão de texto plano
	void sendEmail(SimpleMailMessage msg);
	
	
	/*
	 * Versão HTML dos mesmos metodos acima
	 */
	void sendOrderConfirmationHtmlEmail(Pedido obj);
	
	void sendHtmlEmail(MimeMessage msg);

	// Envia uma nova senha para o email do usuario
	void sendNewPasswordEmail(Cliente cliente, String newPass);

}
