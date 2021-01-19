package br.com.waldirep.springionicmc.resources.utils;

import java.io.UnsupportedEncodingException;
import java.net.URLDecoder;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/*
 * Classe auxiliar para 
 */
public class URL {

	/**
	 * Método auxiliar, utilizado na classe ProdutoResource método findPage()
	 * Método que pega o nome passado na URL e decodifica espaços em brancos e caracteres especias
	 */
	public static String decodeParam(String s) {
		
		try {
			return URLDecoder.decode(s, "UTF-8");
		} catch (UnsupportedEncodingException e) {
            return "";			
		}
	}
	
/**
 * Método que pega da URL a String de categorias selecionadas e converte para inteiros(porque são os IDs das categorias que vem na requisição
 * 
 * static => O método pode ser utilizado sem a necessidade de instanciar esta classe
 * 
 * @param s
 * @return
 */
	public static List<Integer> decodeIntList(String s){
		
		String[] vet = s.split(",");
		List<Integer> list = new ArrayList<>();
		for(int i = 0; i < vet.length; i++) {
			list.add(Integer.parseInt(vet[i]));
		}
		 return list;
		 /*
		  * Expressão lambda, faz a mesma coisa que o código acima
		  */
		//return Arrays.asList(s.split(",")).stream().map(x -> Integer.parseInt(x)).collect(Collectors.toList());
	}
}
