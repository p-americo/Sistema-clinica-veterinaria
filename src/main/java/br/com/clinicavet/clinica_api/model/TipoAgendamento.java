package br.com.clinicavet.clinica_api.model;

import br.com.clinicavet.clinica_api.model.enums.StatusAgendamento; // << CRIAR ESTE ENUM
import jakarta.persistence.*;
import lombok.*;
import java.time.LocalDateTime;

@Entity
@Table(name = "agendamentos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class TipoAgendamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "animal_id", nullable = false)
    private TipoAnimal animal;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "servico_id", nullable = false)
    private TipoServico servico;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cliente_id", nullable = false)
    private TipoCliente cliente;

    @Column(name = "data_hora_agendamento", nullable = false)
    private LocalDateTime dataHoraAgendamento;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private StatusAgendamento status = StatusAgendamento.AGENDADO;

    @Column(length = 255)
    private String observacoes;
}