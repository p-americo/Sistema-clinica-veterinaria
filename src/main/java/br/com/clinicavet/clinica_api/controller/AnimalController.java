package br.com.clinicavet.clinica_api.controller;

import br.com.clinicavet.clinica_api.dto.AnimalRequestDTO;
import br.com.clinicavet.clinica_api.dto.AnimalResponseDTO;
import br.com.clinicavet.clinica_api.dto.AnimalUpdateDTO;
import br.com.clinicavet.clinica_api.service.AnimalService;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/animais")
public class AnimalController {

    private AnimalService animalService;

    @Autowired
    public AnimalController(AnimalService animalService) {
        this.animalService = animalService;
    }

    @PostMapping
    public ResponseEntity<AnimalResponseDTO> criarAnimal(@RequestBody @Valid AnimalRequestDTO animalRequestDTO, UriComponentsBuilder uriComponentsBuilder) {
        AnimalResponseDTO responseDTO = animalService.criarAnimal(animalRequestDTO);

        URI uri = uriComponentsBuilder.path("/api/animais/{id}").buildAndExpand(responseDTO.getId()).toUri();

        return ResponseEntity.created(uri).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<AnimalResponseDTO>> buscarTodosAnimais() {

        List<AnimalResponseDTO> listaAnimais = animalService.buscarTodosAnimais();

        return  ResponseEntity.ok(listaAnimais);
    }


    @GetMapping("/{id}")
    public ResponseEntity<AnimalResponseDTO> buscarAnimalPorId(@PathVariable long id){
        AnimalResponseDTO responseDTO = animalService.buscarAnimalPorId(id);
        return ResponseEntity.ok().body(responseDTO);

    }

    @PutMapping("/{id}")
    public ResponseEntity<AnimalResponseDTO> atualizarAnimal(@PathVariable long id, @RequestBody @Valid AnimalUpdateDTO animalDTO) {

        AnimalResponseDTO responseDTO = animalService.atualizarAnimal(id, animalDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping
    public ResponseEntity<AnimalResponseDTO> deletarAnimal(@PathVariable long id){

        AnimalResponseDTO animalResponseDTO = animalService.deleteAnimal(id);
        return  ResponseEntity.ok().body(animalResponseDTO);
    }
}
