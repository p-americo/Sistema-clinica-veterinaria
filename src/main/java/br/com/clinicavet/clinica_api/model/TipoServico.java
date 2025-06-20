package br.com.clinicavet.clinica_api.model;

import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal;

@Entity
@Table(name = "servicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class TipoServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private br.com.clinicavet.clinica_api.model.enums.TipoServico tipo;

    @ManyToOne
    @JoinColumn(name = "veterinario_id", nullable = false)
    private TipoFuncionario veterinario;

    @Column(nullable = false, precision = 10, scale = 2)
    private BigDecimal valor;
}
