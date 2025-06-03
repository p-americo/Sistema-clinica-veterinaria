package br.com.clinicavet.clinica_api.model;

import br.com.clinicavet.clinica_api.model.enums.PorteAnimal;
import br.com.clinicavet.clinica_api.model.enums.TipoAnimal;
import jakarta.persistence.*;
import lombok.*;



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

    @Column(nullable = false)
    private String raca;

    private String cor;

    @Column(nullable = false)
    private double peso;

    @Column(name = "data_nascimento",  nullable = false)
    private java.time.LocalDate dataNascimento;

    @Column(nullable = false)
    private PorteAnimal porte;

    @Column(name = "tipo_animal", nullable = false)
    private TipoAnimal tipoAnimal;


}
