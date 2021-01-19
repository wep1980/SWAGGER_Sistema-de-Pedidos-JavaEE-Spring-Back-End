package br.com.waldirep.springionicmc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.waldirep.springionicmc.domain.Estado;
import br.com.waldirep.springionicmc.repositories.EstadoRepository;

@Service // Se não colocar esse objetos não serao injetaveis
public class EstadoService {
	
	
	@Autowired
	private EstadoRepository repo;
	
	
	/**
	 * Metodo que busca todos os estados ordenados por nome
	 * @return
	 */
	public List<Estado> findAll(){
		return repo.findAllByOrderByNome();
	}

}
