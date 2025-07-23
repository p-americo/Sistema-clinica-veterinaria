package br.com.clinicavet.clinica_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "internacoes")
@Getter
@Setter
public class TipoInternacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private TipoAnimal animal;

    @Column(name = "data_entrada", nullable = false)
    private LocalDateTime dataEntrada;

    @Column(name = "data_saida")
    private LocalDateTime dataSaida;

    @Column(name = "status", nullable = false)
    private String status; // INTERNADO / ALTA / CANCELADO

    /*@OneToMany(mappedBy = "internacao", cascade = CascadeType.ALL)
    private List<TipoRegistroProntuario> registros = new ArrayList<>();*/
}
