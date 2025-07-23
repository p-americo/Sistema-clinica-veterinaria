package br.com.clinicavet.clinica_api.repository;

import br.com.clinicavet.clinica_api.model.TipoMedicamento;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface MedicamentoRepository extends JpaRepository<TipoMedicamento, Long> {


    List<TipoMedicamento> findByProdutoNomeContainingIgnoreCase(String nome);


    Optional<TipoMedicamento> findByProdutoNomeIgnoreCase(String nome);
}
