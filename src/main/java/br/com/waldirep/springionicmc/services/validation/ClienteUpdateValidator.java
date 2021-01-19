package br.com.waldirep.springionicmc.services.validation;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;

import javax.servlet.http.HttpServletRequest;
import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.servlet.HandlerMapping;

import br.com.waldirep.springionicmc.domain.Cliente;
import br.com.waldirep.springionicmc.dto.ClienteDTO;
import br.com.waldirep.springionicmc.repositories.ClienteRepository;
import br.com.waldirep.springionicmc.resources.exception.FieldMessage;

/**
 * ClienteUpdate => Nome da anotação
 * ClienteDTO => Classe que vai aceitar a anotação e possui o atributo email
 * @author Waldir
 *
 */
public class ClienteUpdateValidator implements ConstraintValidator<ClienteUpdate, ClienteDTO> {
	
	/**
	 * HttpServletRequest => Objeto padrão java para web que vai permitir obter o parametro da URi
	 */
	@Autowired
	private HttpServletRequest request;
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	
	/**
	 * Método de inicialização
	 * Nome da anotação - ClienteUpdate
	 */
	@Override
	public void initialize(ClienteUpdate ann) {
	}
	
	
	/**
	 * Método que contém a regra de validação do CPF ou CNPJ
	 * Retorna true ou false
	 * 
	 * OBS : Quando se faz uma atualização o ID não é passado no corpo do DTO mas pela URI da requisição
	 */
	@Override
	public boolean isValid(ClienteDTO objDto, ConstraintValidatorContext context) {
		
		/**
		 * Função para pegar o parametro( ID ) da Uri
		 * Os atributos são armazenados dentro de um Map paracido com a estrura de um objeto JSON
		 * 
		 * CAST de um objeto gerérico => request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE) para o tipo (Map<String, String>)
		 */
		@SuppressWarnings("unchecked")
		Map<String, String> map = (Map<String, String>) request.getAttribute(HandlerMapping.URI_TEMPLATE_VARIABLES_ATTRIBUTE);
		Integer uriId = Integer.parseInt(map.get("id")); // Conversão com parse de String para inteiro
		
		
		
		// Criação de uma lista vazia do FieldMessage, classe auxiliar das exceptions customizadas
		List<FieldMessage> list = new ArrayList<>();
		
		
		// Regra para verificar se existe emails iguais, mas se for o próprio tentando atualizar é permitido
		Cliente aux = clienteRepository.findByEmail(objDto.getEmail());
		if(aux != null && !aux.getId().equals(uriId)) {
			list.add(new FieldMessage("email", "Email já existente"));
		}

		/**
		 * Transporta a lista de erros personalizados para a lista de erros do framework
		 * 
		 * Percorre a lista de Fieldmessage, e para cada objeto que tiver na lista sera adicionado o erro correspondente na lista
		 * de erros do framework
		 */
		for (FieldMessage e : list) {
			context.disableDefaultConstraintViolation();
			context.buildConstraintViolationWithTemplate(e.getMessage()).addPropertyNode(e.getFieldName())
					.addConstraintViolation();
		}
		return list.isEmpty(); // Se a lista é vazia, significa que não existe erros e retorna true
	}
}
