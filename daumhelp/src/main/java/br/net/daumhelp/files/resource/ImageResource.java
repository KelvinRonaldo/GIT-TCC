package br.net.daumhelp.files.resource;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

import br.net.daumhelp.dto.ClienteDTO;
import br.net.daumhelp.dto.ProfissionalDTO;
import br.net.daumhelp.dto.repository.ClienteDTORepository;
import br.net.daumhelp.dto.repository.ProfissionalDTORepository;
import br.net.daumhelp.files.Disco;
import br.net.daumhelp.model.Pedido;
import br.net.daumhelp.repository.PedidoRepository;

//REQUISIÇOES QUE REALIZAM O UPLOAD DE IMAGENS DA PLATAFORMA

@CrossOrigin
//@CrossOrigin(origins = "http://localhost:3000")
//@CrossOrigin(origins = "http://ec2-3-220-68-195.compute-1.amazonaws.com")
@RestController
@RequestMapping("/imagens")
public class ImageResource {

	@Autowired
	private Disco disco;
	
	@Autowired
	private ClienteDTORepository clienteDTOrepository;
	
	@Autowired
	private ProfissionalDTORepository proDTOrepository;
	
	@Autowired
	private PedidoRepository pedidoRepository;

	//FAZ UPLOAD DE IMAGENS DE CLIENTES
	@PostMapping("/cliente")
	public void uploadImgCliente(@RequestParam MultipartFile img, @RequestParam Long idCliente) {

		//BUSCA CLIENTE PARA DA FOTO **1
		ClienteDTO cliente = clienteDTOrepository.findById(idCliente).get();
	
		//VERIFICA SE ELE JA POSSUI UMA FOTO PARA APAGA-LA E SALVAR A NOVA **2
		if(cliente.getFoto() != null) {			
			if(cliente.getFoto() != "" && !cliente.getFoto().isEmpty()) {
				disco.apagar(cliente.getFoto());//APAGA A IMAGEM ANTIGA	 **3
			}
		}
		//SALVA A NOVA IMAGEM DE RETORNA SEU CAMINHO **4
		String imgClienteCaminho = disco.salvarFotoCliente(img, idCliente);
		
		//COLOCA O CAMINHO DA IMGAME NO CLIENTE E O AUTALIZA NO BANCO **5
		cliente.setFoto(imgClienteCaminho);
		clienteDTOrepository.save(cliente);	
	}
	
	//FAZ UPLOADS DE IUMAGENS DE PROFISSIONAIS
	@PostMapping("/profissional")
	public void uploadImgPro(@RequestParam MultipartFile img, @RequestParam Long idPro) {
		
		//**1
		ProfissionalDTO pro = proDTOrepository.findById(idPro).get();
		
		//**2
		if(pro.getFoto() != null) {
			if(pro.getFoto() != "" && !pro.getFoto().isEmpty()) {
				disco.apagar(pro.getFoto()); //**3			
			}			
		}
		
		//**4
		String imgProCaminho = disco.salvarFotoPro(img, idPro);
		
		//**5
		pro.setFoto(imgProCaminho);
		proDTOrepository.save(pro);	
	} 
	
	//FAZ UPLOAD DE ATÉ 3 IMAGENS DE UMA VEZ DE PEDIDOS
	@PostMapping("/pedido")
	public void uploadImgsPedido(@RequestParam List<MultipartFile> imgs, @RequestParam Long idPedido) {

		//**1
		Pedido pedido = pedidoRepository.findById(idPedido).get();
		
		//ARRAY QUE GUARDA CAMINHO DAS IMAGENS DO PEDIDO
		ArrayList<String> imgsPedidoCaminho = new ArrayList<String>();

		int i = -1;//CONTROLA QUAL POSIÇÃO DO ARRAY O CAMINHO DA CADA IMAGEM FICARÁ

		for(MultipartFile img : imgs) {
			//VERIFICA SE A IMAGEM EXISTE
			if(!img.getOriginalFilename().isEmpty() && img.getSize() > 0) {
				//SALVA IMAGEM E RETORNA SEU NOVO CAMINHO
				imgsPedidoCaminho.add(disco.salvarFotoPedido(img, idPedido));
				++i;
				//COLOCA CADA IMAGEM NO PEDIDO
				if(i == 0) {
					pedido.setFoto1(imgsPedidoCaminho.get(i));				
				}else if(i == 1) {
					pedido.setFoto2(imgsPedidoCaminho.get(i));				
				}else if(i == 2){
					pedido.setFoto3(imgsPedidoCaminho.get(i));
				}
			}
		}
		
		//ATUALIZA O PEDIDO NO BANCO
		pedidoRepository.save(pedido);
		
	}
	
	
	
	
}
