package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.AnimalRequestDTO;
import br.com.clinicavet.clinica_api.dto.AnimalResponseDTO;
import br.com.clinicavet.clinica_api.dto.AnimalUpdateDTO;
import br.com.clinicavet.clinica_api.model.EAnimal;
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
    }

    @Transactional
    public AnimalResponseDTO criarAnimal(AnimalRequestDTO animalDTO) {

        //ECliente cliente = clienteRepository.findById(animalDTO.getClient_id()).orElseThrow(() -> new NoSuchElementException("Cliente (Dono) não foi encontrado"));

        EAnimal novoAnimal = modelMapper.map(animalDTO, EAnimal.class);

       //novoAnimal.setDono(cliente);

        EAnimal animalSalvo = animalRepository.save(novoAnimal);

        return modelMapper.map(animalSalvo, AnimalResponseDTO.class);
    }

    @Transactional
    public AnimalResponseDTO buscarAnimalPorId(long animal_id) {

        EAnimal animalBuscado = animalRepository.findById(animal_id).orElseThrow(() -> new NoSuchElementException("Animal não encontrado com o ID: " + animal_id));

        return  modelMapper.map(animalBuscado, AnimalResponseDTO.class);
    }

    @Transactional
    public List<AnimalResponseDTO> buscarTodosAnimais() {
        List<EAnimal> animais = animalRepository.findAll();

        return animais.stream().map(animal -> modelMapper.map(animal, AnimalResponseDTO.class)).collect(Collectors.toList());
    }

    @Transactional
    public AnimalResponseDTO atualizarAnimal(long animal_id, AnimalUpdateDTO animalDTO) {

        EAnimal animalExistente = animalRepository.findById(animal_id).orElseThrow(()-> new NoSuchElementException("Animal não encotrado com o ID: " + animal_id));

         modelMapper.map(animalDTO, animalExistente);

         EAnimal animalAtualizado = animalRepository.save(animalExistente);

        return modelMapper.map(animalAtualizado, AnimalResponseDTO.class);

    }

    @Transactional
    public AnimalResponseDTO deleteAnimal(long animal_id) {

        EAnimal animalDeletado = animalRepository.findById(animal_id).orElseThrow(()-> new NoSuchElementException("Animal não encontrado com o ID: " + animal_id));

        animalRepository.delete(animalDeletado);

        return modelMapper.map(animalDeletado, AnimalResponseDTO.class);
    }

    private AnimalResponseDTO mapEntidadeResponse(EAnimal animal) {

        AnimalResponseDTO dto = modelMapper.map(animal, AnimalResponseDTO.class);

        if (animal.getDataNascimento() != null) {
            Period periodo = Period.between(animal.getDataNascimento(), LocalDate.now());
            String idadeStr;
             idadeStr = String.format("%d anos e %d meses", periodo.getYears(), periodo.getMonths());
            dto.setIdadeFormatada(idadeStr);
        } else {
            dto.setIdadeFormatada("Não informada");
        }

        return dto;
    }

}
