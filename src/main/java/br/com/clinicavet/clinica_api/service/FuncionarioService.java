package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.FuncionarioRequestDTO;
import br.com.clinicavet.clinica_api.dto.FuncionarioResponseDTO;
import br.com.clinicavet.clinica_api.dto.FuncionarioRequestDTO;
import br.com.clinicavet.clinica_api.model.ECargo;
import br.com.clinicavet.clinica_api.model.EFuncionario;
import br.com.clinicavet.clinica_api.model.enums.TipoCargo;
import br.com.clinicavet.clinica_api.repository.CargoRepository;
import br.com.clinicavet.clinica_api.repository.FuncionarioRepository;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class FuncionarioService {

    private final FuncionarioRepository funcionarioRepository;
    private final CargoRepository cargoRepository;
    private final ModelMapper modelMapper;

    public FuncionarioService(FuncionarioRepository funcionarioRepository, CargoRepository cargoRepository, ModelMapper modelMapper) {
        this.funcionarioRepository = funcionarioRepository;
        this.cargoRepository = cargoRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public FuncionarioResponseDTO criarFuncionario(FuncionarioRequestDTO requestDTO) {
        // Busca a entidade do cargo
        ECargo cargo = cargoRepository.findById(requestDTO.getCargoId())
                .orElseThrow(() -> new NoSuchElementException("Cargo não encontrado com o ID: " + requestDTO.getCargoId()));

        // Validação de negócio: se o cargo é VETERINARIO, o CRMV é obrigatório.
        if (cargo.getCargo() == TipoCargo.VETERINARIO) {
            if (requestDTO.getCrmv() == null || requestDTO.getCrmv().isBlank()) {
                throw new IllegalArgumentException("CRMV é obrigatório para o cargo de Veterinário.");
            }
        } else {
            // Garante que o CRMV seja nulo para outros cargos
            if (requestDTO.getCrmv() != null && !requestDTO.getCrmv().isBlank()) {
                throw new IllegalArgumentException("CRMV só pode ser preenchido para o cargo de Veterinário.");
            }
        }

        EFuncionario novoFuncionario = modelMapper.map(requestDTO, EFuncionario.class);
        novoFuncionario.setCargo(cargo);

        EFuncionario funcionarioSalvo = funcionarioRepository.save(novoFuncionario);
        return modelMapper.map(funcionarioSalvo, FuncionarioResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public FuncionarioResponseDTO buscarPorId(Long id) {
        EFuncionario funcionario = funcionarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Funcionário não encontrado com o ID: " + id));
        return modelMapper.map(funcionario, FuncionarioResponseDTO.class);
    }

    @Transactional(readOnly = true)
    public List<FuncionarioResponseDTO> listarTodos() {
        return funcionarioRepository.findAll()
                .stream()
                .map(func -> modelMapper.map(func, FuncionarioResponseDTO.class))
                .collect(Collectors.toList());
    }

    // Implementação dos métodos de atualizar e deletar seguiria a mesma lógica dos outros serviços
}