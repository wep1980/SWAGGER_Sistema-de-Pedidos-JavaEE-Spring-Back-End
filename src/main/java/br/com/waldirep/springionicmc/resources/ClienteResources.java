package br.com.waldirep.springionicmc.resources;

import java.net.URI;
import java.util.List;
import java.util.stream.Collectors;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.waldirep.springionicmc.domain.Cliente;
import br.com.waldirep.springionicmc.dto.ClienteDTO;
import br.com.waldirep.springionicmc.dto.ClienteNewDTO;
import br.com.waldirep.springionicmc.services.ClienteService;



@RestController
@RequestMapping(value = "/clientes")
public class ClienteResources {
	
	@Autowired
	private ClienteService service;
	
	/**
	 * Método que busca um USUARIO
	 * 
	 * ENDPOINT de acesso para clientes logados, eles podem acessar somente eles mesmo
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET) // Este END POINT recebe /categorias/id ( Recebe o id digitado )
	public ResponseEntity<Cliente> find (@PathVariable Integer id) {     // O END POINT recebe o id da URL atraves da anotação @PathVariable
		
		Cliente cliente = service.find(id);
		
		return ResponseEntity.ok().body(cliente);
	}
	
	
	/**
	 * Metodo que busca por email
	 * 
	 * ENDPOINT que retorna um email, recebendo como parametro o email digitado pelo usuario
	 * @param email
	 * @return
	 */
	@RequestMapping(value = "/email", method = RequestMethod.GET) // Este END POINT recebe /categorias/id ( Recebe o id digitado )
	public ResponseEntity<Cliente> find (@RequestParam(value = "value") String email) {     // O END POINT recebe o id da URL atraves da anotação @PathVariable
		
		Cliente cliente = service.findByEmail(email);
		
		return ResponseEntity.ok().body(cliente);
	}
	
	
	
	/**
	 * ENDPOINT liberado para TODOS os USUARIOS
	 * 
	 * RequestMethod.POST -> Insere
	 * O método save retorna um objeto
	 * Para o objeto ser construído a partir dos dados JSON enviado é necessario colocar a anotação @RequestBody
	 * @param obj
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody ClienteNewDTO objDto){
		
		Cliente obj = service.fromDTO(objDto);
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri(); // Captura a URi do novo recurso inserido
		return ResponseEntity.created(uri).build();
	}
	
	
	/**
	 * ENDPOINT liberado para alterações somente para USUARIOS CADASTRADOS e sendo o proprio
	 * 
	 * Método que atualiza 
	 * 
	 * @RequestBody Cliente id => recebe o objeto JSON
	 *  @PathVariable Integer id => Recebe o paramêtro na URL
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody ClienteDTO objDto, @PathVariable Integer id){
		
		Cliente obj = service.fromDTO(objDto);
		
		obj.setId(id); // Garante que a categoria que vai ser atualizada e a passada na URL
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	/**
	 * ENDPOINT de acesso apenas para ADMIN
	 * 
	 * Método que apaga um USUARIO
	 * @param id
	 * @return
	 */
	@PreAuthorize("hasAnyRole('ADMIN')") // Autorização por perfil -> Apenas quem é ADMIN tem acesso -> Configurado na classe securityConfig com @EnableGlobalMethodSecurity(prePostEnabled = true)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)  
	public ResponseEntity<Void> delete (@PathVariable Integer id) {  
		
		service.delete(id);
		
		return ResponseEntity.noContent().build();	
	}
	
	
	/**
	 * ENDPOINT de acesso apenas para ADMIN
	 * 
	 * Método que lista todos os USUARIOS
	 * @return
	 */
	@PreAuthorize("hasAnyRole('ADMIN')") // Autorização por perfil -> Apenas quem é ADMIN tem acesso -> Configurado na classe securityConfig com @EnableGlobalMethodSecurity(prePostEnabled = true)
	@RequestMapping(method = RequestMethod.GET)  
	public ResponseEntity<List<ClienteDTO>> findAll () {  
		
		List<Cliente> list = service.findAll();
		
		/*
		 * stream() - Percorre a lista
		 * map() - Efetua uma operação para cada elemento da lista
		 * obj - apelido para cada elemento da lista
		 * -> cria uma função
		 * .collect(Collectors.toList()) - transforma o stream para um tipo list
		 */
		List<ClienteDTO> listDto = list.stream().map(obj -> new ClienteDTO(obj)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listDto);
	}
	
	

	/**
	 * Método de paginação
	 * 
	 * ENDPOINT de acesso apenas para ADMIN
	 * 
	 * @RequestParam -> deixa os parametros opicionais
	 * linesPerPage ->  A sugestão é colocar 24 pq ele é multiplo de 1,2,3 e 4
	 * 
	 * direction -> DESC - ordenação contraria
	 * 
	 * @param page
	 * @param linesPerPage
	 * @param orderBy
	 * @param direction
	 * @return
	 */
	@PreAuthorize("hasAnyRole('ADMIN')") // Autorização por perfil -> Apenas quem é ADMIN tem acesso -> Configurado na classe securityConfig com @EnableGlobalMethodSecurity(prePostEnabled = true)
	@RequestMapping(value ="/page",  method = RequestMethod.GET)  
	public ResponseEntity<Page<ClienteDTO>> findPage (
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(value = "oderBy", defaultValue = "nome") String orderBy, 
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {  // DESC - ordenação contraria
		
		Page<Cliente> list = service.findPage(page, linesPerPage, orderBy, direction);
		
		Page<ClienteDTO> pageDto = list.map(obj -> new ClienteDTO(obj));
		
		return ResponseEntity.ok().body(pageDto);
	}
	
	
	
	/**
	 * Método ENDPOINT - Para o envio de uma imagem de um cliente
	 * 
	 * @RequestParam(name="file") -> Recebe um parametro da requisição HTTP
	 * @param file
	 * @return
	 */
	@RequestMapping(value = "/picture", method = RequestMethod.POST)
	public ResponseEntity<Void> uploadProfilepicture(@RequestParam(name="file") MultipartFile file){
		
		URI uri = service.uploadProfilePicture(file);
		
		return ResponseEntity.created(uri).build();
	}

	
	

}