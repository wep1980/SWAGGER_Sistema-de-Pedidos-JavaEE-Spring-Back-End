package br.com.waldirep.springionicmc.resources;

import java.util.List;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

import br.com.waldirep.springionicmc.domain.Cidade;
import br.com.waldirep.springionicmc.domain.Estado;
import br.com.waldirep.springionicmc.dto.CidadeDTO;
import br.com.waldirep.springionicmc.dto.EstadoDTO;
import br.com.waldirep.springionicmc.services.CidadeService;
import br.com.waldirep.springionicmc.services.EstadoService;

@RestController
@RequestMapping(value = "/estados")
public class EstadoResource {
	
	
	@Autowired
	private EstadoService service;
	
	@Autowired
	private CidadeService cidadeService;
	
	
	/**
	 * Metodo que busca todos os estados
	 * ENDPOINT que busca todos os estados
	 * @return
	 */
	@RequestMapping(method = RequestMethod.GET)
	public ResponseEntity<List<EstadoDTO>> findAll(){
		
		List<Estado> list = service.findAll();
	
		List<EstadoDTO> listDto = list.stream().map(obj -> new EstadoDTO(obj)).collect(Collectors.toList());
		
		return ResponseEntity.ok().body(listDto);
	}
	
	
	/**
	 * Metodo que retorna a lista de cidades relacionada ao estado escolhido
	 * 
	 * @param estadoId -> Pega o valor do estado selecionado pelo usuario na URL
	 * @return
	 * 
	 * OBS : Este ENDPOINT foi colocado aqui no recurso de estado por ser fortemente acoplado aos estados
	 */
	@RequestMapping(value = "/{estadoId}/cidades", method = RequestMethod.GET)
	public ResponseEntity<List<CidadeDTO>> findCidades(@PathVariable Integer estadoId){
		
		List<Cidade> list = cidadeService.findByEstado(estadoId); // retorna a lista de cidades proveniente ao estado selecionado
		
		List<CidadeDTO> listDto = list.stream().map(obj -> new CidadeDTO(obj)).collect(Collectors.toList()); // Converte a lista de Cidades para uma lista de CidadeDTO
		
		return ResponseEntity.ok().body(listDto); // Retorna a lista de CidadeDTO
		
	}
	

}
