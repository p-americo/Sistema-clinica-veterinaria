package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.CargoRequestDTO;
import br.com.clinicavet.clinica_api.dto.ClienteRequestDTO;
import br.com.clinicavet.clinica_api.dto.ClienteResponseDTO;
import br.com.clinicavet.clinica_api.dto.ClienteUpdateDTO;

import java.util.List;

public interface ClienteService {

    ClienteResponseDTO criarCliente (CargoRequestDTO cargo);

    ClienteResponseDTO atualizarCliente (Long id, ClienteUpdateDTO cargo);

    ClienteResponseDTO deletarCliente(Long id);

    ClienteResponseDTO buscarPorId(Long id);

    List<ClienteResponseDTO> listarClientes();
}
