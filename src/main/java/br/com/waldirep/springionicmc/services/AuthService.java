package br.com.waldirep.springionicmc.services;

import java.util.Random;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.waldirep.springionicmc.domain.Cliente;
import br.com.waldirep.springionicmc.repositories.ClienteRepository;
import br.com.waldirep.springionicmc.services.exceptions.ObjectNotFoundException;

@Service // Componente do framework
public class AuthService {
	
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	@Autowired
	private BCryptPasswordEncoder pe;
	
	@Autowired
	private EmailService emailService;
	
	private Random rand = new Random(); // Classe do JAVA que gera valores aleatorios
	
	
	
	public void sendNewPassword(String email){
		
		//Verifica se o email existe
		Cliente cliente = clienteRepository.findByEmail(email);
		if(cliente == null) {
			throw new ObjectNotFoundException("Email não encontrado");
		}
		
		/*
		 * newPassword() -> Método que implementa uma nova senha aleatoria
		 */
		String newPass = newPassword(); // Cria uma nova senha aleatoria
		cliente.setSenha(pe.encode(newPass)); // Seta a nova senha criptografada
		
		clienteRepository.save(cliente); // Salva o cliente
		emailService.sendNewPasswordEmail(cliente, newPass); // Envia o email
	}



	/**
	 * Método auxiliar que cria uma nova senha auxiliar
	 * @return
	 */
	private String newPassword() {
		
		char[] vet = new char[10];
		for(int i = 0; i < 10; i++) {
			vet[i] = randomChar(); // randomChar() -> Método para gerar um caracter aleatorio que pode ser digito ou letra
			
		}
		return new String(vet);
	}



	/**
	 * Método auxiliar que gera caracteres, que pode ser digitos ou letras
	 * 
	 * digito = 0
	 * letra maiuscula = 1
	 * letra minuscula = 2
	 * 
	 * @return
	 */
	private char randomChar() {
		int opt = rand.nextInt(3); // Gera um numero inteiro de 0 até 2 = 0,1,2
		if(opt == 0) { // Gera um digito
			return (char) (rand.nextInt(10) + 48);
		}else if(opt == 1) { // Gera letra maiuscula
			return (char) (rand.nextInt(26) + 65);
		}else { // Gera letra minuscula
			return (char) (rand.nextInt(26) + 97);
		}
	}

}
