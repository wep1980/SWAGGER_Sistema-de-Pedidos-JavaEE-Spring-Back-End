package br.com.waldirep.springionicmc.services;

import java.io.IOException;
import java.io.InputStream;
import java.net.URI;
import java.net.URISyntaxException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.amazonaws.services.s3.AmazonS3;
import com.amazonaws.services.s3.model.ObjectMetadata;

import br.com.waldirep.springionicmc.services.exceptions.FileException;

@Service
public class S3Service {

	@Autowired // Injetando uma instancia do Bean AmazonS3
	private AmazonS3 s3client;

	@Value("${s3.bucket}")
	private String bucketName;

	private Logger LOG = LoggerFactory.getLogger(S3Service.class);

	/**
	 * Metodo provisorio e para teste que faz upload para o AmazonS3
	 * 
	 * @param localFilePath
	 */
	/*
	 * public void uploadFile(String localFilePath) {
	 * 
	 * try { File file = new File(localFilePath); // Objeto do java.io
	 * LOG.info("Iniciando upload"); s3client.putObject(new
	 * PutObjectRequest(bucketName, "teste.jpg", file));
	 * LOG.info("Upload finalizado");
	 * 
	 * 
	 * } catch (AmazonServiceException e) { LOG.info("AmazonServiceException: " +
	 * e.getMessage()); LOG.info("Status code: " + e.getErrorCode());
	 * 
	 * } catch (AmazonClientException e) { LOG.info("AmazonClientException: " +
	 * e.getMessage()); } }
	 */

	
	
	/**
	 * Método que envia para S3Amazon um arquivo que vem na requisição
	 * 
	 * retorna um objeto do tipo URI com o endereço WEB do novo recurso que foi
	 * gerado
	 * 
	 * O Método foi sobreEscrito abaixo para aumentar a modularidade do serviço
	 * 
	 * @param multipartFile
	 */
	public URI uploadFile(MultipartFile multipartFile) {

		try {
		
		String fileName = multipartFile.getOriginalFilename(); // Pega o nome do arquivo que foi enviado
		InputStream is = multipartFile.getInputStream(); // Objeto basico de leitura que encapsula o processamento de leitura de um arquivo
		String contentType = multipartFile.getContentType(); // String que contem a informação do tipo do arquivo( Image, text ....) que foi enviado 
		
		return uploadFile(is, fileName, contentType);
		
		} catch (IOException e) {
			throw new FileException("Erro de IO: " + e.getMessage());
		}

	}

	
	
	/**
	 * Método sobreEscrito
	 * 
	 * @param is
	 * @param fileName
	 * @param contentType
	 * @return
	 */
	public URI uploadFile(InputStream is, String fileName, String contentType) {

		try {
			ObjectMetadata meta = new ObjectMetadata();
			meta.setContentType(contentType);

			LOG.info("Iniciando upload");
			s3client.putObject(bucketName, fileName, is, meta);
			LOG.info("Upload finalizado");

			return s3client.getUrl(bucketName, fileName).toURI(); // Convertendo a URL para URi
			
		} catch (URISyntaxException e) { // Esse erro e praticamente impossivel de acontecer pois essa URL vem direto da Amazon, mas como o compilador não sabe temos que colocar esse tratamento
			throw new FileException("Erro ao converter URL para URI");
		}

	}
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	
	

}
