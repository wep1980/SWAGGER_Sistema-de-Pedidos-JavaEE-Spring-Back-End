package br.com.waldirep.springionicmc.services;

import java.awt.Color;
import java.awt.image.BufferedImage;
import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.IOException;
import java.io.InputStream;

import javax.imageio.ImageIO;

import org.apache.commons.io.FilenameUtils;
import org.imgscalr.Scalr;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import br.com.waldirep.springionicmc.services.exceptions.FileException;

/**
 * Classe responsavel por fornecer funcionalidades de imagem
 * @author wepbi
 *
 */
@Service
public class ImageService {
	
	
	
	public BufferedImage getJpgImageFromFile(MultipartFile uploadedFile) {
		
		String extensao = FilenameUtils.getExtension(uploadedFile.getOriginalFilename()); // Pegando a extensão do arquivo
		if(!"png".equals(extensao) && !"jpg".equals(extensao)) {
			throw new FileException("Somente imagens PNG e JPG são permitidas");
		}
		
		try {
			BufferedImage img = ImageIO.read(uploadedFile.getInputStream());
			if("png".equals(extensao)) {
				img = pngToJpg(img); // Método que converte de PNG para JPG
			}
			return img;
		} catch (IOException e) {
		   throw new FileException("Erro ao ler o arquivo");
		}
		
	}
	
	

	// Método que converte uma arquivo PNG para JPG
	public BufferedImage pngToJpg(BufferedImage img) {
		
		BufferedImage jpgImage = new BufferedImage(img.getWidth(), img.getHeight(), BufferedImage.TYPE_INT_RGB);
		jpgImage.createGraphics().drawImage(img, 0, 0, Color.WHITE, null);
		
		return jpgImage;
	}
	
	
	/**
	 * Metodo que retorna um inputStream, que encapsula a leitura a partir de um bufferedImage
	 * 
	 * OBS: O Método que faz o upload para o S3 ele recebe um inputStream, por isso e necessario esse metodo
	 * @param img
	 * @param extensao
	 * @return
	 */
	public InputStream getInputStream(BufferedImage img, String extensao) {
		
		try {
			ByteArrayOutputStream os = new ByteArrayOutputStream();
			ImageIO.write(img, extensao, os);
			return new ByteArrayInputStream(os.toByteArray());
			
			
		} catch (IOException e){	
			throw new FileException("Erro ao ler arquivo");
		}
	}
	
	
	// Metodo que define um tamanho da imagem padrão
	public BufferedImage cropSquare (BufferedImage sourceImg) {
		
		// Descobre qual e o tamanho minimo, a altura ou a largura -- Condição ternaria
		int min = (sourceImg.getHeight() <= sourceImg.getWidth()) ? sourceImg.getHeight() : sourceImg.getWidth(); // Se a altura for menor ou igual a largura, então e a altura, senão e a largura
		
		// Aqui recorta a imagem
		return Scalr.crop(
				sourceImg,
				(sourceImg.getWidth()/2) - (min/2),
				(sourceImg.getHeight()/2) - (min/2),
				min,
				min);
	}

	
	/**
	 *  Método que redimesiona a imagem
	 * @param sourceImg -> recebe uma imagem
	 * @param size -> recebe o tamanho da imagem
	 * @return
	 */
	public BufferedImage resize (BufferedImage sourceImg, int size) {
		return Scalr.resize(sourceImg, Scalr.Method.ULTRA_QUALITY, size);
	}
}
