package br.com.clinicavet.clinica_api.dto;

import br.com.clinicavet.clinica_api.model.enums.TipoServico;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicoRequestDTO {

    @NotNull
    private TipoServico tipo;

    @NotNull
    private Long veterinarioId;

    @Positive
    private double valor;
}
