package br.com.clinicavet.clinica_api.repository;

import br.com.clinicavet.clinica_api.model.TipoCargo;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CargoRepository extends JpaRepository<TipoCargo, Long> {


    Optional<TipoCargo> findByCargo(br.com.clinicavet.clinica_api.model.enums.TipoCargo cargo);


}
