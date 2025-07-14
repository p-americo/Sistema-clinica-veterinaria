package br.com.clinicavet.clinica_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.util.ArrayList;
import java.util.List;

@Entity
@Table(name = "prontuarios")
@Getter
@Setter
public class TipoProntuario {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Relacionamento um-para-um: Um animal tem um prontuário.
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false, unique = true)
    private TipoAnimal animal;

    @Column(name = "alergias_conhecidas", columnDefinition = "TEXT")
    private String alergiasConhecidas;

    @Column(name = "condicoes_preexistentes", columnDefinition = "TEXT")
    private String condicoesPreexistentes;

    // Um prontuário tem uma lista de entradas de histórico
    @OneToMany(mappedBy = "prontuario", cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<TipoRegistroProntuario> registros = new ArrayList<>();
}