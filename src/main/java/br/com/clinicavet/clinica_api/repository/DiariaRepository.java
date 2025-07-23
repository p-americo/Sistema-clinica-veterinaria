package br.com.clinicavet.clinica_api.repository;

import br.com.clinicavet.clinica_api.model.TipoDiariaInternacao;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface DiariaRepository extends JpaRepository<TipoDiariaInternacao, Long> {
    List<TipoDiariaInternacao> findByInternacaoId(Long internacaoId);
}
