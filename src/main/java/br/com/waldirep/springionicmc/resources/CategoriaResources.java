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
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.waldirep.springionicmc.domain.Categoria;
import br.com.waldirep.springionicmc.dto.CategoriaDTO;
import br.com.waldirep.springionicmc.services.CategoriaService;



@RestController
@RequestMapping(value = "/categorias")
public class CategoriaResources {
	
	@Autowired
	private CategoriaService service;
	
	
	/**
	 * Este END POINT recebe /categorias/id ( Recebe o id digitado )
	 * O END POINT recebe o id da URL atraves da anotação @PathVariable
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET)  
	public ResponseEntity<Categoria> find (@PathVariable Integer id) {  
		
		Categoria obj = service.find(id);
		
		return ResponseEntity.ok().body(obj);
	}
	
	
	/**
	 * RequestMethod.POST -> Insere
	 * O método save retorna um objeto
	 * Para o objeto ser construído a partir dos dados JSON enviado é necessario colocar a anotação @RequestBody
	 * @param obj
	 * @return
	 */
	@PreAuthorize("hasAnyRole('ADMIN')") // Autorização por perfil -> Apenas quem é ADMIN tem acesso -> Configurado na classe securityConfig com @EnableGlobalMethodSecurity(prePostEnabled = true)
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody CategoriaDTO objDto){
		
		Categoria obj = service.fromDTO(objDto);
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri(); // Captura a URi do novo recurso inserido
		return ResponseEntity.created(uri).build();
	}
	
	
	/**
	 * @RequestBody Categoria id => recebe o objeto JSON
	 *  @PathVariable Integer id => Recebe o paramêtro na URL
	 * @return
	 */
	@PreAuthorize("hasAnyRole('ADMIN')") // Autorização por perfil -> Apenas quem é ADMIN tem acesso -> Configurado na classe securityConfig com @EnableGlobalMethodSecurity(prePostEnabled = true)
	@RequestMapping(value = "/{id}", method = RequestMethod.PUT)
	public ResponseEntity<Void> update(@Valid @RequestBody CategoriaDTO objDto, @PathVariable Integer id){
		
		Categoria obj = service.fromDTO(objDto);
		
		obj.setId(id); // Garante que a categoria que vai ser atualizada e a passada na URL
		obj = service.update(obj);
		return ResponseEntity.noContent().build();
	}
	
	
	
	@PreAuthorize("hasAnyRole('ADMIN')") // Autorização por perfil -> Apenas quem é ADMIN tem acesso -> Configurado na classe securityConfig com @EnableGlobalMethodSecurity(prePostEnabled = true)
	@RequestMapping(value = "/{id}", method = RequestMethod.DELETE)  
	public ResponseEntity<Void> delete (@PathVariable Integer id) {  
		
		service.delete(id);
		
		return ResponseEntity.noContent().build();	
	}
	
	
	
	@RequestMapping(method = RequestMethod.GET)  
	public ResponseEntity<List<CategoriaDTO>> findAll () {  
		
		List<Categoria> list = service.findAll();
		
		/*
		 * stream() - Percorre a lista
		 * map() - Efetua uma operação para cada elemento da lista
		 * obj - apelido para cada elemento da lista
		 * -> cria uma função
		 * .collect(Collectors.toList()) - transforma o stream para um tipo list
		 */
		List<CategoriaDTO> listDto = list.stream().map(obj -> new CategoriaDTO(obj)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listDto);
	}
	

	/**
	 * EndPoint de paginação
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
	@RequestMapping(value ="/page",  method = RequestMethod.GET)  
	public ResponseEntity<Page<CategoriaDTO>> findPage (
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(value = "oderBy", defaultValue = "nome") String orderBy, 
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {  // DESC - ordenação contraria
		
		Page<Categoria> list = service.findPage(page, linesPerPage, orderBy, direction);
		
		Page<CategoriaDTO> pageDto = list.map(obj -> new CategoriaDTO(obj));
		
		return ResponseEntity.ok().body(pageDto);
	}

}