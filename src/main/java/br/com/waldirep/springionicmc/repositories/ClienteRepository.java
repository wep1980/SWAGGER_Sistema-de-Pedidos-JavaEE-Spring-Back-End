package br.com.waldirep.springionicmc.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.waldirep.springionicmc.domain.Cliente;

@Repository
public interface ClienteRepository extends JpaRepository<Cliente, Integer>{
	
	
	/**
	 * Busca por email
	 * 
	 * Recurso padrão de nomes - Método criado automáticamente pelo SpringData com consulta JPQL que busca no banco de dados por email.
	 * @Transactional(readOnly = true) => A operação não precisa ser envolvida com uma transação no banco de dados
	 * ou seja fica mais rapida e diminui o locking no gerenciamento de transações do banco de dados
	 * @param email
	 * @return
	 */
	@Transactional(readOnly = true)
	Cliente findByEmail(String email);

}
