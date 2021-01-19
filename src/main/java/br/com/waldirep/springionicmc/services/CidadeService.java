package br.com.waldirep.springionicmc.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import br.com.waldirep.springionicmc.domain.Cidade;
import br.com.waldirep.springionicmc.repositories.CidadeRepository;

@Service
public class CidadeService {
	
	
	@Autowired
	private CidadeRepository repo;
	
	
	/**
	 * Método que busca o estado pelo ID
	 * @param estadoId
	 * @return
	 */
	public List<Cidade> findByEstado(Integer estadoId){
		return repo.findCidades(estadoId);
	}

}
