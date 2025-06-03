package br.com.clinicavet.clinica_api.repository;

import br.com.clinicavet.clinica_api.model.EAnimal;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

public interface AnimalRepository extends JpaRepository<EAnimal, Long> {
}
