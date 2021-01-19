package br.com.waldirep.springionicmc.resources;

import javax.servlet.http.HttpServletResponse;
import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.waldirep.springionicmc.dto.EmailDTO;
import br.com.waldirep.springionicmc.security.JWTUtil;
import br.com.waldirep.springionicmc.security.UserSS;
import br.com.waldirep.springionicmc.services.AuthService;
import br.com.waldirep.springionicmc.services.UserService;

/**
 * Classe responsavel por disponibilizar ENDPOINTS relacionados a autenticação e autorização
 * @author wepbi
 *
 */
@RestController
@RequestMapping(value = "/auth")
public class AuthResource {
	
	@Autowired
	JWTUtil jwtUtil;
	
	@Autowired
	private AuthService service;
	

	/**
	 * Método ( ENDPOINT ) que atualiza o token 
	 * 
	 * Quando o token esta proximo de expirar a aplicação acessa esse ENDPOINT e pega um token novo
	 * 
	 * OBS : O token para acessar esse ENDPOINT precisa ainda estar valido
	 * OBS : O USUARIO precisa estar logado para acessar esse ENDPOINT
	 * 
	 * @param response
	 * @return
	 */
	@RequestMapping(value = "/refresh_token", method = RequestMethod.POST)
	public ResponseEntity<Void> refreshToken(HttpServletResponse response) {
		UserSS user = UserService.authenticated(); // Pega o usuario logado
		String token = jwtUtil.generateToken(user.getUsername()); // Gera um novo token para o usuario
		response.addHeader("Authorization", "Bearer " + token); // Adiciona o token na resposta da requisição
		response.addHeader("access-control-expose-headers", "Authorization"); // Liberação de CORS, deixa o cabeçalho exposto. Liberação de recursos vindas de requisições diferentes
		
		return ResponseEntity.noContent().build();
	}
	
	
	/**
	 * Método ( ENDPOINT ) que envia uma nova senha para o email do usuario
	 * 
	 * @RequestBody -> Recebe o email atraves de um POST
	 * 
	 * @Valid -> Valida as anotações colocadas na classe EmailDTO. Exemplo: @Email(message = "Email inválido")
	                                                                        private String email;
	 * 
	 * OBS : Foi criada a classe EmailDTO para receber esse email enviado
	 * @param objDto
	 * @return
	 */
	@RequestMapping(value = "/forgot", method = RequestMethod.POST)
	public ResponseEntity<Void> forgot (@Valid @RequestBody EmailDTO objDto) {
		
		service.sendNewPassword(objDto.getEmail());
		
		return ResponseEntity.noContent().build();
	}

}
