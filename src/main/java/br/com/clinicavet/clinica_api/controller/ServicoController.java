package br.ifg.meaupet.controller;

import br.ifg.meaupet.dto.ServicoDTO;
import br.ifg.meaupet.services.ServicoService;
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
@RequestMapping("/servico")
public class ServicoController {
    @Autowired
    private ServicoService service;

    @GetMapping
    public Page<ServicoDTO> listarServico(@PageableDefault(size = 10) Pageable paginacao) {
        return service.obterServico(paginacao);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoDTO> detalharServico(@PathVariable @NotNull Long id) {
        ServicoDTO dto = service.obterServicoPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ServicoDTO> cadastrarServico(@RequestBody @Valid ServicoDTO dto, UriComponentsBuilder uriBuilder) {
        ServicoDTO funcionario = service.cadastrarServico(dto);
        URI uri = uriBuilder.path("/pessoas/{id}").buildAndExpand(funcionario.getId()).toUri();
        return ResponseEntity.created(uri).body(funcionario);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoDTO> atualizarServico(@PathVariable @NotNull Long id, @RequestBody ServicoDTO dto, UriComponentsBuilder uriBuilder) {
        ServicoDTO funcionario = service.atualizarServico(id, dto);
        return ResponseEntity.ok(funcionario);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable @NotNull Long id) {
        service.deletarServio(id);
        return ResponseEntity.noContent().build();
    }
}
