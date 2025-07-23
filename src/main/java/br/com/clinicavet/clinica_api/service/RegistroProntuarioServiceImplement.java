package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.AdministracaoMedicamentoResponseDTO;
import br.com.clinicavet.clinica_api.dto.RegistroProntuarioRequestDTO;
import br.com.clinicavet.clinica_api.dto.RegistroProntuarioResponseDTO;
import br.com.clinicavet.clinica_api.dto.RegistroProntuarioUpdateDTO;
import br.com.clinicavet.clinica_api.model.*;
import br.com.clinicavet.clinica_api.repository.*;
import br.com.clinicavet.clinica_api.service.Interface.RegistroProntuarioServiceInterface;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class RegistroProntuarioServiceImplement implements RegistroProntuarioServiceInterface {

    private final RegistroProntuarioRepository registroProntuarioRepository;
    private final ProntuarioRepository prontuarioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final AgendamentoRepository agendamentoRepository;
    private final InternacaoRepository internacaoRepository;
    private final ModelMapper modelMapper;

    public RegistroProntuarioServiceImplement(RegistroProntuarioRepository registroProntuarioRepository, ProntuarioRepository prontuarioRepository, FuncionarioRepository funcionarioRepository, AgendamentoRepository agendamentoRepository, InternacaoRepository internacaoRepository, ModelMapper modelMapper) {
        this.registroProntuarioRepository = registroProntuarioRepository;
        this.prontuarioRepository = prontuarioRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.agendamentoRepository = agendamentoRepository;
        this.internacaoRepository = internacaoRepository;
        this.modelMapper = modelMapper;
    }

    @Override
    @Transactional
    public RegistroProntuarioResponseDTO criarRegistro(RegistroProntuarioRequestDTO registroRequestDTO) {
        // Verificar se o prontuário existe
        TipoProntuario prontuario = prontuarioRepository.findById(registroRequestDTO.getProntuarioId())
                .orElseThrow(() -> new NoSuchElementException("Prontuário não encontrado com o ID: " + registroRequestDTO.getProntuarioId()));

        // Verificar se o veterinário existe
        TipoFuncionario veterinario = funcionarioRepository.findById(registroRequestDTO.getVeterinarioResponsavelId())
                .orElseThrow(() -> new NoSuchElementException("Veterinário não encontrado com o ID: " + registroRequestDTO.getVeterinarioResponsavelId()));

        TipoRegistroProntuario novoRegistro = modelMapper.map(registroRequestDTO, TipoRegistroProntuario.class);
        novoRegistro.setId(null);
        novoRegistro.setProntuario(prontuario);
        novoRegistro.setVeterinarioResponsavel(veterinario);

        // Relacionar com agendamento se fornecido
        if (registroRequestDTO.getAgendamentoId() != null) {
            TipoAgendamento agendamento = agendamentoRepository.findById(registroRequestDTO.getAgendamentoId())
                    .orElseThrow(() -> new NoSuchElementException("Agendamento não encontrado com o ID: " + registroRequestDTO.getAgendamentoId()));
            novoRegistro.setAgendamento(agendamento);
        }

        // Relacionar com internação se fornecido
        if (registroRequestDTO.getInternacaoId() != null) {
            TipoInternacao internacao = internacaoRepository.findById(registroRequestDTO.getInternacaoId())
                    .orElseThrow(() -> new NoSuchElementException("Internação não encontrada com o ID: " + registroRequestDTO.getInternacaoId()));
            novoRegistro.setInternacao(internacao);
        }

        TipoRegistroProntuario registroSalvo = registroProntuarioRepository.save(novoRegistro);

        return mapEntidadeParaResponse(registroSalvo);
    }

    @Override
    @Transactional
    public RegistroProntuarioResponseDTO atualizarRegistro(Long id, RegistroProntuarioUpdateDTO registroUpdateDTO) {
        TipoRegistroProntuario registroExistente = registroProntuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Registro do prontuário não encontrado com o ID: " + id));

        modelMapper.map(registroUpdateDTO, registroExistente);

        // Atualizar veterinário se fornecido
        if (registroUpdateDTO.getVeterinarioResponsavelId() != null) {
            TipoFuncionario veterinario = funcionarioRepository.findById(registroUpdateDTO.getVeterinarioResponsavelId())
                    .orElseThrow(() -> new NoSuchElementException("Veterinário não encontrado com o ID: " + registroUpdateDTO.getVeterinarioResponsavelId()));
            registroExistente.setVeterinarioResponsavel(veterinario);
        }

        // Atualizar agendamento se fornecido
        if (registroUpdateDTO.getAgendamentoId() != null) {
            TipoAgendamento agendamento = agendamentoRepository.findById(registroUpdateDTO.getAgendamentoId())
                    .orElseThrow(() -> new NoSuchElementException("Agendamento não encontrado com o ID: " + registroUpdateDTO.getAgendamentoId()));
            registroExistente.setAgendamento(agendamento);
        }

        // Atualizar internação se fornecido
        if (registroUpdateDTO.getInternacaoId() != null) {
            TipoInternacao internacao = internacaoRepository.findById(registroUpdateDTO.getInternacaoId())
                    .orElseThrow(() -> new NoSuchElementException("Internação não encontrada com o ID: " + registroUpdateDTO.getInternacaoId()));
            registroExistente.setInternacao(internacao);
        }

        TipoRegistroProntuario registroAtualizado = registroProntuarioRepository.save(registroExistente);
        return mapEntidadeParaResponse(registroAtualizado);
    }

    @Override
    @Transactional
    public void deletarRegistro(Long id) {
        if (!registroProntuarioRepository.existsById(id)) {
            throw new NoSuchElementException("Registro do prontuário não encontrado com o ID: " + id);
        }
        registroProntuarioRepository.deleteById(id);
    }

    @Override
    public RegistroProntuarioResponseDTO buscarPorId(Long id) {
        TipoRegistroProntuario registro = registroProntuarioRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Registro do prontuário não encontrado com o ID: " + id));
        return mapEntidadeParaResponse(registro);
    }

    @Override
    public List<RegistroProntuarioResponseDTO> buscarPorProntuarioId(Long prontuarioId) {
        return registroProntuarioRepository.findByProntuarioIdOrderByDataHoraDesc(prontuarioId).stream()
                .map(this::mapEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegistroProntuarioResponseDTO> buscarPorVeterinarioId(Long veterinarioId) {
        return registroProntuarioRepository.findByVeterinarioResponsavelId(veterinarioId).stream()
                .map(this::mapEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegistroProntuarioResponseDTO> buscarPorInternacaoId(Long internacaoId) {
        return registroProntuarioRepository.findByInternacaoId(internacaoId).stream()
                .map(this::mapEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegistroProntuarioResponseDTO> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return registroProntuarioRepository.findByDataHoraBetween(inicio, fim).stream()
                .map(this::mapEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<RegistroProntuarioResponseDTO> listarTodos() {
        return registroProntuarioRepository.findAll().stream()
                .map(this::mapEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public RegistroProntuarioResponseDTO buscarPorIdComMedicamentos(Long id) {
        TipoRegistroProntuario registro = registroProntuarioRepository.findByIdWithMedicamentos(id);
        if (registro == null) {
            throw new NoSuchElementException("Registro do prontuário não encontrado com o ID: " + id);
        }

        RegistroProntuarioResponseDTO response = mapEntidadeParaResponse(registro);

        if (registro.getMedicamentosAdministrados() != null) {
            List<AdministracaoMedicamentoResponseDTO> medicamentos = registro.getMedicamentosAdministrados().stream()
                    .map(administracao -> {
                        AdministracaoMedicamentoResponseDTO dto = modelMapper.map(administracao, AdministracaoMedicamentoResponseDTO.class);
                        // CORREÇÃO PRINCIPAL AQUI: Acessar o nome através do produto associado.
                        if (administracao.getMedicamento() != null && administracao.getMedicamento().getProduto() != null) {
                            dto.setNomeMedicamento(administracao.getMedicamento().getProduto().getNome());
                        }
                        if (administracao.getFuncionarioExecutor() != null) {
                            dto.setNomeFuncionarioExecutor(administracao.getFuncionarioExecutor().getNome());
                        }
                        return dto;
                    })
                    .collect(Collectors.toList());
            response.setMedicamentosAdministrados(medicamentos);
        }

        return response;
    }

    private RegistroProntuarioResponseDTO mapEntidadeParaResponse(TipoRegistroProntuario registro) {
        RegistroProntuarioResponseDTO dto = modelMapper.map(registro, RegistroProntuarioResponseDTO.class);

        if (registro.getProntuario() != null) {
            dto.setProntuarioId(registro.getProntuario().getId());
        }

        if (registro.getAgendamento() != null) {
            dto.setAgendamentoId(registro.getAgendamento().getId());
        }

        if (registro.getVeterinarioResponsavel() != null) {
            dto.setVeterinarioResponsavelId(registro.getVeterinarioResponsavel().getId());
            dto.setNomeVeterinario(registro.getVeterinarioResponsavel().getNome());
        }

        if (registro.getInternacao() != null) {
            dto.setInternacaoId(registro.getInternacao().getId());
        }

        return dto;
    }
}
