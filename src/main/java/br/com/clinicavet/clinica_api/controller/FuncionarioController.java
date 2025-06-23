package br.com.clinicavet.clinica_api.controller;

import br.com.clinicavet.clinica_api.dto.FuncionarioRequestDTO;
import br.com.clinicavet.clinica_api.dto.FuncionarioResponseDTO;
import br.com.clinicavet.clinica_api.dto.FuncionarioUpdateDTO;
import br.com.clinicavet.clinica_api.service.FuncionarioServiceImplement;
import jakarta.validation.Valid;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@RestController
@RequestMapping("/api/funcionarios")
public class FuncionarioController {

    private final FuncionarioServiceImplement funcionarioServiceImplement;

    public FuncionarioController(FuncionarioServiceImplement funcionarioServiceImplement) {
        this.funcionarioServiceImplement = funcionarioServiceImplement;
    }

    @PostMapping
    public ResponseEntity<FuncionarioResponseDTO> criarFuncionario(@RequestBody @Valid FuncionarioRequestDTO requestDTO, UriComponentsBuilder uriBuilder) {
        FuncionarioResponseDTO responseDTO = funcionarioServiceImplement.criarFuncionario(requestDTO);
        URI uri = uriBuilder.path("/api/funcionarios/{id}").buildAndExpand(responseDTO.getId()).toUri();
        return ResponseEntity.created(uri).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<FuncionarioResponseDTO>> listarFuncionarios() {
        return ResponseEntity.ok(funcionarioServiceImplement.buscarTodos());
    }

    @GetMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> buscarFuncionarioPorId(@PathVariable Long id) {
        return ResponseEntity.ok(funcionarioServiceImplement.buscarPorId(id));
    }

    // Dentro da classe FuncionarioController

    @PutMapping("/{id}")
    public ResponseEntity<FuncionarioResponseDTO> atualizarFuncionario(@PathVariable Long id, @RequestBody @Valid FuncionarioUpdateDTO requestDTO) {
        FuncionarioResponseDTO responseDTO = funcionarioServiceImplement.atualizarFuncionario(id, requestDTO);
        return ResponseEntity.ok(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarFuncionario(@PathVariable Long id) {
        funcionarioServiceImplement.deletarFuncionario(id);
        return ResponseEntity.noContent().build();
    }

}