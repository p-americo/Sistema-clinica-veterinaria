package br.com.clinicavet.clinica_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.time.LocalDateTime;

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
    
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "veterinario_responsavel_id", nullable = false)
    private TipoFuncionario veterinarioResponsavel;
    
    @Column(name = "data_inicio", nullable = false)
    private LocalDateTime dataInicio;
    
    @Column(name = "data_fim")
    private LocalDateTime dataFim;
    
    @Column(columnDefinition = "TEXT")
    private String motivoInternacao;
    
    @Column(columnDefinition = "TEXT")
    private String observacoes;
    
    @Column(name = "ativa", nullable = false)
    private Boolean ativa = true;
}
