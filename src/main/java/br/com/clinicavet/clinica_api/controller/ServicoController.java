package br.com.clinicavet.clinica_api.controller;

import br.com.clinicavet.clinica_api.dto.ServicoRequestDTO; // << DTO para entrada
import br.com.clinicavet.clinica_api.dto.ServicoResponseDTO; // << DTO para saída
import br.com.clinicavet.clinica_api.service.ServicoService;
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
@RequestMapping("/api/servicos") // << Boa prática: plural e com prefixo /api
public class ServicoController {

    private final ServicoService service; // << Usando injeção via construtor

    @Autowired
    public ServicoController(ServicoService service) {
        this.service = service;
    }

    @GetMapping
    public ResponseEntity<Page<ServicoResponseDTO>> listarServicos(@PageableDefault(size = 10, sort = "tipo") Pageable paginacao) {
        Page<ServicoResponseDTO> paginaDeServicos = service.obterTodosServicos(paginacao);
        return ResponseEntity.ok(paginaDeServicos);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> detalharServico(@PathVariable @NotNull Long id) {
        // Usando o tipo de DTO correto (Response)
        ServicoResponseDTO dto = service.obterServicoPorId(id);
        return ResponseEntity.ok(dto);
    }

    @PostMapping
    public ResponseEntity<ServicoResponseDTO> cadastrarServico(@RequestBody @Valid ServicoRequestDTO dto, UriComponentsBuilder uriBuilder) {
        // Renomeando variável e usando o tipo de DTO correto (Response)
        ServicoResponseDTO servicoSalvo = service.cadastrarServico(dto);
        // Corrigindo o path da URI para o recurso correto
        URI uri = uriBuilder.path("/api/servicos/{id}").buildAndExpand(servicoSalvo.getId()).toUri();
        return ResponseEntity.created(uri).body(servicoSalvo);
    }

    @PutMapping("/{id}")
    public ResponseEntity<ServicoResponseDTO> atualizarServico(@PathVariable @NotNull Long id, @RequestBody @Valid ServicoRequestDTO dto) {
        // Renomeando variável, usando o tipo de DTO correto e removendo UriComponentsBuilder desnecessário
        ServicoResponseDTO servicoAtualizado = service.atualizarServico(id, dto);
        return ResponseEntity.ok(servicoAtualizado);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarServico(@PathVariable @NotNull Long id) {
        // Corrigindo o erro de digitação na chamada do serviço
        service.deletarServico(id);
        return ResponseEntity.noContent().build();
    }
}