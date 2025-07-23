package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.InternacaoRequestDTO;
import br.com.clinicavet.clinica_api.dto.InternacaoResponseDTO;
import br.com.clinicavet.clinica_api.model.TipoAnimal;
import br.com.clinicavet.clinica_api.model.TipoInternacao;
import br.com.clinicavet.clinica_api.repository.AnimalRepository;
import br.com.clinicavet.clinica_api.repository.InternacaoRepository;
import br.com.clinicavet.clinica_api.service.Interface.InternacaoServiceInterface;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class InternacaoServiceImplement implements InternacaoServiceInterface {

    private final InternacaoRepository repository;
    private final AnimalRepository animalRepository;
    private final ModelMapper mapper;

    public InternacaoServiceImplement(InternacaoRepository repository, AnimalRepository animalRepository, ModelMapper mapper) {
        this.repository = repository;
        this.animalRepository = animalRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional // É uma boa prática adicionar @Transactional em métodos que modificam o banco
    public InternacaoResponseDTO criarInternacao(InternacaoRequestDTO dto) {
        TipoAnimal animal = animalRepository.findById(dto.getAnimalId())
                .orElseThrow(() -> new NoSuchElementException("Animal não encontrado com o ID: " + dto.getAnimalId()));

        TipoInternacao internacao = mapper.map(dto, TipoInternacao.class);
        internacao.setId(null);
        internacao.setAnimal(animal);
        internacao.setStatus("INTERNADO"); // Definindo o status inicial
        TipoInternacao salva = repository.save(internacao);
        InternacaoResponseDTO response = mapper.map(salva, InternacaoResponseDTO.class);
        response.setAnimalId(animal.getId());

        return response;
    }

    @Override
    public InternacaoResponseDTO atualizarInternacao(Long id, InternacaoRequestDTO dto) {
        TipoInternacao existente = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Internação não encontrada"));

        mapper.map(dto, existente);

        TipoAnimal animal = animalRepository.findById(dto.getAnimalId())
                .orElseThrow(() -> new NoSuchElementException("Animal não encontrado"));

        existente.setAnimal(animal);

        TipoInternacao atualizada = repository.save(existente);
        InternacaoResponseDTO response = mapper.map(atualizada, InternacaoResponseDTO.class);
        response.setAnimalId(animal.getId());
        return response;
    }

    @Override
    public InternacaoResponseDTO buscarPorId(Long id) {
        TipoInternacao internacao = repository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Internação não encontrada"));

        InternacaoResponseDTO response = mapper.map(internacao, InternacaoResponseDTO.class);
        response.setAnimalId(internacao.getAnimal().getId());
        return response;
    }

    @Override
    public List<InternacaoResponseDTO> listarTodas() {
        return repository.findAll().stream()
                .map(internacao -> {
                    InternacaoResponseDTO dto = mapper.map(internacao, InternacaoResponseDTO.class);
                    dto.setAnimalId(internacao.getAnimal().getId());
                    return dto;
                })
                .collect(Collectors.toList());
    }

    @Override
    public void deletarInternacao(Long id) {
        if (!repository.existsById(id)) {
            throw new NoSuchElementException("Internação não encontrada");
        }
        repository.deleteById(id);
    }
}

