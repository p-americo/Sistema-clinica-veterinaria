package br.com.clinicavet.clinica_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "medicamentos")
@Getter
@Setter
public class TipoMedicamento extends TipoProduto {

    @Id
    private Long id;

    // Bora trabalhar yuri, implementar aqui

}