package br.com.clinicavet.clinica_api.repository;

import br.com.clinicavet.clinica_api.model.ECargo;
import br.com.clinicavet.clinica_api.model.enums.TipoCargo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CargoRepository extends JpaRepository<ECargo, Long> {


    Optional<ECargo> findByCargo(TipoCargo cargo);


}
