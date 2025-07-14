package br.com.clinicavet.clinica_api.repository;

import br.com.clinicavet.clinica_api.model.TipoAdministracaoMedicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.time.LocalDateTime;
import java.util.List;

public interface AdministracaoMedicamentoRepository extends JpaRepository<TipoAdministracaoMedicamento, Long> {
    
    List<TipoAdministracaoMedicamento> findByEntradaProntuarioId(Long entradaProntuarioId);
    
    List<TipoAdministracaoMedicamento> findByMedicamentoId(Long medicamentoId);
    
    List<TipoAdministracaoMedicamento> findByFuncionarioExecutorId(Long funcionarioId);
    
    @Query("SELECT a FROM TipoAdministracaoMedicamento a WHERE a.dataHora BETWEEN :inicio AND :fim")
    List<TipoAdministracaoMedicamento> findByDataHoraBetween(@Param("inicio") LocalDateTime inicio, @Param("fim") LocalDateTime fim);
    
    @Query("SELECT a FROM TipoAdministracaoMedicamento a " +
           "LEFT JOIN FETCH a.medicamento " +
           "LEFT JOIN FETCH a.funcionarioExecutor " +
           "WHERE a.entradaProntuario.id = :entradaProntuarioId")
    List<TipoAdministracaoMedicamento> findByEntradaProntuarioIdWithDetails(@Param("entradaProntuarioId") Long entradaProntuarioId);
}