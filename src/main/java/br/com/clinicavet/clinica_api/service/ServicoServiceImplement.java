package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.ServicoRequestDTO;
import br.com.clinicavet.clinica_api.dto.ServicoResponseDTO;
import br.com.clinicavet.clinica_api.model.TipoFuncionario;
import br.com.clinicavet.clinica_api.model.TipoServico;
import br.com.clinicavet.clinica_api.model.enums.TipoCargo;
import br.com.clinicavet.clinica_api.repository.FuncionarioRepository;
import br.com.clinicavet.clinica_api.repository.ServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class ServicoService {

    private final ServicoRepository servicoRepository; // Boa prática: usar 'final'
    private final FuncionarioRepository funcionarioRepository;
    private final ModelMapper modelMapper;


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
        TipoServico servico = servicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado com o ID: " + id));
        return mapEntidadeParaResponseDTO(servico);
    }

    @Transactional
    public ServicoResponseDTO cadastrarServico(ServicoRequestDTO dto) {

        TipoFuncionario veterinario = funcionarioRepository.findById(dto.getVeterinarioId())
                .orElseThrow(() -> new EntityNotFoundException("Veterinário não encontrado com o ID: " + dto.getVeterinarioId()));

        if (veterinario.getCargo() == null || veterinario.getCargo().getCargo() != TipoCargo.VETERINARIO) {
            throw new IllegalArgumentException("O funcionário com ID " + veterinario.getId() + " não é um veterinário.");
        }

        TipoServico novoServico = modelMapper.map(dto, TipoServico.class);
        novoServico.setVeterinario(veterinario); // Associa a entidade completa

        TipoServico servicoSalvo = servicoRepository.save(novoServico);
        return mapEntidadeParaResponseDTO(servicoSalvo);
    }

    @Transactional
    public ServicoResponseDTO atualizarServico(Long id, ServicoRequestDTO dto) {
        TipoServico servicoExistente = servicoRepository.findById(id)
                .orElseThrow(() -> new EntityNotFoundException("Serviço não encontrado para atualização com o ID: " + id));

        TipoFuncionario veterinario = funcionarioRepository.findById(dto.getVeterinarioId())
                .orElseThrow(() -> new EntityNotFoundException("Veterinário não encontrado com o ID: " + dto.getVeterinarioId()));

        // Valida se o funcionário informado é um veterinário
        if (veterinario.getCargo() == null || veterinario.getCargo().getCargo() != TipoCargo.VETERINARIO) {
            throw new IllegalArgumentException("O funcionário com ID " + veterinario.getId() + " não é um veterinário.");
        }

        modelMapper.map(dto, servicoExistente); // Mapeia os campos simples
        servicoExistente.setVeterinario(veterinario); // Reassocia o veterinário
        servicoExistente.setId(id); // Garante que o ID não seja alterado

        TipoServico servicoAtualizado = servicoRepository.save(servicoExistente);
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
    private ServicoResponseDTO mapEntidadeParaResponseDTO(TipoServico servico) {
        ServicoResponseDTO dto = modelMapper.map(servico, ServicoResponseDTO.class);
        if (servico.getVeterinario() != null) {
            // Popula o campo derivado 'nomeVeterinario'
            dto.setNomeVeterinario(servico.getVeterinario().getNome());
        }
        return dto;
    }
}