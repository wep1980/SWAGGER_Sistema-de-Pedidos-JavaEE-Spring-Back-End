package br.com.waldirep.springionicmc.services.validation;

import java.util.ArrayList;
import java.util.List;

import javax.validation.ConstraintValidator;
import javax.validation.ConstraintValidatorContext;

import org.springframework.beans.factory.annotation.Autowired;

import br.com.waldirep.springionicmc.domain.Cliente;
import br.com.waldirep.springionicmc.domain.unums.TipoCliente;
import br.com.waldirep.springionicmc.dto.ClienteNewDTO;
import br.com.waldirep.springionicmc.repositories.ClienteRepository;
import br.com.waldirep.springionicmc.resources.exception.FieldMessage;
import br.com.waldirep.springionicmc.services.validation.utils.BR;

/**
 * ClienteInsert => Nome da anotação
 * ClienteNewDTO => Classe que vai aceitar a anotação
 * @author Waldir
 *
 */
public class ClienteInsertValidator implements ConstraintValidator<ClienteInsert, ClienteNewDTO> {
	
	
	@Autowired
	private ClienteRepository clienteRepository;
	
	
	/**
	 * Método de inicialização
	 */
	@Override
	public void initialize(ClienteInsert ann) {
	}
	
	
	/**
	 * Método que contém a regra de validação do CPF ou CNPJ
	 * Retorna true ou false
	 */
	@Override
	public boolean isValid(ClienteNewDTO objDto, ConstraintValidatorContext context) {
		
		// Criação de uma lista vazia do FieldMessage, classe auxiliar das exceptions customizadas
		List<FieldMessage> list = new ArrayList<>();
		
		// Se for igual ao tipo pessoa fisica e se o CPF não for valido
		if(objDto.getTipoCliente().equals(TipoCliente.PESSOAFISICA.getCodigo()) && !BR.isValidCPF(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CPF invalido"));
		}
		
		// Se for igual ao tipo pessoa juridica e se o CNPJ não for valido
		if(objDto.getTipoCliente().equals(TipoCliente.PESSOAJURIDICA.getCodigo()) && !BR.isValidCNPJ(objDto.getCpfOuCnpj())) {
			list.add(new FieldMessage("cpfOuCnpj", "CNPJ invalido"));
		}
		
		// Regra para verificação de emails iguais
		Cliente aux = clienteRepository.findByEmail(objDto.getEmail());
		if(aux != null) {
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
