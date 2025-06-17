package br.com.clinicavet.clinica_api.dto;

import br.com.clinicavet.clinica_api.model.enums.TipoServico;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
@Getter
@Setter

public class ServicoResponseDTO {

    private Long id;

    private TipoServico tipo;

    private BigDecimal valor;
}
