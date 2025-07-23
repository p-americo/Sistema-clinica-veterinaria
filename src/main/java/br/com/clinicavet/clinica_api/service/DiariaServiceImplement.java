package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.DiariaRequestDTO;
import br.com.clinicavet.clinica_api.dto.DiariaResponseDTO;
import br.com.clinicavet.clinica_api.model.TipoDiariaInternacao;
import br.com.clinicavet.clinica_api.model.TipoInternacao;
import br.com.clinicavet.clinica_api.repository.DiariaRepository;
import br.com.clinicavet.clinica_api.repository.InternacaoRepository;
import br.com.clinicavet.clinica_api.service.Interface.DiariaService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class DiariaServiceImplement implements DiariaService {

    private final DiariaRepository diariaRepository;
    private final InternacaoRepository internacaoRepository;
    private final ModelMapper mapper;

    public DiariaServiceImplement(DiariaRepository diariaRepository, InternacaoRepository internacaoRepository, ModelMapper mapper) {
        this.diariaRepository = diariaRepository;
        this.internacaoRepository = internacaoRepository;
        this.mapper = mapper;
    }

    @Override
    @Transactional // É uma boa prática adicionar @Transactional em métodos que modificam o banco
    public DiariaResponseDTO criarDiaria(DiariaRequestDTO dto) {
        TipoInternacao internacao = internacaoRepository.findById(dto.getInternacaoId())
                .orElseThrow(() -> new NoSuchElementException("Internação não encontrada com o ID: " + dto.getInternacaoId()));

        TipoDiariaInternacao diaria = mapper.map(dto, TipoDiariaInternacao.class);

        // LINHA ADICIONADA PARA CORRIGIR O ERRO
        // Garante que o JPA entenda que esta é uma operação de INSERT, não UPDATE.
        diaria.setId(null);

        diaria.setInternacao(internacao);

   /* if (dto.getMedicamentos() != null) {
        List<TipoAdminMedicamento> medicamentos = dto.getMedicamentos().stream().map(medDto -> {
            TipoAdminMedicamento med = new TipoAdminMedicamento();
            med.setNome(medDto.getNome());
            med.setDosagem(medDto.getDosagem());
            med.setDiaria(diaria);
            return med;
        }).collect(Collectors.toList());

        diaria.setMedicamentos(medicamentos);
    }*/

        TipoDiariaInternacao salva = diariaRepository.save(diaria);
        return mapper.map(salva, DiariaResponseDTO.class);
    }


    @Override
    public DiariaResponseDTO atualizarDiaria(Long id, DiariaRequestDTO dto) {
        TipoDiariaInternacao diaria = diariaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Diária não encontrada"));

        mapper.map(dto, diaria);
        diaria.getMedicamentos().clear();
/*
        if (dto.getMedicamentos() != null) {
            List<TipoAdminstracaoMedicamento> medicamentos = dto.getMedicamentos().stream().map(medDto -> {
                TipoAdminstracaoMedicamento med = new TipoAdminstracaoMedicamento();
                med.setNome(medDto.getNome());
                med.setDosagem(medDto.getDosagem());
                med.setDiaria(diaria);
                return med;
            }).collect(Collectors.toList());

            diaria.getMedicamentos().addAll(medicamentos);
        }
*/
        TipoDiariaInternacao salva = diariaRepository.save(diaria);
        return mapper.map(salva, DiariaResponseDTO.class);
    }

    @Override
    public DiariaResponseDTO buscarPorId(Long id) {
        TipoDiariaInternacao diaria = diariaRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Diária não encontrada"));
        return mapper.map(diaria, DiariaResponseDTO.class);
    }

    @Override
    public List<DiariaResponseDTO> listarTodas() {
        return diariaRepository.findAll().stream()
                .map(diaria -> mapper.map(diaria, DiariaResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public List<DiariaResponseDTO> listarPorInternacao(Long internacaoId) {
        return diariaRepository.findByInternacaoId(internacaoId).stream()
                .map(diaria -> mapper.map(diaria, DiariaResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Override
    public void deletarDiaria(Long id) {
        if (!diariaRepository.existsById(id)) {
            throw new NoSuchElementException("Diária não encontrada");
        }
        diariaRepository.deleteById(id);
    }
}