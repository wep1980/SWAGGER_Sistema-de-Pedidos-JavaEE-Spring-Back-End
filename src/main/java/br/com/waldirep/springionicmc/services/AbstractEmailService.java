package br.com.waldirep.springionicmc.services;

import java.util.Date;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.thymeleaf.TemplateEngine;
import org.thymeleaf.context.Context;

import br.com.waldirep.springionicmc.domain.Cliente;
import br.com.waldirep.springionicmc.domain.Pedido;

public abstract class AbstractEmailService implements EmailService {

	/*
	 * Variavel para pegar o valor default.sender= do arquivo application.properties
	 */
	@Value("${default.sender}")
	private String sender;

	@Autowired
	private TemplateEngine templateEngine;

	@Autowired
	private JavaMailSender javaMailSender;

	
	
	/**
	 * Metodo de envio de confirmação de pedido
	 * Envio do email de texto plano
	 */
	@Override
	public void sendOrderConfirmationEmail(Pedido obj) {

		SimpleMailMessage sm = prepareSimpleMailMessageFromPedido(obj);
		sendEmail(sm);
	}
	
	

	protected SimpleMailMessage prepareSimpleMailMessageFromPedido(Pedido obj) {

		SimpleMailMessage sm = new SimpleMailMessage();

		sm.setTo(obj.getCliente().getEmail()); // Destinatario do email
		sm.setFrom(sender); // remetente do email
		sm.setSubject("Pedido confirmado! Código: " + obj.getId()); // Assunto do email
		sm.setSentDate(new Date(System.currentTimeMillis())); // Data do email com horario do servidor
		sm.setText(obj.toString()); // Corpo do email

		return sm;
	}

	
	/**
	 * Método responsável por retornar o HTML preenchido com os dados de um pedido,
	 * a partir do template Thymeleaf
	 * 
	 * @param obj
	 * @return
	 */
	protected String htmlFromTemplatePedido(Pedido obj) {

		Context context = new Context();

		context.setVariable("pedido", obj); // Define que o template vai utilizar o objeto obj com apelido de pedido
		templateEngine.process("email/confirmacaoPedido", context); // Processar o template e retornar o HTML em String

		return templateEngine.process("email/confirmacaoPedido", context);

	}

	
	/**
	 * Metodo de envio de confirmação de email Envio HTML
	 * 
	 * OBS : Se acontecer uma MessagingException na hora de gerar o email HTML, sera enviado o email texto plano
	 * 
	 */
	@Override
	public void sendOrderConfirmationHtmlEmail(Pedido obj) {

		try {
			
		    MimeMessage mm = prepareMimeMessageFromPedido(obj);
		    sendHtmlEmail(mm);
		    
		   } catch (MessagingException e) {
			   
			   sendOrderConfirmationEmail(obj);
		}
	}

	
	/**
	 * Pegando o pedido e gerando um objeto do tipo MimeMessage
	 * @param obj
	 * @return
	 * @throws MessagingException 
	 */
	protected MimeMessage prepareMimeMessageFromPedido(Pedido obj) throws MessagingException {
		
         MimeMessage mimeMessage = javaMailSender.createMimeMessage();
         
         MimeMessageHelper mmh = new MimeMessageHelper(mimeMessage, true);
         
         mmh.setTo(obj.getCliente().getEmail()); // Para quem vai ser enviado o email
         mmh.setFrom(sender); // Quem e o remetente do email
         mmh.setSubject("Pedido confirmado! Código: " + obj.getId()); // Assunto email
         mmh.setSentDate(new Date(System.currentTimeMillis())); // Data e hora do email
         mmh.setText(htmlFromTemplatePedido(obj), true); // Corpo do email
         
         return mimeMessage;
	}
	
	
	/**
	 * Metodo que envia uma nova senha para o email do usuario
	 */
	@Override
	public void sendNewPasswordEmail(Cliente cliente, String newPass) {
		SimpleMailMessage sm = prepareNewPasswordEmail(cliente, newPass);
		sendEmail(sm);
	}



	protected SimpleMailMessage prepareNewPasswordEmail(Cliente cliente, String newPass) {
		
		SimpleMailMessage sm = new SimpleMailMessage();

		sm.setTo(cliente.getEmail()); // Destinatario do email
		sm.setFrom(sender); // remetente do email
		sm.setSubject("Solicitação de nova senha"); // Assunto do email
		sm.setSentDate(new Date(System.currentTimeMillis())); // Data do email com horario do servidor
		sm.setText("Nova senha: " + newPass); // Corpo do email

		return sm;
	}

}
