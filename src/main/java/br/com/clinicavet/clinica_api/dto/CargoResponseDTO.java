package br.com.clinicavet.clinica_api.dto;

import br.com.clinicavet.clinica_api.model.enums.TipoCargo;
import lombok.Getter;
import lombok.Setter;
import java.math.BigDecimal;

@Getter
@Setter
public class CargoResponseDTO {

    private Long id;
    private TipoCargo cargo;
    private BigDecimal salario;
}