package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.service.Interface.MedicamentoService;
import org.springframework.stereotype.Service;

@Service
public class ProntuarioService {

    private final MedicamentoService medicamentoService;
    
    public ProntuarioService(MedicamentoService medicamentoService) {
        this.medicamentoService = medicamentoService;
    }
}
