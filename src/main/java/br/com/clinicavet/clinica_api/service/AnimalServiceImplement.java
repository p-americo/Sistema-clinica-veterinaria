package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.AnimalRequestDTO;
import br.com.clinicavet.clinica_api.dto.AnimalResponseDTO;
import br.com.clinicavet.clinica_api.dto.AnimalUpdateDTO;
import br.com.clinicavet.clinica_api.model.TipoAnimal;
import br.com.clinicavet.clinica_api.model.TipoCliente;
import br.com.clinicavet.clinica_api.repository.AnimalRepository;
import br.com.clinicavet.clinica_api.repository.ClienteRepository;
import br.com.clinicavet.clinica_api.service.Interface.AnimalService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AnimalServiceImplement implements AnimalService {

    private final AnimalRepository animalRepository;
    private final ModelMapper modelMapper;
    private final ClienteRepository clienteRepository;


    public AnimalServiceImplement(AnimalRepository animalRepository, ModelMapper modelMapper, ClienteRepository clienteRepository) {
        this.animalRepository = animalRepository;
        this.modelMapper = modelMapper;
        this.clienteRepository = clienteRepository;
        this.modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @Transactional
    public AnimalResponseDTO criarAnimal(AnimalRequestDTO animalDTO) {
        TipoCliente cliente = clienteRepository.findById(animalDTO.getClienteId())
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado com o ID: " + animalDTO.getClienteId()));

        TipoAnimal novoAnimal = modelMapper.map(animalDTO, TipoAnimal.class);

        novoAnimal.setId(null);

        novoAnimal.setCliente(cliente);

        TipoAnimal animalSalvo = animalRepository.save(novoAnimal);

        return mapEntidadeParaResponse(animalSalvo);
    }

    @Transactional
    public AnimalResponseDTO buscarAnimalPorId(long animalId) {
        TipoAnimal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new NoSuchElementException("Animal não encontrado com o ID: " + animalId));
        return mapEntidadeParaResponse(animal);
    }

    @Override
    public List<AnimalResponseDTO> listarTodos() {
        return List.of();
    }

    @Transactional
    public List<AnimalResponseDTO> buscarTodosAnimais() {
        return animalRepository.findAll().stream()
                .map(this::mapEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional
    public AnimalResponseDTO atualizarAnimal(Long animalId, AnimalUpdateDTO animalDTO) {
        TipoAnimal animalExistente = animalRepository.findById(animalId)
                .orElseThrow(() -> new NoSuchElementException("Animal não encontrado com o ID: " + animalId));

        modelMapper.map(animalDTO, animalExistente);

        if (animalDTO.getClienteId() != null) {

            if (animalExistente.getCliente() == null || !animalDTO.getClienteId().equals(animalExistente.getCliente().getId())) {
                TipoCliente novoCliente = clienteRepository.findById(animalDTO.getClienteId())
                        .orElseThrow(() -> new NoSuchElementException("Novo cliente (dono) não encontrado com o ID: " + animalDTO.getClienteId()));

                animalExistente.setCliente(novoCliente);
            }
        }

        TipoAnimal animalAtualizado = animalRepository.save(animalExistente);
        return mapEntidadeParaResponse(animalAtualizado);
    }

    @Override
    public void deletarAnimal(Long id) {

        animalRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Animal não encontrado com o ID: " + id));
        animalRepository.deleteById(id);
    }

    @Override
    public AnimalResponseDTO buscarPorId(Long id) {
        return null;
    }


    @Transactional
    public void deletarAnimal(long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new NoSuchElementException("Animal não encontrado com o ID: " + animalId);
        }
        animalRepository.deleteById(animalId);
    }

    private AnimalResponseDTO mapEntidadeParaResponse(TipoAnimal animal) {
        AnimalResponseDTO dto = modelMapper.map(animal, AnimalResponseDTO.class);

        if (animal.getDataNascimento() != null) {
            Period periodo = Period.between(animal.getDataNascimento(), LocalDate.now());
            String idadeFormatada = String.format("%d anos e %d meses", periodo.getYears(), periodo.getMonths());
            dto.setIdadeFormatada(idadeFormatada);
        } else {
            dto.setIdadeFormatada("Idade não informada");
        }
        return dto;
    }
}