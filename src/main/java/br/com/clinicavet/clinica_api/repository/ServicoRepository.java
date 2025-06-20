package br.com.clinicavet.clinica_api.repository;

import br.com.clinicavet.clinica_api.model.TipoServico;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ServicoRepository extends JpaRepository<TipoServico, Long> {

    boolean existsByVeterinarioId(Long veterinarioId);
}
