package br.com.clinicavet.clinica_api.repository;


import br.com.clinicavet.clinica_api.model.TipoAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository <TipoAgendamento, Long>{

}
