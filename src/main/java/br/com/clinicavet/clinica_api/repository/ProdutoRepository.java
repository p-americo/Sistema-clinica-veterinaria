package br.com.clinicavet.clinica_api.repository;

import br.com.clinicavet.clinica_api.model.TipoProduto;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ProdutoRepository extends JpaRepository<TipoProduto, Long> {

    Optional<TipoProduto> findByNomeIgnoreCase(String nome);
}
