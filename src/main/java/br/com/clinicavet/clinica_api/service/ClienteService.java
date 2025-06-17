package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.ClienteRequestDTO;
import br.com.clinicavet.clinica_api.dto.ClienteResponseDTO;
import br.com.clinicavet.clinica_api.model.ECliente;
import br.com.clinicavet.clinica_api.repository.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    private final ClienteRepository clienteRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository,  ModelMapper modelMapper) {
        this.clienteRepository = clienteRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public ClienteResponseDTO criarCliente(ClienteRequestDTO clienteRequestDTO) {
        ECliente cliente = modelMapper.map(clienteRequestDTO, ECliente.class);
        ECliente clienteSalvo = clienteRepository.save(cliente);
        return modelMapper.map(clienteSalvo, ClienteResponseDTO.class);
    }

    @Transactional(readOnly = true)
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
        ECliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado para atualização com o ID: " + id));

        modelMapper.map(clienteRequestDTO, clienteExistente);

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

    @Transactional
    public ClienteResponseDTO removerCliente(Long cliente_id) {
        // Correção aqui
        ECliente clienteRemovido = clienteRepository.findById(cliente_id).orElseThrow(() -> new NoSuchElementException("Cliente não encontrado com o ID " + cliente_id));

        clienteRepository.delete(clienteRemovido);

        return modelMapper.map(clienteRemovido, ClienteResponseDTO.class);
    }
}