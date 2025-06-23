package br.com.clinicavet.clinica_api.service;


import br.com.clinicavet.clinica_api.dto.AgendamentoRequestDTO;
import br.com.clinicavet.clinica_api.dto.AgendamentoResponseDTO;
import br.com.clinicavet.clinica_api.dto.AgendamentoUpdateDTO;
import br.com.clinicavet.clinica_api.model.*; // EAnimal, ECliente, EServico, EAgendamento
import br.com.clinicavet.clinica_api.model.enums.StatusAgendamento; // Importe seu Enum de Status
import br.com.clinicavet.clinica_api.repository.*; // Todos os repositórios
import br.com.clinicavet.clinica_api.service.Interface.AgendamentoService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;


import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;


@Service
public class AgendamentoServiceImplement implements AgendamentoService {

    // @Autowired faz a injeção das dependecias na classe, caso delcare somente em cima do atributo
    // a não sem construtor o atributo não pode ser final = mutavel
    // indicar no construtor, antes se tinha mais contruttores,
    private final AgendamentoRepository agendamentoRepository;
    private final AnimalRepository animalRepository;
    private final ServicoRepository servicoRepository;
    private final ClienteRepository clienteRepository;
    private final ModelMapper modelMapper;


    public AgendamentoServiceImplement(AgendamentoRepository agendamentoRepository, AnimalRepository animalRepository, ServicoRepository servicoRepository, ClienteRepository clienteRepository, ModelMapper modelMapper) {
        this.agendamentoRepository = agendamentoRepository;
        this.animalRepository = animalRepository;
        this.servicoRepository = servicoRepository;
        this.clienteRepository = clienteRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public AgendamentoResponseDTO criarAgendamento(AgendamentoRequestDTO requestDTO) {
        // Busca as entidades relacionadas
        TipoCliente cliente = clienteRepository.findById(requestDTO.getClienteId()).orElseThrow(() -> new NoSuchElementException("Cliente não encontrado"));
        TipoAnimal animal = animalRepository.findById(requestDTO.getAnimalId()).orElseThrow(() -> new NoSuchElementException("Animal não encontrado"));
        TipoServico servico = servicoRepository.findById(requestDTO.getServicoId()).orElseThrow(() -> new NoSuchElementException("Serviço não encontrado"));


        if (!animal.getCliente().getId().equals(cliente.getId())) {
            throw new IllegalArgumentException("O animal informado não pertence ao cliente especificado.");
        }


        TipoAgendamento novoAgendamento = new TipoAgendamento();
        novoAgendamento.setCliente(cliente);
        novoAgendamento.setAnimal(animal);
        novoAgendamento.setServico(servico);
        novoAgendamento.setDataHoraAgendamento(requestDTO.getDataHoraAgendamento());
        novoAgendamento.setObservacoes(requestDTO.getObservacoes());

        // Define o status inicial programmaticamente. O cliente não pode escolher isso.
        //novoAgendamento.setStatus(StatusAgendamento.AGENDADO);

        TipoAgendamento agendamentoSalvo = agendamentoRepository.save(novoAgendamento);

        return modelMapper.map(agendamentoSalvo, AgendamentoResponseDTO.class);
    }


    @Transactional
    public AgendamentoResponseDTO cancelarAgendamento(Long id) {
        TipoAgendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Agendamento não encontrado com o ID: " + id));

        // Lógica de negócio: talvez você não possa cancelar um agendamento que já foi realizado.
        if (agendamento.getStatus() == StatusAgendamento.REALIZADO) {
            throw new IllegalStateException("Não é possível cancelar um agendamento já realizado.");
        }

        agendamento.setStatus(StatusAgendamento.CANCELADO);
        agendamentoRepository.save(agendamento);
        return modelMapper.map(agendamento, AgendamentoResponseDTO.class);
    }


    @Transactional(readOnly = true)
    public AgendamentoResponseDTO listarPorId(Long id) {
        TipoAgendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Agendamento não encontrado com o ID: " + id));
        return modelMapper.map(agendamento, AgendamentoResponseDTO.class);
    }


    @Transactional(readOnly = true)
    public List<AgendamentoResponseDTO> listarTodos() {
        return agendamentoRepository.findAll().stream()
                .map(agendamento -> modelMapper.map(agendamento, AgendamentoResponseDTO.class))
                .collect(Collectors.toList());
    }

    @Transactional
    public AgendamentoResponseDTO confirmarAgendamento(Long id) {
        TipoAgendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Agendamento não encontrado com o ID: " + id));

        if (agendamento.getStatus() != StatusAgendamento.AGENDADO) {
            throw new IllegalStateException("Apenas agendamentos com status AGENDADO podem ser confirmados.");
        }

        agendamento.setStatus(StatusAgendamento.CONFIRMADO);

        agendamentoRepository.save(agendamento);

        return modelMapper.map(agendamento, AgendamentoResponseDTO.class);
    }

    @Transactional
    public AgendamentoResponseDTO atualizarAgendamento(Long id, AgendamentoUpdateDTO updateDTO) {

        TipoAgendamento agendamentoExistente = agendamentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Agendamento não encontrado para atualização com o ID: " + id));

        if (agendamentoExistente.getStatus() == StatusAgendamento.CANCELADO ||
                agendamentoExistente.getStatus() == StatusAgendamento.REALIZADO) {
            throw new IllegalStateException("Não é possível alterar um agendamento com status " + agendamentoExistente.getStatus());
        }

        modelMapper.map(updateDTO, agendamentoExistente);

        TipoAgendamento agendamentoAtualizado = agendamentoRepository.save(agendamentoExistente);

        return modelMapper.map(agendamentoAtualizado, AgendamentoResponseDTO.class);
    }

    public AgendamentoResponseDTO realizarAgendamento(Long id) {

        TipoAgendamento realizarAgendamento = agendamentoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Agendamento não encontrado com o ID: " + id));

        realizarAgendamento.setStatus(StatusAgendamento.REALIZADO);
        agendamentoRepository.save(realizarAgendamento);
        return modelMapper.map(realizarAgendamento, AgendamentoResponseDTO.class);
    }

    public void deletarAgendamento(Long id) {

        agendamentoRepository.findById(id).orElseThrow(() -> new NoSuchElementException("Agendamento não encontrado com o ID: " + id));

        agendamentoRepository.deleteById(id);
    }
}