package br.com.clinicavet.clinica_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "administracoes_medicamentos")
@Getter
@Setter

public class TipoAdministracaoMedicamento {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // CORRETO: Referência à entidade EMedicamento (o produto em si)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "medicamento_id", nullable = false)
    private TipoMedicamento medicamento;

    // Relacionamento com a "folha de anotações" onde este ato foi registrado
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "entrada_prontuario_id", nullable = false)
    private TipoRegistroProntuario entradaProntuario;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "funcionario_executor_id", nullable = false)
    private TipoFuncionario funcionarioExecutor;

    @Column(nullable = false)
    private BigDecimal quantidadeAdministrada;

    @Column(nullable = false)
    private LocalDateTime dataHora;

    private String dosagem; // A dosagem pode ser registrada aqui (ex: "5mg/ml")

}
