package br.com.clinicavet.clinica_api.controller;

import br.com.clinicavet.clinica_api.dto.InternacaoRequestDTO;
import br.com.clinicavet.clinica_api.dto.InternacaoResponseDTO;
import br.com.clinicavet.clinica_api.service.Interface.InternacaoServiceInterface;
import br.com.clinicavet.clinica_api.service.Interface.InternacaoServiceInterface;
import br.com.clinicavet.clinica_api.service.InternacaoServiceImplement;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import jakarta.validation.Valid;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;



@RestController
@RequestMapping("/api/internacoes")
public class InternacaoController {

    private final InternacaoServiceInterface service;
    private final InternacaoServiceImplement internacaoServiceImplement;

    public InternacaoController(InternacaoServiceInterface service, InternacaoServiceImplement internacaoServiceImplement) {
        this.service = service;
        this.internacaoServiceImplement = internacaoServiceImplement;
    }

    @PostMapping
    public ResponseEntity<InternacaoResponseDTO> criarInternacao(@RequestBody @Valid InternacaoRequestDTO dto, UriComponentsBuilder uriBuilder) {
        InternacaoResponseDTO resposta = internacaoServiceImplement.criarInternacao(dto);
        URI uri = uriBuilder.path("/api/internacoes/{id}").buildAndExpand(resposta.getId()).toUri();
        return ResponseEntity.created(uri).body(resposta);
    }


    @PutMapping("/{id}")
    public ResponseEntity<InternacaoResponseDTO> atualizarInternacao(@PathVariable Long id, @RequestBody @Valid InternacaoRequestDTO dto) {
        return ResponseEntity.ok(service.atualizarInternacao(id, dto));
    }

    @GetMapping("/{id}")
    public ResponseEntity<InternacaoResponseDTO> buscarPorId(@PathVariable Long id) {
        return ResponseEntity.ok(service.buscarPorId(id));
    }

    @GetMapping
    public ResponseEntity<List<InternacaoResponseDTO>> listarTodas() {
        return ResponseEntity.ok(service.listarTodas());
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletar(@PathVariable Long id) {
        service.deletarInternacao(id);
        return ResponseEntity.noContent().build();
    }
}
