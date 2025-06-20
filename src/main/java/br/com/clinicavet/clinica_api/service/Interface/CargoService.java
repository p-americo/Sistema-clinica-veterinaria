package br.com.clinicavet.clinica_api.service.Interface;

import br.com.clinicavet.clinica_api.dto.CargoRequestDTO;
import br.com.clinicavet.clinica_api.dto.CargoResponseDTO;

import java.util.List;

public interface CargoService {

    CargoResponseDTO criarCargo (CargoRequestDTO cargo);

    CargoResponseDTO buscarPorId(Long id);

    List<CargoResponseDTO> lsitarTodos();

    CargoResponseDTO atualizarCargo (Long id, CargoRequestDTO cargo);

    CargoResponseDTO deletarCargo(Long id);


}
