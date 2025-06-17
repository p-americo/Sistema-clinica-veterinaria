package br.com.clinicavet.clinica_api.dto;

import br.com.clinicavet.clinica_api.model.enums.EspecieAnimal;
import br.com.clinicavet.clinica_api.model.enums.PorteAnimal;
import br.com.clinicavet.clinica_api.model.enums.SexoAnimal;
import jakarta.validation.constraints.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDate;
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor

public class AnimalUpdateDTO {

    @Size(max = 50)
    private String nome;

    private EspecieAnimal especie;

    private PorteAnimal porte;

    @Size(max = 50)
    private String raca;

    private SexoAnimal sexo;

    @Size(max = 15)
    private String cor;

    @NotNull(message = "O peso do animal é obrigatório.")
    @PositiveOrZero(message = "O peso do animal não pode ser negativo.")
    private BigDecimal peso;

    private boolean castrado;

    @PastOrPresent
    private LocalDate dataNascimento;

    @Size(max = 100)
    private String observacao;

    @Positive(message = "O valor do ID deve ser positivo.")
    private long clienteId;
}
