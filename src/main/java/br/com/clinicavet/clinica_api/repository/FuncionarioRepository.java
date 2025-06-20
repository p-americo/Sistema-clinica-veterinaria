package br.com.clinicavet.clinica_api.repository;


import br.com.clinicavet.clinica_api.model.TipoFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository <TipoFuncionario, Long>{

    boolean existsByCargoId(Long cargoId);

}
