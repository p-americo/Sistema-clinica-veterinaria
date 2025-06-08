package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.ClienteRequestDTO;
import br.com.clinicavet.clinica_api.dto.ClienteResponseDTO;
import br.com.clinicavet.clinica_api.model.ECliente;
import br.com.clinicavet.clinica_api.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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


}
