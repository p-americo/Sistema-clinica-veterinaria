package br.ifg.meaupet.repository;

import br.ifg.meaupet.model.EAgendamento;
import org.springframework.data.jpa.repository.JpaRepository;

public interface AgendamentoRepository extends JpaRepository <EAgendamento, Long>{

}
