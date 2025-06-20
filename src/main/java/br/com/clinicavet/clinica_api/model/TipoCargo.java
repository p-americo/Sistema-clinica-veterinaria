package br.com.clinicavet.clinica_api.model;

import br.com.clinicavet.clinica_api.model.enums.TipoCargo;
import jakarta.persistence.*;
import lombok.*;

import java.math.BigDecimal; // << Importar para o salário
import java.util.List;

@Entity
@Table(name = "cargos")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ECargo {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY) // << Especificar a estratégia
    private Long id;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false, unique = true)
    private TipoCargo cargo;

    @Column(nullable = false, precision = 10, scale = 2) // Definindo precisão para o banco
    private BigDecimal salario;

    @OneToMany(mappedBy = "cargo")
    private List<EFuncionario> funcionarios;

}