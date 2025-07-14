package br.com.clinicavet.clinica_api.model;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;

@Entity
public class TipoInternacao {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
}
