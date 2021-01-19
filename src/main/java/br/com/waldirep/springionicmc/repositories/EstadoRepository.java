package br.com.waldirep.springionicmc.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.waldirep.springionicmc.domain.Estado;


@Repository
public interface EstadoRepository extends JpaRepository<Estado, Integer> {
	
	
	/*
	 * Metodo que busca todos os estados ordenados por nome
	 * 
	 * Metodo criado pelo padrao de nomes do SpringData
	 * 
	 * @Transactional(readOnly = true) => A operação não precisa ser envolvida com uma transação no banco de dados
	 */
	@Transactional(readOnly = true)
	public List<Estado> findAllByOrderByNome();
	
	

}
