package br.com.waldirep.springionicmc.repositories;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;

import br.com.waldirep.springionicmc.domain.Categoria;
import br.com.waldirep.springionicmc.domain.Produto;


@Repository
public interface ProdutoRepository extends JpaRepository<Produto, Integer> {

	/**
	 * @Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.name LIKE %:nome% AND cat IN :categorias")
	 * Consulta JPQL 
	 * 
	 * @Param("nome") => pega o valor da variavel e joga no %:nome% da consulta JPQL
	 * @Param("categorias") List<Categoria> categorias => pega o valor da variavel e joga no :categorias da consulta JPQL
	 * 
	 * @Transactional(readOnly = true) => A operação não precisa ser envolvida com uma transação no banco de dados
	 * ou seja fica mais rapida e diminui o locking no gerenciamento de transações do banco de dados
	 * 
	 * @param nome
	 * @param categorias
	 * @param pageRequest
	 * @return
	 */
	@Transactional(readOnly = true)
	@Query("SELECT DISTINCT obj FROM Produto obj INNER JOIN obj.categorias cat WHERE obj.nome LIKE %:nome% AND cat IN :categorias")
	Page<Produto> search(@Param("nome") String nome,@Param("categorias") List<Categoria> categorias, Pageable pageRequest);
	
	
	/*
	 * Método feito utilizando o padrão de nomes ( Faz a mesma coisa que o método acima )
	 * https://docs.spring.io/spring-data/jpa/docs/current/reference/html/#jpa.query-methods.at-query (5.3.2)
	 */
	@Transactional(readOnly = true)
	Page<Produto> findDistinctByNomeContainingAndCategoriasIn(String nome, List<Categoria> categorias, Pageable pageRequest);
	

}
