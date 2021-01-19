package br.com.waldirep.springionicmc.resources;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import br.com.waldirep.springionicmc.domain.Produto;
import br.com.waldirep.springionicmc.dto.ProdutoDTO;
import br.com.waldirep.springionicmc.resources.utils.URL;
import br.com.waldirep.springionicmc.services.ProdutoService;


/**
 * ENDPOINTS
 * 
 * @author Waldir
 *
 */
@RestController
@RequestMapping(value = "/produtos")
public class ProdutoResource {
	
	@Autowired
	private ProdutoService service;
	
	/**
	 * Método que busca todos os produtos
	 * 
	 * ENDPOINT Liberado para acesso de TODOS os USUARIOS
	 * @param id
	 * @return
	 */
	@RequestMapping(value = "/{id}", method = RequestMethod.GET) // Este END POINT recebe /pedidos/id ( Recebe o id digitado )
	public ResponseEntity<Produto> find (@PathVariable Integer id) {     // O END POINT recebe o id da URL atraves da anotação @PathVariable
		
		Produto produto = service.find(id);
		
		return ResponseEntity.ok().body(produto);
	}
	
	
	/**
	 * Método de paginação
	 * 
	 * ENDPOINT Liberado para acesso de TODOS os USUARIOS
	 * 
	 * @RequestParam -> deixa os parametros opicionais
	 * linesPerPage ->  A sugestão é colocar 24 pq ele é multiplo de 1,2,3 e 4
	 * 
	 * direction -> DESC - ordenação contraria
	 * 
	 * O método GET não aceita o envio de parametros no corpo da requisição e sim recuperar dados
	 * Os dados de consulta são passados como parametros na URL, Exp : http://localhost:8080/produtos/nome=?&categorias=1,3,4
	 * @param page
	 * @param linesPerPage
	 * @param orderBy
	 * @param direction
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)  
	public ResponseEntity<Page<ProdutoDTO>> findPage (
			@RequestParam(value = "nome", defaultValue = "") String nome, // Parametro passado na URL
			@RequestParam(value = "categorias", defaultValue = "") String categorias,  // Parametro passado na URL
			@RequestParam(value = "page", defaultValue = "0") Integer page, 
			@RequestParam(value = "linesPerPage", defaultValue = "24") Integer linesPerPage, 
			@RequestParam(value = "oderBy", defaultValue = "nome") String orderBy, 
			@RequestParam(value = "direction", defaultValue = "ASC") String direction) {  // DESC - ordenação contraria
		
		String nomeDecoded = URL.decodeParam(nome);
		List<Integer> ids = URL.decodeIntList(categorias);
		
		Page<Produto> list = service.search(nomeDecoded, ids, page, linesPerPage, orderBy, direction);
		
		Page<ProdutoDTO> listDto = list.map(obj -> new ProdutoDTO(obj));
		
		return ResponseEntity.ok().body(listDto);
	}


}