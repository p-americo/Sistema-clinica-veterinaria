package br.com.clinicavet.clinica_api.repository;

import br.com.clinicavet.clinica_api.model.TipoRegistroProntuario;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface RegistroProntuarioRepository extends JpaRepository<TipoRegistroProntuario, Long> {
    
    List<TipoRegistroProntuario> findByProntuarioId(Long prontuarioId);
    
    List<TipoRegistroProntuario> findByProntuarioIdOrderByDataHoraDesc(Long prontuarioId);
    
    List<TipoRegistroProntuario> findByVeterinarioResponsavelId(Long veterinarioId);
    
    List<TipoRegistroProntuario> findByInternacaoId(Long internacaoId);
    
    @Query("SELECT r FROM TipoRegistroProntuario r WHERE r.dataHora BETWEEN :inicio AND :fim")
    List<TipoRegistroProntuario> findByDataHoraBetween(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
    
    @Query("SELECT r FROM TipoRegistroProntuario r " +
           "LEFT JOIN FETCH r.medicamentosAdministrados " +
           "WHERE r.id = :id")
    TipoRegistroProntuario findByIdWithMedicamentos(@Param("id") Long id);
    
    @Query("SELECT r FROM TipoRegistroProntuario r " +
           "LEFT JOIN FETCH r.veterinarioResponsavel " +
           "LEFT JOIN FETCH r.internacao " +
           "WHERE r.prontuario.id = :prontuarioId " +
           "ORDER BY r.dataHora DESC")
    List<TipoRegistroProntuario> findByProntuarioIdWithDetails(@Param("prontuarioId") Long prontuarioId);
}

