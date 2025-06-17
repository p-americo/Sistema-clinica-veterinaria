package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.ServicoRequestDTO;
import br.com.clinicavet.clinica_api.dto.ServicoResponseDTO;
import br.com.clinicavet.clinica_api.model.EFuncionario;
import br.com.clinicavet.clinica_api.model.EServico;
import br.com.clinicavet.clinica_api.model.enums.TipoCargo;
import br.com.clinicavet.clinica_api.repository.FuncionarioRepository;
import br.com.clinicavet.clinica_api.repository.ServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.apache.coyote.Request;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository; // Boa prática: usar 'final'
    private final FuncionarioRepository funcionarioRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public ServicoService(ServicoRepository servicoRepository, FuncionarioRepository funcionarioRepository, ModelMapper modelMapper) {
        this.servicoRepository = servicoRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional(readOnly = true)
    public Page<ServicoResponseDTO> obterTodosServicos(Pageable paginacao) {
        return servicoRepository.findAll(paginacao)
                .map(this::mapEntidadeParaResponseDTO); // Reusa o método helper
    }

    @Transactional(readOnly = true)
    public ServicoResponseDTO obterServicoPorId(Long id) {
        EServico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com o ID: " + id));
        return mapEntidadeParaResponseDTO(servico);
    }

    @Transactional
    public ServicoResponseDTO cadastrarServico(ServicoRequestDTO dto) {
        // Busca a entidade do funcionário (que deve ser um veterinário)
        EFuncionario veterinario = funcionarioRepository.findById(dto.getVeterinarioId())
                .orElseThrow(() -> new EntityNotFoundException("Veterinário não encontrado com o ID: " + dto.getVeterinarioId()));

        // VALIDAÇÃO DE NEGÓCIO: Garante que o funcionário associado tem o cargo de VETERINARIO
        if (veterinario.getCargo() == null || veterinario.getCargo().getCargo() != TipoCargo.VETERINARIO) {
            throw new IllegalArgumentException("O funcionário com ID " + veterinario.getId() + " não é um veterinário.");
        }

        EServico novoServico = modelMapper.map(dto, EServico.class);
        novoServico.setId(null); //ADD AQUI SE DER ERRO DE CORRESPONDENCIA

        EServico servicoSalvo = servicoRepository.save(novoServico);
        return mapEntidadeParaResponseDTO(servicoSalvo);
    }

    @Transactional
    public ServicoResponseDTO atualizarServico(Long id, ServicoRequestDTO dto) {
        EServico servicoExistente = servicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado para atualização com o ID: " + id));

        EFuncionario veterinario = funcionarioRepository.findById(dto.getVeterinarioId())
                .orElseThrow(() -> new EntityNotFoundException("Veterinário não encontrado com o ID: " + dto.getVeterinarioId()));

        // Valida se o funcionário informado é um veterinário
        if (veterinario.getCargo() == null || veterinario.getCargo().getCargo() != TipoCargo.VETERINARIO) {
            throw new IllegalArgumentException("O funcionário com ID " + veterinario.getId() + " não é um veterinário.");
        }

        modelMapper.map(dto, servicoExistente); // Mapeia os campos simples
        servicoExistente.setVeterinario(veterinario); // Reassocia o veterinário
        servicoExistente.setId(id); // Garante que o ID não seja alterado

        EServico servicoAtualizado = servicoRepository.save(servicoExistente);
        return mapEntidadeParaResponseDTO(servicoAtualizado);
    }

    @Transactional
    public void deletarServico(Long id) {
        if (!servicoRepository.existsById(id)) {
            throw new EntityNotFoundException("Serviço não encontrado para deleção com o ID: " + id);
        }
        servicoRepository.deleteById(id);
    }

    // Método helper privado para centralizar o mapeamento para o ResponseDTO
    private ServicoResponseDTO mapEntidadeParaResponseDTO(EServico servico) {
        ServicoResponseDTO dto = modelMapper.map(servico, ServicoResponseDTO.class);
        return dto;
    }
}