package br.ifg.meaupet.repository;

import br.ifg.meaupet.model.EFuncionario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FuncionarioRepository extends JpaRepository <EFuncionario, Long>{

}
