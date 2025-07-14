package br.com.clinicavet.clinica_api.dto;

import br.com.clinicavet.clinica_api.model.enums.EnumCategoriaMedicameno;
import br.com.clinicavet.clinica_api.model.enums.EnumViaMedicamento;

public class MedicamentoResponseDTO {

        private Long id;
        private EnumCategoriaMedicameno categoria; // E outros campos que o prontuário precise ver
        private EnumViaMedicamento via;
        private String principioAtivo;

}
