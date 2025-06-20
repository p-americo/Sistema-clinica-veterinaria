package br.com.clinicavet.clinica_api.model;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;
import java.time.LocalDate;

@Entity
@Table(name = "funcionarios")
@Getter
@Setter
@PrimaryKeyJoinColumn(name = "pessoa_id")
public class TipoFuncionario extends TipoPessoa {


    @Column(name = "data_admissao", nullable = false)
    private LocalDate dataAdmissao;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "cargo_id", nullable = false)
    private TipoCargo cargo;

    @Column(name = "crmv", unique = true)
    private String crmv;

}