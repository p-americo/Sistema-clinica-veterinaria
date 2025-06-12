package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.ClienteRequestDTO;
import br.com.clinicavet.clinica_api.dto.ClienteResponseDTO;
import br.com.clinicavet.clinica_api.model.ECliente;
import br.com.clinicavet.clinica_api.repository.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional; // Importe a anotação do Spring

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository; // É uma boa prática declarar como final
    private final ModelMapper modelMapper; // É uma boa prática declarar como final

    @Autowired
    public ClienteService(ClienteRepository clienteRepository,  ModelMapper modelMapper) {
        this.clienteRepository = clienteRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public ClienteResponseDTO criarCliente(ClienteRequestDTO clienteRequestDTO) {
        // Mapeia o DTO de request para a entidade
        ECliente cliente = modelMapper.map(clienteRequestDTO, ECliente.class);
        // Salva a nova entidade no banco
        ECliente clienteSalvo = clienteRepository.save(cliente);
        // Mapeia a entidade salva para o DTO de resposta e retorna
        return modelMapper.map(clienteSalvo, ClienteResponseDTO.class);
    }


    @Transactional(readOnly = true) // Otimização para operações de apenas leitura
    public ClienteResponseDTO buscarClientePorId(Long id) {
        ECliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado com o ID: " + id));
        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }


    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodosClientes() {
        List<ECliente> clientes = clienteRepository.findAll();

        return clientes.stream()
                .map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class))
                .collect(Collectors.toList());
    }


    @Transactional
    public ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO clienteRequestDTO) {
        // Busca o cliente existente no banco. Se não encontrar, lança exceção.
        ECliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado para atualização com o ID: " + id));

        modelMapper.map(clienteRequestDTO, clienteExistente);

        clienteExistente.setId(id);

        ECliente clienteAtualizado = clienteRepository.save(clienteExistente);
        return modelMapper.map(clienteAtualizado, ClienteResponseDTO.class);
    }


    @Transactional
    public void deletarCliente(Long id) {

        if (!clienteRepository.existsById(id)) {
            throw new NoSuchElementException("Cliente não encontrado para deleção com o ID: " + id);
        }
        clienteRepository.deleteById(id);
    }
}