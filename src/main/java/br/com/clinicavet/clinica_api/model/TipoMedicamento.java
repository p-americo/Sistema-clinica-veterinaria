package br.com.clinicavet.clinica_api.model;

import br.com.clinicavet.clinica_api.model.enums.EnumCategoriaMedicameno;
import br.com.clinicavet.clinica_api.model.enums.EnumViaMedicamento;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "medicamentos")
@Getter
@Setter
public class TipoMedicamento extends TipoProduto {

    @Enumerated(EnumType.STRING)
    @Column(name = "categoria")
    private EnumCategoriaMedicameno categoria;
    
    @Enumerated(EnumType.STRING)
    @Column(name = "via_administracao")
    private EnumViaMedicamento viaAdministracao;
    
    @Column(name = "dosagem_padrao", length = 100)
    private String dosagemPadrao;
    
    @Column(name = "principio_ativo", length = 200)
    private String principioAtivo;
    
    @Column(name = "fabricante", length = 100)
    private String fabricante;
    
    @Column(name = "lote", length = 50)
    private String lote;
    
    @Column(name = "prescricao_obrigatoria")
    private Boolean prescricaoObrigatoria = false;
}