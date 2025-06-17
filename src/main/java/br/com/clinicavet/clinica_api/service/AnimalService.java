package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.AnimalRequestDTO;
import br.com.clinicavet.clinica_api.dto.AnimalResponseDTO;
import br.com.clinicavet.clinica_api.dto.AnimalUpdateDTO;
import br.com.clinicavet.clinica_api.model.EAnimal;
import br.com.clinicavet.clinica_api.model.ECliente;
import br.com.clinicavet.clinica_api.repository.AnimalRepository;
import br.com.clinicavet.clinica_api.repository.ClienteRepository;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.Period;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AnimalService {

    private final AnimalRepository animalRepository;
    private final ModelMapper modelMapper;
    private final ClienteRepository clienteRepository;

    @Autowired
    public AnimalService(AnimalRepository animalRepository, ModelMapper modelMapper, ClienteRepository clienteRepository) {
        this.animalRepository = animalRepository;
        this.modelMapper = modelMapper;
        this.clienteRepository = clienteRepository;
        this.modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @Transactional
    public AnimalResponseDTO criarAnimal(AnimalRequestDTO animalDTO) {
        ECliente cliente = clienteRepository.findById(animalDTO.getClienteId())
                .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado com o ID: " + animalDTO.getClienteId()));

        EAnimal novoAnimal = modelMapper.map(animalDTO, EAnimal.class);

        // FORÇA O ID A SER NULO ANTES DE SALVAR
        // Esta é a garantia de que a operação será um INSERT.
        novoAnimal.setId(null);

        novoAnimal.setCliente(cliente);

        EAnimal animalSalvo = animalRepository.save(novoAnimal);

        return mapEntidadeParaResponse(animalSalvo);
    }

    @Transactional
    public AnimalResponseDTO buscarAnimalPorId(long animalId) {
        EAnimal animal = animalRepository.findById(animalId)
                .orElseThrow(() -> new NoSuchElementException("Animal não encontrado com o ID: " + animalId));
        return mapEntidadeParaResponse(animal);
    }

    @Transactional
    public List<AnimalResponseDTO> buscarTodosAnimais() {
        return animalRepository.findAll().stream()
                .map(this::mapEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    @Transactional
<<<<<<< HEAD
    public AnimalResponseDTO atualizarAnimal(long animalId, AnimalUpdateDTO animalDTO) {
        EAnimal animalExistente = animalRepository.findById(animalId)
                .orElseThrow(() -> new NoSuchElementException("Animal não encontrado com o ID: " + animalId));

        if (animalDTO.getClienteId() != 0) {
            ECliente novoCliente = clienteRepository.findById(animalDTO.getClienteId())
                    .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado com o ID: " + animalDTO.getClienteId()));
            animalExistente.setCliente(novoCliente);
        }

        modelMapper.map(animalDTO, animalExistente);
        EAnimal animalAtualizado = animalRepository.save(animalExistente);

=======
    public AnimalResponseDTO atualizarAnimal(Long animalId, AnimalUpdateDTO animalDTO) { // animalId vem da URL
        EAnimal animalExistente = animalRepository.findById(animalId)
                .orElseThrow(() -> new NoSuchElementException("Animal não encontrado com o ID: " + animalId));

        // Usa ModelMapper para aplicar as atualizações do DTO na entidade existente
        // Como o ModelMapper está configurado para pular nulos, ele só atualizará os campos que vieram no DTO
        modelMapper.map(animalDTO, animalExistente);

            ECliente novoCliente = clienteRepository.findById(animalDTO.getClienteId())
                    .orElseThrow(() -> new NoSuchElementException("Cliente não encontrado com o ID: " + animalDTO.getClienteId()));
            animalExistente.setCliente(novoCliente);


        EAnimal animalAtualizado = animalRepository.save(animalExistente);
>>>>>>> 706af775a0d67d65b0905a6902f09b7ba8138bd6
        return mapEntidadeParaResponse(animalAtualizado);
    }

    @Transactional
    public void deletarAnimal(long animalId) {
        if (!animalRepository.existsById(animalId)) {
            throw new NoSuchElementException("Animal não encontrado com o ID: " + animalId);
        }
        animalRepository.deleteById(animalId);
    }

    private AnimalResponseDTO mapEntidadeParaResponse(EAnimal animal) {
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