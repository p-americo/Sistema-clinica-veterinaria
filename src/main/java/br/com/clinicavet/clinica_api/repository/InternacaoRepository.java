package br.com.clinicavet.clinica_api.repository;

import br.com.clinicavet.clinica_api.model.TipoInternacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface InternacaoRepository extends JpaRepository<TipoInternacao, Long> {
    
    @Query("SELECT i FROM TipoInternacao i WHERE i.dataInicio BETWEEN :inicio AND :fim")
    List<TipoInternacao> findByDataInicioBetween(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
    
    @Query("SELECT i FROM TipoInternacao i WHERE i.dataFim IS NULL")
    List<TipoInternacao> findInternacoesAtivas();
}