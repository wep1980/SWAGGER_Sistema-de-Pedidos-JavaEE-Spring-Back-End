package br.com.waldirep.springionicmc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.waldirep.springionicmc.domain.Categoria;
import br.com.waldirep.springionicmc.domain.Produto;
import br.com.waldirep.springionicmc.repositories.CategoriaRepository;
import br.com.waldirep.springionicmc.repositories.ProdutoRepository;
import br.com.waldirep.springionicmc.services.exceptions.ObjectNotFoundException;

@Service
public class ProdutoService {
	
	@Autowired
	private ProdutoRepository produtoRepository;
	
	@Autowired
	private CategoriaRepository categoriaRepository;
	
	
	public Produto find(Integer id) {
		
		Optional<Produto> pedido = produtoRepository.findById(id);
		
		/* orElseThrow - Método que recebe uma função que instancia uma excessão */
		return pedido.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " 
				+ Produto.class.getName()));
		
	}
	
	/**
	 * Busca Páginada
	 * 
	 * @param nome
	 * @param id
	 * @return
	 */
	public Page<Produto> search (String nome, List<Integer> ids, Integer page, Integer linesPerPage, String orderBy, String direction){
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		/*
		 * Busca a lista de ads no Banco de dados
		 */
		List<Categoria> categorias = categoriaRepository.findAllById(ids);
		
		return produtoRepository.search(nome, categorias, pageRequest);
	}

}
