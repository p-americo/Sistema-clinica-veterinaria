package br.com.clinicavet.clinica_api.dto;

import br.com.clinicavet.clinica_api.model.ECliente;
import br.com.clinicavet.clinica_api.model.enums.EspecieAnimal;
import br.com.clinicavet.clinica_api.model.enums.PorteAnimal;
import br.com.clinicavet.clinica_api.model.enums.SexoAnimal;
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

    private EspecieAnimal especie;

    private PorteAnimal porte;

    private String raca;

    private SexoAnimal sexo;

    private String cor;

    private double peso;

    private boolean castrado;

    private String idadeFormatada;

    private String observacao;

    //private ECliente cliente;

}
