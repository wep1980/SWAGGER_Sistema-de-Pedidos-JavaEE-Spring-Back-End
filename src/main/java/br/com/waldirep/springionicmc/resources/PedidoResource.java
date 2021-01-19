package br.com.waldirep.springionicmc.resources;

import java.net.URI;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import br.com.waldirep.springionicmc.domain.Pedido;
import br.com.waldirep.springionicmc.services.PedidoService;



@RestController
@RequestMapping(value = "/pedidos")
public class PedidoResource {
	
	@Autowired
	private PedidoService service;
	
	/**
	 * Método que busca um pedido
	 * 
	 * ENDPOINT de acesso para clientes logados, eles podem acessar o proprio pedido
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET) // Este END POINT recebe /pedidos/id ( Recebe o id digitado )
	public ResponseEntity<Pedido> find (@PathVariable Integer id) {     // O END POINT recebe o id da URL atraves da anotação @PathVariable
		
		Pedido pedido = service.find(id);
		
		return ResponseEntity.ok().body(pedido);
	}
	
	
	/**
	 * Método que insere um pedido
	 * 
	 * ENDPOINT de acesso para clientes logados, eles podem acessar o proprio pedido
	 * 
	 * RequestMethod.POST -> Insere
	 * O método save retorna um objeto
	 * Para o objeto ser construído a partir dos dados JSON enviado é necessario colocar a anotação @RequestBody
	 * @param obj
	 * @return
	 */
	@RequestMapping(method = RequestMethod.POST)
	public ResponseEntity<Void> insert(@Valid @RequestBody Pedido obj){
		
		
		obj = service.insert(obj);
		URI uri = ServletUriComponentsBuilder.fromCurrentRequest()
				.path("/{id}").buildAndExpand(obj.getId()).toUri(); // Captura a URi do novo recurso inserido
		return ResponseEntity.created(uri).build();
	}
	
	
	/**
	 * Método que busca os pedidos do cliente
	 * 
	 * ENDPOINT de paginação
	 * 
	 * @RequestParam -> deixa os parametros opicionais
	 * linesPerPage ->  A sugestão é colocar 24 pq ele é multiplo de 1,2,3 e 4
	 * 
	 * direction -> DESC - ordenação decrescente
	 * direction -> ASC - ordenação crescente
	 * 
	 * @param page
	 * @param linesPerPage
	 * @param orderBy
	 * @param direction
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)  
	public ResponseEntity<Page<Pedido>> findPage (
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(value = "oderBy", defaultValue = "instante") String orderBy, // Ordenando os pedidos por data
			@RequestParam(value = "direction", defaultValue = "DESC") String direction) {  // DESC - ordenação decrescente - PARA APARECER OS ULTIMOS PEDIDOS PRIMEIRO
		
		Page<Pedido> list = service.findPage(page, linesPerPage, orderBy, direction);
		
		return ResponseEntity.ok().body(list);
	}

}