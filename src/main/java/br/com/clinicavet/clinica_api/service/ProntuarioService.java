package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.Execeptions.DataIntegrityViolationException;
import br.com.clinicavet.clinica_api.dto.ProntuarioRequestDTO;
import br.com.clinicavet.clinica_api.dto.ProntuarioResponseDTO;
import br.com.clinicavet.clinica_api.dto.ProntuarioUpdateDTO;
import br.com.clinicavet.clinica_api.dto.RegistroProntuarioResponseDTO;
import br.com.clinicavet.clinica_api.model.TipoAnimal;
import br.com.clinicavet.clinica_api.model.TipoProntuario;
import br.com.clinicavet.clinica_api.repository.AnimalRepository;
import br.com.clinicavet.clinica_api.repository.ProntuarioRepository;
import br.com.clinicavet.clinica_api.service.Interface.MedicamentoService;
import br.com.clinicavet.clinica_api.service.Interface.ProntuarioServiceInterface;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class ProntuarioService implements ProntuarioServiceInterface {

    private final MedicamentoService medicamentoService;
    private final ProntuarioRepository prontuarioRepository;
    private final AnimalRepository animalRepository;
    private final ModelMapper modelMapper;
    
    public ProntuarioService(MedicamentoService medicamentoService, 
                           ProntuarioRepository prontuarioRepository,
                           AnimalRepository animalRepository,
                           ModelMapper modelMapper) {
        this.medicamentoService = medicamentoService;
        this.prontuarioRepository = prontuarioRepository;
        this.animalRepository = animalRepository;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @Override
    @Transactional
    public ProntuarioResponseDTO criarProntuario(ProntuarioRequestDTO prontuarioRequestDTO) {
        // Verificar se o animal existe
        TipoAnimal animal = animalRepository.findById(prontuarioRequestDTO.getAnimalId())
                .orElseThrow(() -> new NoSuchElementException("Animal não encontrado com o ID: " + prontuarioRequestDTO.getAnimalId()));
        
        // Verificar se já existe prontuário para este animal
        if (prontuarioRepository.existsByAnimalId(prontuarioRequestDTO.getAnimalId())) {
            throw new DataIntegrityViolationException("Já existe um prontuário para este animal");
        }
        
        TipoProntuario novoProntuario = modelMapper.map(prontuarioRequestDTO, TipoProntuario.class);
        novoProntuario.setId(null);
        novoProntuario.setAnimal(animal);
        
        TipoProntuario prontuarioSalvo = prontuarioRepository.save(novoProntuario);
        
        return mapEntidadeParaResponse(prontuarioSalvo);
    }

    @Override
    @Transactional
    public ProntuarioResponseDTO atualizarProntuario(Long id, ProntuarioUpdateDTO prontuarioUpdateDTO) {
        TipoProntuario prontuarioExistente = prontuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Prontuário não encontrado com o ID: " + id));
        
        modelMapper.map(prontuarioUpdateDTO, prontuarioExistente);
        
        TipoProntuario prontuarioAtualizado = prontuarioRepository.save(prontuarioExistente);
        return mapEntidadeParaResponse(prontuarioAtualizado);
    }

    @Override
    @Transactional
    public void deletarProntuario(Long id) {
        if (!prontuarioRepository.existsById(id)) {
            throw new NoSuchElementException("Prontuário não encontrado com o ID: " + id);
        }
        prontuarioRepository.deleteById(id);
    }

    @Override
    public ProntuarioResponseDTO buscarPorId(Long id) {
        TipoProntuario prontuario = prontuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Prontuário não encontrado com o ID: " + id));
        return mapEntidadeParaResponse(prontuario);
    }

    @Override
    public ProntuarioResponseDTO buscarPorAnimalId(Long animalId) {
        TipoProntuario prontuario = prontuarioRepository.findByAnimalId(animalId)
                .orElseThrow(() -> new NoSuchElementException("Prontuário não encontrado para o animal com ID: " + animalId));
        return mapEntidadeParaResponse(prontuario);
    }

    @Override
    public List<ProntuarioResponseDTO> listarTodos() {
        return prontuarioRepository.findAll().stream()
                .map(this::mapEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public ProntuarioResponseDTO buscarPorIdComRegistros(Long id) {
        TipoProntuario prontuario = prontuarioRepository.findByIdWithRegistros(id)
                .orElseThrow(() -> new NoSuchElementException("Prontuário não encontrado com o ID: " + id));
        
        ProntuarioResponseDTO response = mapEntidadeParaResponse(prontuario);
        
        if (prontuario.getRegistros() != null) {
            List<RegistroProntuarioResponseDTO> registros = prontuario.getRegistros().stream()
                    .map(registro -> {
                        RegistroProntuarioResponseDTO dto = modelMapper.map(registro, RegistroProntuarioResponseDTO.class);
                        if (registro.getVeterinarioResponsavel() != null) {
                            dto.setNomeVeterinario(registro.getVeterinarioResponsavel().getNome());
                        }
                        return dto;
                    })
                    .collect(Collectors.toList());
            response.setRegistros(registros);
        }
        
        return response;
    }
    
    private ProntuarioResponseDTO mapEntidadeParaResponse(TipoProntuario prontuario) {
        ProntuarioResponseDTO dto = modelMapper.map(prontuario, ProntuarioResponseDTO.class);
        
        if (prontuario.getAnimal() != null) {
            dto.setAnimalId(prontuario.getAnimal().getId());
            dto.setNomeAnimal(prontuario.getAnimal().getNome());
        }
        
        return dto;
    }
}
