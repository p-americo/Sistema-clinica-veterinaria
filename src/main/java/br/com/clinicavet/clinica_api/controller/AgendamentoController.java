package br.ifg.meaupet.controller;

import br.ifg.meaupet.dto.AgendamentoDTO;
import br.ifg.meaupet.services.AgendamentoService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@RestController
@RequestMapping("/agendamento")
public class AgendamentoController {
    @Autowired
    private AgendamentoService service;

    @GetMapping
    public Page<AgendamentoDTO> listarAgendamento(@PageableDefault(size = 10) Pageable paginacao) {
        return service.obterAnimal(paginacao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<AgendamentoDTO> detalharAgendamento(@PathVariable @NotNull Long id) {
        AgendamentoDTO dto = service.obterAnimalPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<AgendamentoDTO> cadastrarAgendamento(@RequestBody @Valid AgendamentoDTO dto, UriComponentsBuilder uriBuilder) {
        AgendamentoDTO agendamento = service.cadastrarAnimal(dto);
        URI uri = uriBuilder.path("/pessoas/{id}").buildAndExpand(agendamento.getId()).toUri();
        return ResponseEntity.created(uri).body(agendamento);
    }

    @PutMapping("/{id}")
    public ResponseEntity<AgendamentoDTO> atualizarAnimal(@PathVariable @NotNull Long id, @RequestBody AgendamentoDTO dto, UriComponentsBuilder uriBuilder) {
        AgendamentoDTO pessoa = service.atualizarAnimal(id, dto);
        return ResponseEntity.ok(pessoa);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarAnimal(@PathVariable @NotNull Long id) {
        service.deletarAnimal(id);
        return ResponseEntity.noContent().build();
    }
}
