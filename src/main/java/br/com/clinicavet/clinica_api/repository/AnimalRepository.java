package br.com.clinicavet.clinica_api.repository;

import br.com.clinicavet.clinica_api.model.TipoAnimal;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AnimalRepository extends JpaRepository<TipoAnimal, Long> {

    boolean existsByNome(String nome);
    TipoAnimal findByNome(String nome);
    boolean existsByClienteId(Long clienteId);
}
