package br.com.clinicavet.clinica_api.controller;

import br.com.clinicavet.clinica_api.dto.InternacaoRequestDTO;
import br.com.clinicavet.clinica_api.dto.InternacaoResponseDTO;
import br.com.clinicavet.clinica_api.dto.InternacaoUpdateDTO;
import br.com.clinicavet.clinica_api.service.InternacaoServiceImplement;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;

@RestController
@RequestMapping("/api/internacoes")
public class InternacaoController {

    private final InternacaoServiceImplement internacaoService;

    @Autowired
    public InternacaoController(InternacaoServiceImplement internacaoService) {
        this.internacaoService = internacaoService;
    }

    @PostMapping
    public ResponseEntity<InternacaoResponseDTO> criarInternacao(@RequestBody @Valid InternacaoRequestDTO internacaoRequestDTO, 
                                                               UriComponentsBuilder uriComponentsBuilder) {
        InternacaoResponseDTO responseDTO = internacaoService.criarInternacao(internacaoRequestDTO);

        URI uri = uriComponentsBuilder.path("/api/internacoes/{id}").buildAndExpand(responseDTO.getId()).toUri();

        return ResponseEntity.created(uri).body(responseDTO);
    }

    @GetMapping
    public ResponseEntity<List<InternacaoResponseDTO>> buscarTodasInternacoes() {
        List<InternacaoResponseDTO> listaInternacoes = internacaoService.listarTodos();
        return ResponseEntity.ok(listaInternacoes);
    }

    @GetMapping("/{id}")
    public ResponseEntity<InternacaoResponseDTO> buscarInternacaoPorId(@PathVariable Long id) {
        InternacaoResponseDTO responseDTO = internacaoService.buscarPorId(id);
        return ResponseEntity.ok().body(responseDTO);
    }

    @GetMapping("/ativas")
    public ResponseEntity<List<InternacaoResponseDTO>> buscarInternacoesAtivas() {
        List<InternacaoResponseDTO> listaInternacoes = internacaoService.buscarInternacoesAtivas();
        return ResponseEntity.ok(listaInternacoes);
    }

    @GetMapping("/periodo")
    public ResponseEntity<List<InternacaoResponseDTO>> buscarInternacoesPorPeriodo(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime inicio,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE_TIME) LocalDateTime fim) {
        List<InternacaoResponseDTO> listaInternacoes = internacaoService.buscarPorPeriodo(inicio, fim);
        return ResponseEntity.ok(listaInternacoes);
    }

    @PutMapping("/{id}")
    public ResponseEntity<InternacaoResponseDTO> atualizarInternacao(@PathVariable Long id, 
                                                                   @RequestBody @Valid InternacaoUpdateDTO internacaoUpdateDTO) {
        InternacaoResponseDTO responseDTO = internacaoService.atualizarInternacao(id, internacaoUpdateDTO);
        return ResponseEntity.ok().body(responseDTO);
    }

    @PutMapping("/{id}/finalizar")
    public ResponseEntity<InternacaoResponseDTO> finalizarInternacao(@PathVariable Long id) {
        InternacaoResponseDTO responseDTO = internacaoService.finalizarInternacao(id);
        return ResponseEntity.ok().body(responseDTO);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarInternacao(@PathVariable Long id) {
        internacaoService.deletarInternacao(id);
        return ResponseEntity.noContent().build();
    }
}