package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.InternacaoRequestDTO;
import br.com.clinicavet.clinica_api.dto.InternacaoResponseDTO;
import br.com.clinicavet.clinica_api.dto.InternacaoUpdateDTO;
import br.com.clinicavet.clinica_api.model.TipoAnimal;
import br.com.clinicavet.clinica_api.model.TipoFuncionario;
import br.com.clinicavet.clinica_api.model.TipoInternacao;
import br.com.clinicavet.clinica_api.repository.AnimalRepository;
import br.com.clinicavet.clinica_api.repository.FuncionarioRepository;
import br.com.clinicavet.clinica_api.repository.InternacaoRepository;
import br.com.clinicavet.clinica_api.service.Interface.InternacaoServiceInterface;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class InternacaoServiceImplement implements InternacaoServiceInterface {
    
    private final InternacaoRepository internacaoRepository;
    private final AnimalRepository animalRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ModelMapper modelMapper;
    
    public InternacaoServiceImplement(InternacaoRepository internacaoRepository,
                                     AnimalRepository animalRepository,
                                     FuncionarioRepository funcionarioRepository,
                                     ModelMapper modelMapper) {
        this.internacaoRepository = internacaoRepository;
        this.animalRepository = animalRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @Override
    @Transactional
    public InternacaoResponseDTO criarInternacao(InternacaoRequestDTO internacaoRequestDTO) {
        // Verificar se o animal existe
        TipoAnimal animal = animalRepository.findById(internacaoRequestDTO.getAnimalId())
                .orElseThrow(() -> new NoSuchElementException("Animal não encontrado com o ID: " + internacaoRequestDTO.getAnimalId()));
        
        // Verificar se o veterinário existe
        TipoFuncionario veterinario = funcionarioRepository.findById(internacaoRequestDTO.getVeterinarioResponsavelId())
                .orElseThrow(() -> new NoSuchElementException("Veterinário não encontrado com o ID: " + internacaoRequestDTO.getVeterinarioResponsavelId()));
        
        TipoInternacao novaInternacao = modelMapper.map(internacaoRequestDTO, TipoInternacao.class);
        novaInternacao.setId(null);
        novaInternacao.setAnimal(animal);
        novaInternacao.setVeterinarioResponsavel(veterinario);
        
        // Se não foi fornecida data de início, usar a data atual
        if (novaInternacao.getDataInicio() == null) {
            novaInternacao.setDataInicio(LocalDateTime.now());
        }
        
        // Se não foi especificado, a internação inicia como ativa
        if (novaInternacao.getAtiva() == null) {
            novaInternacao.setAtiva(true);
        }
        
        TipoInternacao internacaoSalva = internacaoRepository.save(novaInternacao);
        
        return mapEntidadeParaResponse(internacaoSalva);
    }

    @Override
    @Transactional
    public InternacaoResponseDTO atualizarInternacao(Long id, InternacaoUpdateDTO internacaoUpdateDTO) {
        TipoInternacao internacaoExistente = internacaoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Internação não encontrada com o ID: " + id));
        
        modelMapper.map(internacaoUpdateDTO, internacaoExistente);
        
        // Atualizar veterinário responsável se fornecido
        if (internacaoUpdateDTO.getVeterinarioResponsavelId() != null) {
            TipoFuncionario veterinario = funcionarioRepository.findById(internacaoUpdateDTO.getVeterinarioResponsavelId())
                    .orElseThrow(() -> new NoSuchElementException("Veterinário não encontrado com o ID: " + internacaoUpdateDTO.getVeterinarioResponsavelId()));
            internacaoExistente.setVeterinarioResponsavel(veterinario);
        }
        
        TipoInternacao internacaoAtualizada = internacaoRepository.save(internacaoExistente);
        return mapEntidadeParaResponse(internacaoAtualizada);
    }

    @Override
    @Transactional
    public void deletarInternacao(Long id) {
        if (!internacaoRepository.existsById(id)) {
            throw new NoSuchElementException("Internação não encontrada com o ID: " + id);
        }
        internacaoRepository.deleteById(id);
    }

    @Override
    public InternacaoResponseDTO buscarPorId(Long id) {
        TipoInternacao internacao = internacaoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Internação não encontrada com o ID: " + id));
        return mapEntidadeParaResponse(internacao);
    }

    @Override
    public List<InternacaoResponseDTO> buscarInternacoesAtivas() {
        return internacaoRepository.findInternacoesAtivas().stream()
                .map(this::mapEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InternacaoResponseDTO> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return internacaoRepository.findByDataInicioBetween(inicio, fim).stream()
                .map(this::mapEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<InternacaoResponseDTO> listarTodos() {
        return internacaoRepository.findAll().stream()
                .map(this::mapEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    @Override
    @Transactional
    public InternacaoResponseDTO finalizarInternacao(Long id) {
        TipoInternacao internacao = internacaoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Internação não encontrada com o ID: " + id));
        
        internacao.setDataFim(LocalDateTime.now());
        internacao.setAtiva(false);
        
        TipoInternacao internacaoFinalizada = internacaoRepository.save(internacao);
        return mapEntidadeParaResponse(internacaoFinalizada);
    }
    
    private InternacaoResponseDTO mapEntidadeParaResponse(TipoInternacao internacao) {
        InternacaoResponseDTO dto = modelMapper.map(internacao, InternacaoResponseDTO.class);
        
        if (internacao.getAnimal() != null) {
            dto.setAnimalId(internacao.getAnimal().getId());
            dto.setNomeAnimal(internacao.getAnimal().getNome());
        }
        
        if (internacao.getVeterinarioResponsavel() != null) {
            dto.setVeterinarioResponsavelId(internacao.getVeterinarioResponsavel().getId());
            dto.setNomeVeterinario(internacao.getVeterinarioResponsavel().getNome());
        }
        
        return dto;
    }
}