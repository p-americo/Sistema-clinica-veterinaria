package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.ClienteRequestDTO;
import br.com.clinicavet.clinica_api.dto.ClienteResponseDTO;
import br.com.clinicavet.clinica_api.model.TipoCliente;
import br.com.clinicavet.clinica_api.repository.ClienteRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ClienteServiceImplement {

    private final ClienteRepository clienteRepository;
    private final ModelMapper modelMapper;


    public ClienteServiceImplement(ClienteRepository clienteRepository, ModelMapper modelMapper) {
        this.clienteRepository = clienteRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public ClienteResponseDTO criarCliente(ClienteRequestDTO clienteRequestDTO) {
        TipoCliente cliente = modelMapper.map(clienteRequestDTO, TipoCliente.class);
        TipoCliente clienteSalvo = clienteRepository.save(cliente);
        return modelMapper.map(clienteSalvo, ClienteResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public ClienteResponseDTO buscarClientePorId(Long id) {
        TipoCliente cliente = clienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado com o ID: " + id));
        return modelMapper.map(cliente, ClienteResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public List<ClienteResponseDTO> listarTodosClientes() {
        List<TipoCliente> clientes = clienteRepository.findAll();
        return clientes.stream()
                .map(cliente -> modelMapper.map(cliente, ClienteResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public ClienteResponseDTO atualizarCliente(Long id, ClienteRequestDTO clienteRequestDTO) {
        TipoCliente clienteExistente = clienteRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado para atualização com o ID: " + id));

        modelMapper.map(clienteRequestDTO, clienteExistente);

        TipoCliente clienteAtualizado = clienteRepository.save(clienteExistente);
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
        TipoCliente clienteRemovido = clienteRepository.findById(cliente_id).orElseThrow(() -> new NoSuchElementException("Cliente não encontrado com o ID " + cliente_id));

        clienteRepository.delete(clienteRemovido);

        return modelMapper.map(clienteRemovido, ClienteResponseDTO.class);
    }
}