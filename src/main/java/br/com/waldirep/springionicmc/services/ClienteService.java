package br.com.waldirep.springionicmc.services;

import java.awt.image.BufferedImage;
import java.net.URI;
import java.util.List;
import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Sort.Direction;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

import br.com.waldirep.springionicmc.domain.Cidade;
import br.com.waldirep.springionicmc.domain.Cliente;
import br.com.waldirep.springionicmc.domain.Endereco;
import br.com.waldirep.springionicmc.domain.unums.Perfil;
import br.com.waldirep.springionicmc.domain.unums.TipoCliente;
import br.com.waldirep.springionicmc.dto.ClienteDTO;
import br.com.waldirep.springionicmc.dto.ClienteNewDTO;
import br.com.waldirep.springionicmc.repositories.ClienteRepository;
import br.com.waldirep.springionicmc.repositories.EnderecoRepository;
import br.com.waldirep.springionicmc.security.UserSS;
import br.com.waldirep.springionicmc.services.exceptions.AuthorizationException;
import br.com.waldirep.springionicmc.services.exceptions.DataIntegrityException;
import br.com.waldirep.springionicmc.services.exceptions.ObjectNotFoundException;



@Service
public class ClienteService {
	
	
	
	@Autowired
	private BCryptPasswordEncoder bCryptPasswordEncoder;
	
	@Autowired  // Injeção de dependencia, dependencia instanciada automaticamente pelo spring
	private ClienteRepository clienteRepository;

	@Autowired
	private EnderecoRepository enderecoRepository;
	
	@Autowired
	private S3Service s3Service;
	
	@Autowired
	private ImageService imageService;
	
	// Pegando o valor da configuração do arquivo application.properties
	@Value("${img.prefix.client.profile}")
	private String prefix;
	
	// Pegando o valor da configuração do arquivo application.properties
	@Value("${img.profile.size}")
	private Integer size;
	
	
	
	/**
	 * Método que busca um USUARIO por ID
	 * 
	 * @param id
	 * @return
	 */
	public Cliente find(Integer id) {
		
		/*
		 * Regra de negócio que verifica se existe um usuario logado
		 */
		UserSS user = UserService.authenticated(); // Pega o usuario logado
		if(user == null || !user.hasRole(Perfil.ADMIN) && !id.equals(user.getId())) { // ( Se o USUARIO logado for igual a null ou nao tiver o perfil de ADMIN ) e o ID não for o do USUARIO logado
			throw new AuthorizationException("Acesso negado");
		}
		
		Optional<Cliente> cliente = clienteRepository.findById(id);
		
		/* orElseThrow - Método que recebe uma função que instancia uma excessão */
		return cliente.orElseThrow(() -> new ObjectNotFoundException("Objeto não encontrado! Id: " + id + ", Tipo: " 
				+ Cliente.class.getName()));
	}
	
	
	
	/**
	 * Método insere um cliente com o endereço
	 * 
	 * @Transactional => Garante que os dados serão inseridos de forma transacional ja que serão inseridos também os endereços
	 * 
	 * Para garantir que esta sendo inserido um objeto novo foi adicionado obj.setId(null)
	 * O objeto novo tem que ter o ID null, se o ID estiver valendo algo o método save vai considerar uma atualização
	 * @param obj
	 * @return
	 */
	@Transactional
	public Cliente insert(Cliente obj) {
		obj.setId(null); 
		obj = clienteRepository.save(obj);
		enderecoRepository.saveAll(obj.getEnderecos());
		return obj;
	}
	
	
	
	public Cliente update(Cliente obj) {
		Cliente newObj = find(obj.getId()); // Busca o objeto na base de dados e salva em um novo obj
		updateData(newObj, obj); // Método que atualiza os dados do novo objeto com base no objeto que veio como argumento
		return clienteRepository.save(newObj);
	}
	
	
	/**
	 * Método auxiliar do update
	 * 
	 * newObj foi buscado na base de dados e atualizado com o obj
	 */
	private void updateData(Cliente newObj, Cliente obj) {
		newObj.setNome(obj.getNome());
		newObj.setEmail(obj.getEmail());
	}
	

	public void delete(Integer id) {
		
		find(id);  // Busca o objeto antes, caso ele não exista a excessão ja é tratada
		try {
			clienteRepository.deleteById(id);
		} catch (DataIntegrityViolationException e) {
			throw new DataIntegrityException("Não é possível excluir porque há pedidos relacionados");
		}
		
	}

	/**
	 * Metodo que retorna todos os clientes
	 * @return
	 */
	public List<Cliente> findAll() {

		return clienteRepository.findAll();
	}
	
	
	/**
	 * Metodo que retorna um cliente por email
	 * Nossa aplicação armazena o token e o carrinho de compras.
	 * O Token carrega dentro dele o nome de usuario(email) e o tempo de expiração
	 * @param email
	 * @return
	 */
	public Cliente findByEmail(String email) {
		
		UserSS user = UserService.authenticated(); // Pega o usuario logado
		if(user == null || !user.hasRole(Perfil.ADMIN) && !email.equals(user.getUsername())) { // ( Se o USUARIO logado for igual a null ou nao tiver o perfil de ADMIN ) e o EMAIL não for o do USUARIO logado
			throw new AuthorizationException("Acesso negado");
		}
		
		Cliente obj = clienteRepository.findByEmail(email);
		if(obj == null) {
			throw new ObjectNotFoundException("Objeto não encontrado! Id: " + user.getId() + ", Tipo: " + Cliente.class.getName());
		}
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
	public Page<Cliente> findPage(Integer page, Integer linesPerPage, String orderBy, String direction){
		
		PageRequest pageRequest = PageRequest.of(page, linesPerPage, Direction.valueOf(direction), orderBy);
		
		return clienteRepository.findAll(pageRequest);
	}

	
	/*
	 * Método que instancia um Cliente a partir de uma ClienteDTO
	 * 
	 * OBS : Não instancia do banco de dados
	 */
	public Cliente fromDTO(ClienteDTO objDto) {
		return new Cliente(objDto.getId(), objDto.getNome(), objDto.getEmail(), null, null, null );
	}
	
	
	/**
	 * Método que instancia um ClienteNewDTO com endereço e telefones
	 * Sobrecarga do método fromDTO()
	 * @param objDto
	 * @return
	 */
	public Cliente fromDTO(ClienteNewDTO objDto) {
		
		Cliente cliente = new Cliente(null, objDto.getNome(), objDto.getEmail(), objDto.getCpfOuCnpj(), 
				TipoCliente.toEnum(objDto.getTipoCliente()), bCryptPasswordEncoder.encode(objDto.getSenha()));
		
		Cidade cidade = new Cidade(objDto.getCidadeId(), null, null);
		
		Endereco endereco = new Endereco(null, objDto.getLogradouro(), objDto.getNumero(), objDto.getComplemento(), 
				objDto.getBairro(), objDto.getCep(), cliente, cidade);
		
		cliente.getEnderecos().add(endereco); // Adicionando os endereços no cliente
		
		/*
		 *  Adicionado o primeiro telefone que é obrigatório, os outros são opicionais
		 */
		cliente.getTelefones().add(objDto.getTelefone1());
		
		if(objDto.getTelefone2() != null) {
			cliente.getTelefones().add(objDto.getTelefone2());
		}
		if(objDto.getTelefone3() != null) {
			cliente.getTelefones().add(objDto.getTelefone3());
		}
		return cliente;
	}
	
	
	
	
	// Metodo que faz upload da foto do cliente -- Repassa a chamada para o S3Service -- Salva a imagem no cliente 
	public URI uploadProfilePicture(MultipartFile multipartFile) {
		
		UserSS user = UserService.authenticated(); // Pega o usuario logado
		if(user == null) {
			throw new AuthorizationException("Acesso negado");
		}
		
		BufferedImage jpgImage = imageService.getJpgImageFromFile(multipartFile);
		
		jpgImage = imageService.cropSquare(jpgImage); // Metodo que recorta a imagem e deixa quadrada
		jpgImage = imageService.resize(jpgImage, size); // Método que define o tamanho da imagem
		
		// Montando o arquivo personalizado com base no cliente que esta logado
		String fileName = prefix + user.getId() + ".jpg";
		
		return s3Service.uploadFile(imageService.getInputStream(jpgImage, "jpg"), fileName, "image");
		
	}

}
