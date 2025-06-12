package br.com.clinicavet.clinica_api.repository;

import br.com.clinicavet.clinica_api.model.EServico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<EServico, Long> {
}
