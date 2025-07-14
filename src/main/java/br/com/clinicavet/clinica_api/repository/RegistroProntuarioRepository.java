package br.com.clinicavet.clinica_api.repository;

import br.com.clinicavet.clinica_api.model.TipoRegistroProntuario;
import org.springframework.data.jpa.repository.JpaRepository;

public interface RegistroProntuarioRepository extends JpaRepository<TipoRegistroProntuario, Long> {
}

