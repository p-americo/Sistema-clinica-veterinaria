package br.com.clinicavet.clinica_api.model;

import br.com.clinicavet.clinica_api.model.enums.PorteAnimal;
import br.com.clinicavet.clinica_api.model.enums.EspecieAnimal;
import br.com.clinicavet.clinica_api.model.enums.SexoAnimal;
import jakarta.persistence.*;
import lombok.*;

import java.time.LocalDate;


@Entity
@Table(name = "animais")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")

public class EAnimal {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(length = 50, nullable = false)
    private String nome;

    @Enumerated(EnumType.STRING)
    @Column(name = "especie", nullable = false)
    private EspecieAnimal especie;

   @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private PorteAnimal porte;

    @Column(length = 50, nullable = false)
    private String raca;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private SexoAnimal sexo;

    @Column(length = 15)
    private String cor;

    @Column(nullable = false)
    private double peso;

    @Column(nullable = false)
    private boolean castrado;

    @Column(name = "data_nascimento",  nullable = false)
    private LocalDate dataNascimento;

    @Column(length = 255)
    private String observacao;

    @ManyToOne
    @JoinColumn(name = "cliente_id", nullable = false)
    private ECliente cliente;

}
