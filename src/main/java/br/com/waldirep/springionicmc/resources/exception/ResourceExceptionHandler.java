package br.com.waldirep.springionicmc.resources.exception;

import javax.servlet.http.HttpServletRequest;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import com.amazonaws.AmazonClientException;
import com.amazonaws.AmazonServiceException;
import com.amazonaws.services.s3.model.AmazonS3Exception;

import br.com.waldirep.springionicmc.services.exceptions.AuthorizationException;
import br.com.waldirep.springionicmc.services.exceptions.DataIntegrityException;
import br.com.waldirep.springionicmc.services.exceptions.FileException;
import br.com.waldirep.springionicmc.services.exceptions.ObjectNotFoundException;

/**
 * Classe de excessões customizadas
 * 
 * Manupulador de erros, claase auxiliar para interceptar as excessões (Filtro)
 * 
 * Todas as excessões do sistema tem a mesma estrutura de erros do Spring
 * 
 * OBS: Código de erro de validação 422 (UNPROCESSABLE_ENTITY) -> Diferencia o erro de validação dos outros erros. Erro de validação de formulario recebe o 422(UNPROCESSABLE_ENTITY) - Tem um tratamento global no sistema
 *      Código de erro de Autorização 403 -> Tem um tratamento global de erro deletando o token que tiver armazenado localmente
 *      
 *      UNPROCESSABLE_ENTITY -> Entidade que não possivel ser processada
 * 
 * @author Waldir
 *
 */
@ControllerAdvice // Gerenciador de tratamento de excessões
public class ResourceExceptionHandler {
	
	

	@ExceptionHandler(ObjectNotFoundException.class) // Indicando que é um tratador de excessão do tipo ObjectNotFoundException
	public ResponseEntity<StandardError> objectNotFound(ObjectNotFoundException e, HttpServletRequest request) {

		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.NOT_FOUND.value(), "Não encontrado", e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(err);
	}
	

	@ExceptionHandler(DataIntegrityException.class) // Indicando que é um tratador de excessão do tipo ObjectNotFoundException
	public ResponseEntity<StandardError> dataIntegrity(DataIntegrityException e, HttpServletRequest request) {

		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Integridade de dados", e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

	/**
	 * Trata o erro de validação de formulario
	 * 
	 * Indicando que é um tratador de excessão do tipo MethodArgumentNotValidException
	 * @param e
	 * @param request
	 * @return
	 */
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<StandardError> validation(MethodArgumentNotValidException e, HttpServletRequest request) {

		ValidationError err = new ValidationError(System.currentTimeMillis(), HttpStatus.UNPROCESSABLE_ENTITY.value(), "Erro de validação", e.getMessage(), request.getRequestURI());
		

		/**
		 * e.getBindingResult().getFieldError() - Acessa toda a lista de campos que aparecem na exceção MethodArgumentNotValidException
		 */
		for (FieldError fe : e.getBindingResult().getFieldErrors()) { // Percorrendo a lista de erros do framework
			err.addError(fe.getField(), fe.getDefaultMessage());
		}

		return ResponseEntity.status(HttpStatus.UNPROCESSABLE_ENTITY).body(err);
	}

	
	
	@ExceptionHandler(AuthorizationException.class) 
	public ResponseEntity<StandardError> authorization(AuthorizationException e, HttpServletRequest request) {

		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.FORBIDDEN.value(), "Acesso negado", e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.FORBIDDEN).body(err);
	}
	
	

//----------------------------------------------------------------------------------------------------------------------------------------------------------------------------	

	
	
	/*
	 * Tratamento de excessão do tipo FileException.class -> upload de arquivos para AmazonS3
	 */
	@ExceptionHandler(FileException.class)
	public ResponseEntity<StandardError> file(FileException e, HttpServletRequest request) {

		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Erro de arquivo", e.getMessage(), request.getRequestURI());
				
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	

	/*
	 * Tratamento de excessão do tipo AmazonServiceException.class -> upload de arquivos para AmazonS3
	 */
	@ExceptionHandler(AmazonServiceException.class)
	public ResponseEntity<StandardError> amazonService(AmazonServiceException e, HttpServletRequest request) {

		HttpStatus code = HttpStatus.valueOf(e.getErrorCode());

		StandardError err = new StandardError(System.currentTimeMillis(), code.value(), "Erro Amazon Service", e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(code).body(err);
	}
	

	/*
	 * Tratamento de excessão do tipo AmazonClientException.class -> upload de arquivos para AmazonS3
	 */
	@ExceptionHandler(AmazonClientException.class) 
	public ResponseEntity<StandardError> amazonCliente(AmazonClientException e, HttpServletRequest request) {

		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Erro Amazon Client", e.getMessage(), request.getRequestURI());
			

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}
	

	/*
	 * Tratamento de excessão do tipo AmazonS3Exception.class -> upload de arquivos para AmazonS3
	 */
	@ExceptionHandler(AmazonS3Exception.class)
	public ResponseEntity<StandardError> amazonS3(AmazonS3Exception e, HttpServletRequest request) {

		StandardError err = new StandardError(System.currentTimeMillis(), HttpStatus.BAD_REQUEST.value(), "Erro S3", e.getMessage(), request.getRequestURI());

		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(err);
	}

}
