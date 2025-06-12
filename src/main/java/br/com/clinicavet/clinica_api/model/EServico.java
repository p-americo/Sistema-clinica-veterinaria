package br.com.clinicavet.clinica_api.model;

import br.com.clinicavet.clinica_api.model.enums.TipoServico;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "servicos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode(of = "id")
public class EServico {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TipoServico tipo;

    @ManyToOne
    @JoinColumn(name = "veterinario_id", nullable = false)
    //private EVeterinario veterinario;

    @Column(nullable = false)
    private double valor;
}
