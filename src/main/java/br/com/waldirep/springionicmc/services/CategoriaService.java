package br.com.waldirep.springionicmc.services;

import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;

import br.com.waldirep.springionicmc.domain.Categoria;
import br.com.waldirep.springionicmc.dto.CategoriaDTO;
import br.com.waldirep.springionicmc.repositories.CategoriaRepository;
import br.com.waldirep.springionicmc.services.exceptions.DataIntegrityException;
import br.com.waldirep.springionicmc.services.exceptions.ObjectNotFoundException;



@Service
public class CategoriaService {
	
	@Autowired  // Injeção de dependencia, dependencia instanciada automaticamente pelo spring
	private CategoriaRepository categoriaRepository;
	
	
	
	public Categoria find(Integer id) {
		
		Optional<Categoria> categoria = categoriaRepository.findById(id);
		
		/* orElseThrow - Método que recebe uma função que instancia uma excessão */
		return categoria.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " 
				+ Categoria.class.getName()));
	}
	
	/**
	 * Para garantir que esta sendo inserido um objeto novo foi adicionado obj.setId(null)
	 * O objeto novo tem que ter o ID null, se o ID estiver valendo algo o método save vai considerar uma atualização
	 * @param obj
	 * @return
	 */
	public Categoria insert(Categoria obj) {
		obj.setId(null); 
		return categoriaRepository.save(obj);
	}

	
	public Categoria update(Categoria obj) {
		Categoria newObj = find(obj.getId()); // Busca o objeto na base de dados e salva em um novo obj
		updateData(newObj, obj); // Método que atualiza os dados do novo objeto com base no objeto que veio como argumento
		return categoriaRepository.save(newObj);
	}
	
	
	/**
	 * Método auxiliar do update
	 * 
	 * newObj foi buscado na base de dados e atualizado com o obj
	 */
	private void updateData(Categoria newObj, Categoria obj) {
		newObj.setNome(obj.getNome());
	}
	

	public void delete(Integer id) {
		
		find(id);  // Busca o objeto antes, caso ele não exista a excessão ja é tratada
		try {
			categoriaRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir uma categoria que contém produtos");
		}
		
	}

	public List<Categoria> findAll() {

		return categoriaRepository.findAll();
	}
	
	
	/**
	 * Método de paginação
	 * 
	 * OBS : direction -> retorna uma String então é feita a conversão Direction.valueOf(direction)
	 * 
	 * @param page
	 * @param linesPerPage
	 * @param orderBy
	 * @param direction
	 * @return
	 */
	public Page<Categoria> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return categoriaRepository.findAll(pageRequest);
	}

	
	/*
	 * Método que instancia uma Categoria a partir de uma CategoriaDTO
	 */
	public Categoria fromDTO(CategoriaDTO objDto) {
		return new Categoria(objDto.getId(), objDto.getNome());
	}
	
	
}
