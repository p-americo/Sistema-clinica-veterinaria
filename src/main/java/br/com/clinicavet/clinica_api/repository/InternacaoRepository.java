package br.com.clinicavet.clinica_api.repository;

import br.com.clinicavet.clinica_api.model.TipoInternacao;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface InternacaoRepository extends JpaRepository<TipoInternacao, Long> {

    // CORREÇÃO: Usando o nome correto do campo "dataSaida" que existe na entidade.
    // Este método agora encontrará todas as internações que ainda não têm uma data de saída.
    List<TipoInternacao> findByDataSaidaIsNull();

    // Se você estiver usando @Query, a correção seria aqui:
    @Query("SELECT i FROM TipoInternacao i WHERE i.dataSaida IS NULL")
    List<TipoInternacao> findInternacoesAtivas();
}
