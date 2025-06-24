package br.com.clinicavet.clinica_api.dto;

import br.com.clinicavet.clinica_api.model.enums.EnumEspecie;
import br.com.clinicavet.clinica_api.model.enums.EnumPorte;
import br.com.clinicavet.clinica_api.model.enums.EnumSexo;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AnimalResponseDTO {

    private Long id;

    private String nome;

    private EnumEspecie especie;

    private EnumPorte porte;

    private String raca;

    private EnumSexo sexo;

    private String cor;

    private double peso;

    private boolean castrado;

    private String idadeFormatada;

    private String observacao;

    //private ECliente cliente;

}
