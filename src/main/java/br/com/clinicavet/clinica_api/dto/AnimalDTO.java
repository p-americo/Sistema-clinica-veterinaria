package br.com.clinicavet.clinica_api.dto;

import br.com.clinicavet.clinica_api.model.enums.PorteAnimal;
import br.com.clinicavet.clinica_api.model.enums.TipoAnimal;
import jakarta.validation.constraints.NotBlank;
import lombok.Getter;
import lombok.Setter;
@Getter
@Setter

public class AnimalDTO {

    @NotBlank
    private long id;
    @NotBlank
    private String nome;
    @NotBlank
    private String raca;
    @NotBlank
    private double peso;
    @NotBlank
    private java.time.LocalDate dataNascimento;
    @NotBlank
    private PorteAnimal porteAnimal;
    @NotBlank
    private TipoAnimal tipoAnimal;


}
