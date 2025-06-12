package br.com.clinicavet.clinica_api.controller;

import br.com.clinicavet.clinica_api.dto.CargoRequestDTO;
import br.com.clinicavet.clinica_api.dto.CargoResponseDTO;
import br.com.clinicavet.clinica_api.service.CargoService;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/cargos")
public class CargoController {

    private final CargoService cargoService;

    public CargoController(CargoService cargoService) {
        this.cargoService = cargoService;
    }

    @PostMapping
    public ResponseEntity<CargoResponseDTO> criarCargo(@RequestBody @Valid CargoRequestDTO requestDTO, UriComponentsBuilder uriBuilder) {
        CargoResponseDTO responseDTO = cargoService.criarCargo(requestDTO);
        URI uri = uriBuilder.path("/api/cargos/{id}").buildAndExpand(responseDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<CargoResponseDTO>> listarCargos() {
        return ResponseEntity.ok(cargoService.listarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<CargoResponseDTO> buscarCargoPorId(@PathVariable Long id) {
        return ResponseEntity.ok(cargoService.buscarPorId(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<CargoResponseDTO> atualizarCargo(@PathVariable Long id, @RequestBody @Valid CargoRequestDTO requestDTO) {
        CargoResponseDTO responseDTO = cargoService.atualizarCargo(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarCargo(@PathVariable Long id) {
        cargoService.deletarCargo(id);
        return ResponseEntity.noContent().build();
    }
}