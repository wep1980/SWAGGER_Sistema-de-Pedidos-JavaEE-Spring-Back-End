package br.com.waldirep.springionicmc.services;

import java.util.Date;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import br.com.waldirep.springionicmc.domain.Cliente;
import br.com.waldirep.springionicmc.domain.ItemPedido;
import br.com.waldirep.springionicmc.domain.PagamentoComBoleto;
import br.com.waldirep.springionicmc.domain.Pedido;
import br.com.waldirep.springionicmc.domain.unums.EstadoPagamento;
import br.com.waldirep.springionicmc.repositories.ItemPedidoRepository;
import br.com.waldirep.springionicmc.repositories.PagamentoRepository;
import br.com.waldirep.springionicmc.repositories.PedidoRepository;
import br.com.waldirep.springionicmc.security.UserSS;
import br.com.waldirep.springionicmc.services.exceptions.AuthorizationException;
import br.com.waldirep.springionicmc.services.exceptions.ObjectNotFoundException;

@Service
public class PedidoService {
	
	@Autowired
	private PedidoRepository pedidoRepository;
	
	@Autowired
	private BoletoService boletoService;
	
	@Autowired
	private PagamentoRepository pagamentoRepository;
	
	@Autowired
	private ProdutoService produtoService;
	
	@Autowired
	private ItemPedidoRepository itemPedidoRepository;
	
	@Autowired
	private ClienteService clienteService;
	
	@Autowired
	private EmailService emailService;
	
	
	public Pedido find(Integer id) {
		
		Optional<Pedido> pedido = pedidoRepository.findById(id);
		
		/* orElseThrow - Método que recebe uma função que instancia uma excessão */
		return pedido.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " 
				+ Pedido.class.getName()));
		
	}


	@Transactional
	public  Pedido insert(Pedido obj) {
		
		obj.setId(null); // Garante que esta sendo inserido um novo pedido
		obj.setInstante(new Date()); // Criando uma nova data com o instante atual
		obj.setCliente(clienteService.find(obj.getCliente().getId())); //Buscando do BD o cliente inteiro
		obj.getPagamento().setEstadoPagamento(EstadoPagamento.PENDENTE); // Um novo pedido sempre tem o estado de pagamento Pendente
		obj.getPagamento().setPedido(obj); // Associação de mão dupla, o pagamento também tem que conhecer o pedido dele
		
		/*
		 * Como não temos um webService que gera um boleto que informa a data de vencimento, a nossa regra de negócio sera que após 1 semana da data do pedido s
		 * sara a data de vencimento do boleto
		 */
		if(obj.getPagamento() instanceof PagamentoComBoleto) { // Se o pagamento for do tipo boleto sera gerada uma data para ele
			PagamentoComBoleto pagto = (PagamentoComBoleto) obj.getPagamento();
			boletoService.preencherPagamentoComBoleto(pagto, obj.getInstante()); // Método da classe BoletoService que preenche no pagamento a data de vencimento
		}
		obj = pedidoRepository.save(obj);
		pagamentoRepository.save(obj.getPagamento());
		
		
		for (ItemPedido itemPedido : obj.getItens()) {
			itemPedido.setDesconto(0.0); // Não estamos trabalhando com desconto por enquanto
			itemPedido.setProduto(produtoService.find(itemPedido.getProduto().getId())); // O itemPedido esta associado ao produto que foi buscado no BD
			itemPedido.setPreco(itemPedido.getProduto().getPreco()); // Pegando o produto e o preço e atribuindo ao item
			itemPedido.setPedido(obj);
		}
		
		itemPedidoRepository.saveAll(obj.getItens());
		emailService.sendOrderConfirmationHtmlEmail(obj); // Enviando email
		return obj;
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
	public Page<Pedido> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		
	
		UserSS user = UserService.authenticated(); // Pegando o usuario logado e verificando se não esta nulo
		if(user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		// Retornando somente os pedidos do USUARIO logado
		Cliente cliente =  clienteService.find(user.getId()); // Pegando o cliente logado
		return pedidoRepository.findByCliente(cliente, pageRequest); // Retorna os pedidos do cliente
		
	}

}
