package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.AdministracaoMedicamentoRequestDTO;
import br.com.clinicavet.clinica_api.dto.AdministracaoMedicamentoResponseDTO;
import br.com.clinicavet.clinica_api.dto.AdministracaoMedicamentoUpdateDTO;
import br.com.clinicavet.clinica_api.model.TipoAdministracaoMedicamento;
import br.com.clinicavet.clinica_api.model.TipoFuncionario;
import br.com.clinicavet.clinica_api.model.TipoMedicamento;
import br.com.clinicavet.clinica_api.model.TipoRegistroProntuario;
import br.com.clinicavet.clinica_api.repository.AdministracaoMedicamentoRepository;
import br.com.clinicavet.clinica_api.repository.FuncionarioRepository;
import br.com.clinicavet.clinica_api.repository.RegistroProntuarioRepository;
import br.com.clinicavet.clinica_api.service.Interface.AdminstracaoMedicamentoService;
import jakarta.transaction.Transactional;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AdministracaoMedicamentoServiceImplement implements AdminstracaoMedicamentoService {
    
    private final AdministracaoMedicamentoRepository administracaoMedicamentoRepository;
    private final RegistroProntuarioRepository registroProntuarioRepository;
    private final FuncionarioRepository funcionarioRepository;
    private final ModelMapper modelMapper;
    
    public AdministracaoMedicamentoServiceImplement(AdministracaoMedicamentoRepository administracaoMedicamentoRepository,
                                                  RegistroProntuarioRepository registroProntuarioRepository,
                                                  FuncionarioRepository funcionarioRepository,
                                                  ModelMapper modelMapper) {
        this.administracaoMedicamentoRepository = administracaoMedicamentoRepository;
        this.registroProntuarioRepository = registroProntuarioRepository;
        this.funcionarioRepository = funcionarioRepository;
        this.modelMapper = modelMapper;
        this.modelMapper.getConfiguration().setSkipNullEnabled(true);
    }

    @Override
    @Transactional
    public AdministracaoMedicamentoResponseDTO criarAdministracao(AdministracaoMedicamentoRequestDTO administracaoRequestDTO) {
        // Verificar se a entrada do prontuário existe
        TipoRegistroProntuario entradaProntuario = registroProntuarioRepository.findById(administracaoRequestDTO.getEntradaProntuarioId())
                .orElseThrow(() -> new NoSuchElementException("Entrada do prontuário não encontrada com o ID: " + administracaoRequestDTO.getEntradaProntuarioId()));
        
        // Verificar se o funcionário executor existe
        TipoFuncionario funcionarioExecutor = funcionarioRepository.findById(administracaoRequestDTO.getFuncionarioExecutorId())
                .orElseThrow(() -> new NoSuchElementException("Funcionário executor não encontrado com o ID: " + administracaoRequestDTO.getFuncionarioExecutorId()));
        
        // Nota: TipoMedicamento será implementado por outra equipe, então por enquanto vamos assumir que existe
        // Aqui seria necessário buscar o medicamento por ID quando a implementação estiver pronta
        
        TipoAdministracaoMedicamento novaAdministracao = modelMapper.map(administracaoRequestDTO, TipoAdministracaoMedicamento.class);
        novaAdministracao.setId(null);
        novaAdministracao.setEntradaProntuario(entradaProntuario);
        novaAdministracao.setFuncionarioExecutor(funcionarioExecutor);
        
        // Por enquanto, vamos criar um medicamento dummy ou deixar null até a implementação da outra equipe
        // novaAdministracao.setMedicamento(medicamento);
        
        TipoAdministracaoMedicamento administracaoSalva = administracaoMedicamentoRepository.save(novaAdministracao);
        
        return mapEntidadeParaResponse(administracaoSalva);
    }

    @Override
    @Transactional
    public AdministracaoMedicamentoResponseDTO atualizarAdministracao(Long id, AdministracaoMedicamentoUpdateDTO administracaoUpdateDTO) {
        TipoAdministracaoMedicamento administracaoExistente = administracaoMedicamentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Administração de medicamento não encontrada com o ID: " + id));
        
        modelMapper.map(administracaoUpdateDTO, administracaoExistente);
        
        // Atualizar funcionário executor se fornecido
        if (administracaoUpdateDTO.getFuncionarioExecutorId() != null) {
            TipoFuncionario funcionarioExecutor = funcionarioRepository.findById(administracaoUpdateDTO.getFuncionarioExecutorId())
                    .orElseThrow(() -> new NoSuchElementException("Funcionário executor não encontrado com o ID: " + administracaoUpdateDTO.getFuncionarioExecutorId()));
            administracaoExistente.setFuncionarioExecutor(funcionarioExecutor);
        }
        
        // Atualizar medicamento se fornecido (aguardando implementação da outra equipe)
        // if (administracaoUpdateDTO.getMedicamentoId() != null) {
        //     TipoMedicamento medicamento = medicamentoRepository.findById(administracaoUpdateDTO.getMedicamentoId())
        //             .orElseThrow(() -> new NoSuchElementException("Medicamento não encontrado com o ID: " + administracaoUpdateDTO.getMedicamentoId()));
        //     administracaoExistente.setMedicamento(medicamento);
        // }
        
        TipoAdministracaoMedicamento administracaoAtualizada = administracaoMedicamentoRepository.save(administracaoExistente);
        return mapEntidadeParaResponse(administracaoAtualizada);
    }

    @Override
    @Transactional
    public void deletarAdministracao(Long id) {
        if (!administracaoMedicamentoRepository.existsById(id)) {
            throw new NoSuchElementException("Administração de medicamento não encontrada com o ID: " + id);
        }
        administracaoMedicamentoRepository.deleteById(id);
    }

    @Override
    public AdministracaoMedicamentoResponseDTO buscarPorId(Long id) {
        TipoAdministracaoMedicamento administracao = administracaoMedicamentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Administração de medicamento não encontrada com o ID: " + id));
        return mapEntidadeParaResponse(administracao);
    }

    @Override
    public List<AdministracaoMedicamentoResponseDTO> buscarPorEntradaProntuarioId(Long entradaProntuarioId) {
        return administracaoMedicamentoRepository.findByEntradaProntuarioId(entradaProntuarioId).stream()
                .map(this::mapEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdministracaoMedicamentoResponseDTO> buscarPorMedicamentoId(Long medicamentoId) {
        return administracaoMedicamentoRepository.findByMedicamentoId(medicamentoId).stream()
                .map(this::mapEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdministracaoMedicamentoResponseDTO> buscarPorFuncionarioId(Long funcionarioId) {
        return administracaoMedicamentoRepository.findByFuncionarioExecutorId(funcionarioId).stream()
                .map(this::mapEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdministracaoMedicamentoResponseDTO> buscarPorPeriodo(LocalDateTime inicio, LocalDateTime fim) {
        return administracaoMedicamentoRepository.findByDataHoraBetween(inicio, fim).stream()
                .map(this::mapEntidadeParaResponse)
                .collect(Collectors.toList());
    }

    @Override
    public List<AdministracaoMedicamentoResponseDTO> listarTodos() {
        return administracaoMedicamentoRepository.findAll().stream()
                .map(this::mapEntidadeParaResponse)
                .collect(Collectors.toList());
    }
    
    private AdministracaoMedicamentoResponseDTO mapEntidadeParaResponse(TipoAdministracaoMedicamento administracao) {
        AdministracaoMedicamentoResponseDTO dto = modelMapper.map(administracao, AdministracaoMedicamentoResponseDTO.class);
        
        if (administracao.getMedicamento() != null) {
            dto.setMedicamentoId(administracao.getMedicamento().getId());
            dto.setNomeMedicamento(administracao.getMedicamento().getNome());
        }
        
        if (administracao.getEntradaProntuario() != null) {
            dto.setEntradaProntuarioId(administracao.getEntradaProntuario().getId());
        }
        
        if (administracao.getFuncionarioExecutor() != null) {
            dto.setFuncionarioExecutorId(administracao.getFuncionarioExecutor().getId());
            dto.setNomeFuncionarioExecutor(administracao.getFuncionarioExecutor().getNome());
        }
        
        return dto;
    }
}