package br.com.clinicavet.clinica_api.model;

import jakarta.persistence.*;
import lombok.*;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.jpa.domain.support.AuditingEntityListener;

import java.time.LocalDate;
import java.util.List;


@Table(name = "clientes")
@Getter
@Setter
@Entity
@NoArgsConstructor
@AllArgsConstructor
@EntityListeners(AuditingEntityListener.class)

public abstract class ECliente extends EPessoa {

    @CreatedDate
    @Column(name = "data_cadastro", nullable = false, updatable = false)
    private LocalDate dataCadastro;

    //relacionamento inverso, listar todos os animais de cliente, não precisa passar id ou algo do tipo, somente em animal
    @OneToMany(mappedBy = "cliente",  cascade = CascadeType.ALL, fetch = FetchType.LAZY)
    private List<EAnimal> animais;

}
