package br.com.clinicavet.clinica_api.service;

// Importe todos os seus DTOs, Entidades e Repositórios necessários
import br.com.clinicavet.clinica_api.dto.AgendamentoRequestDTO;
import br.com.clinicavet.clinica_api.dto.AgendamentoResponseDTO;
import br.com.clinicavet.clinica_api.model.*; // EAnimal, ECliente, EServico, EAgendamento
import br.com.clinicavet.clinica_api.model.enums.StatusAgendamento; // Importe seu Enum de Status
import br.com.clinicavet.clinica_api.repository.*; // Todos os repositórios
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.stream.Collectors;

@Service
public class AgendamentoService {

    // Injeção de todas as dependências necessárias
    private final AgendamentoRepository agendamentoRepository;
    private final AnimalRepository animalRepository;
    private final ServicoRepository servicoRepository;
    private final ClienteRepository clienteRepository;
    private final ModelMapper modelMapper;

    @Autowired
    public AgendamentoService(AgendamentoRepository agendamentoRepository, AnimalRepository animalRepository, ServicoRepository servicoRepository, ClienteRepository clienteRepository, ModelMapper modelMapper) {
        this.agendamentoRepository = agendamentoRepository;
        this.animalRepository = animalRepository;
        this.servicoRepository = servicoRepository;
        this.clienteRepository = clienteRepository;
        this.modelMapper = modelMapper;
    }

    @Transactional
    public AgendamentoResponseDTO criarAgendamento(AgendamentoRequestDTO requestDTO) {
        // Busca as entidades relacionadas
        ECliente cliente = clienteRepository.findById(requestDTO.getClienteId()).orElseThrow(() -> new NoSuchElementException("Cliente não encontrado"));
        EAnimal animal = animalRepository.findById(requestDTO.getAnimalId()).orElseThrow(() -> new NoSuchElementException("Animal não encontrado"));
        EServico servico = servicoRepository.findById(requestDTO.getServicoId()).orElseThrow(() -> new NoSuchElementException("Serviço não encontrado"));


        if (!animal.getCliente().getId().equals(cliente.getId())) {
            throw new IllegalArgumentException("O animal informado não pertence ao cliente especificado.");
        }

        EAgendamento novoAgendamento = new EAgendamento();
        novoAgendamento.setCliente(cliente);
        novoAgendamento.setAnimal(animal);
        novoAgendamento.setServico(servico);
        novoAgendamento.setDataHoraAgendamento(requestDTO.getDataHoraAgendamento());
        novoAgendamento.setObservacoes(requestDTO.getObservacoes());

        // Define o status inicial programmaticamente. O cliente não pode escolher isso.
        //novoAgendamento.setStatus(StatusAgendamento.AGENDADO);

        EAgendamento agendamentoSalvo = agendamentoRepository.save(novoAgendamento);

        return modelMapper.map(agendamentoSalvo, AgendamentoResponseDTO.class);
    }


    @Transactional
    public AgendamentoResponseDTO cancelarAgendamento(Long id) {
        EAgendamento agendamento = agendamentoRepository.findById(id)
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
    public AgendamentoResponseDTO buscarPorId(Long id) {
        EAgendamento agendamento = agendamentoRepository.findById(id)
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
        EAgendamento agendamento = agendamentoRepository.findById(id)
                .orElseThrow(() -> new NoSuchElementException("Agendamento não encontrado com o ID: " + id));

        if (agendamento.getStatus() != StatusAgendamento.AGENDADO) {
            throw new IllegalStateException("Apenas agendamentos com status AGENDADO podem ser confirmados.");
        }

        agendamento.setStatus(StatusAgendamento.CONFIRMADO);

        agendamentoRepository.save(agendamento);

        return modelMapper.map(agendamento, AgendamentoResponseDTO.class);

    }
}