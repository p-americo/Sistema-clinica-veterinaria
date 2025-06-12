package br.com.clinicavet.clinica_api.repository;


import br.com.clinicavet.clinica_api.model.EFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository <EFuncionario, Long>{

    boolean existsByCargoId(Long cargoId);

}
