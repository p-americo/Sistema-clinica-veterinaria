package br.com.clinicavet.clinica_api.dto;

import br.com.clinicavet.clinica_api.model.enums.TipoServico;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class ServicoResponseDTO {

    private Long id;
    private TipoServico tipo;
    private double valor;
    private String nomeVeterinario;
}
