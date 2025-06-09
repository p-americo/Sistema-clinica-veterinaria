package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.ClienteRequestDTO;
import br.com.clinicavet.clinica_api.dto.ClienteResponseDTO;
import br.com.clinicavet.clinica_api.model.ECliente;
import br.com.clinicavet.clinica_api.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ClienteService {

    ClienteRepository clienteRepository;
    ModelMapper modelMapper;

    @Autowired
    public ClienteService(ClienteRepository clienteRepository,  ModelMapper modelMapper) {
        this.clienteRepository = clienteRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public ClienteResponseDTO criarCliente(ClienteRequestDTO clienteRequestDTO) {

        ECliente cliente = modelMapper.map(clienteRequestDTO, ECliente.class);
        clienteRepository.save(cliente);
        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

    @Transactional
    public ClienteResponseDTO buscarClientePorId(Long cliente_id, ClienteRequestDTO clienteRequestDTO) {

        ECliente clienteBuscado = clienteRepository.findById(cliente_id).orElseThrow(() -> new NoSuchElementException("Cliente não encontrado com o ID " + cliente_id));

        return modelMapper.map(clienteBuscado, ClienteResponseDTO.class);
    }

    @Transactional
    public List<ClienteRequestDTO> buscaTodosClientes(ClienteRequestDTO clienteRequestDTO) {

        List<ECliente> clientes = clienteRepository.findAll();

        return clientes.stream().map(cliente -> modelMapper.map(cliente, ClienteRequestDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public ClienteResponseDTO atualizarCliente(Long cliente_id, ClienteRequestDTO clienteRequestDTO) {

        ECliente clienteExistente = clienteRepository.findById(cliente_id).orElseThrow(() -> new NoSuchElementException("Cliente não encontrado com o ID " + cliente_id));

        modelMapper.map(clienteRequestDTO, ClienteResponseDTO.class);

        ECliente clienteAtualizado = clienteRepository.save(clienteExistente);

        return modelMapper.map(clienteAtualizado, ClienteResponseDTO.class);
    }

    @Transactional
    public ClienteResponseDTO removerCliente(Long cliente_id) {

        ECliente clienteRemovido = clienteRepository.findById(cliente_id).orElseThrow(( -> new NoSuchElementException("Cliente não encontrado com o ID " + cliente_id);

        clienteRepository.delete(clienteRemovido);

        return modelMapper.map(clienteRemovido, ClienteResponseDTO.class);
    }


}
