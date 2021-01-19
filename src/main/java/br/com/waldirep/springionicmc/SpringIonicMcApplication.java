package br.com.waldirep.springionicmc;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class SpringIonicMcApplication implements CommandLineRunner { 


	/*@Autowired // Injeção de dependencia para um teste simples ao serviço da AmazonS3
	private S3Service s3Service;*/
	
	public static void main(String[] args) {
		SpringApplication.run(SpringIonicMcApplication.class, args);
	}

	/**
	 * Metodo que instacia automaticamente ao subir o projeto. Precisa do implements CommandLineRunner
	 */
	@Override
	public void run(String... args) throws Exception {

		//s3Service.uploadFile("C:\\amazonS3\\fotos\\java-logo.jpg");
	}

}
