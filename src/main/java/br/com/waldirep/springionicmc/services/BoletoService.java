package br.com.waldirep.springionicmc.services;

import java.util.Calendar;
import java.util.Date;

import org.springframework.stereotype.Service;

import br.com.waldirep.springionicmc.domain.PagamentoComBoleto;

@Service
public class BoletoService {
	
	
	/**
	 * Método que acrescenta uma data de vencimento aos pagamentos com boleto. Na vida real seria utilizado um webService 
	 * @param pagto
	 * @param instanteDoPedido
	 */
	public void preencherPagamentoComBoleto(PagamentoComBoleto pagto, Date instanteDoPedido) {
		
		Calendar cal = Calendar.getInstance();
		cal.setTime(instanteDoPedido);
		cal.add(Calendar.DAY_OF_MONTH, 7);
		pagto.setDataVencimento(cal.getTime());
	}

}
