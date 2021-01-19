package br.com.waldirep.springionicmc.repositories;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.waldirep.springionicmc.domain.Cliente;
import br.com.waldirep.springionicmc.domain.Pedido;

@Repository
public interface PedidoRepository extends JpaRepository<Pedido, Integer>{
	
	/**
	 * Método que retora uma pagina de pedidos
	 * 
	 * Método criado com padrão de nomes do SpringData com consulta JPQL
	 * 
	 * @Transactional(readOnly = true) => A operação não precisa ser envolvida com uma transação no banco de dados
	 * ou seja fica mais rapida e diminui o locking no gerenciamento de transações do banco de dados
	 */
	@Transactional(readOnly = true)
	Page<Pedido> findByCliente(Cliente cliente, Pageable pageResquest);

}
