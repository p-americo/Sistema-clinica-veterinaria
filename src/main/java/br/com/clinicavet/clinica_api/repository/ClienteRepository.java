package br.com.clinicavet.clinica_api.repository;

import br.com.clinicavet.clinica_api.model.ECliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<ECliente, Long> {
}
