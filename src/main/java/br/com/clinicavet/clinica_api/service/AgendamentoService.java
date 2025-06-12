package br.ifg.meaupet.services;

import br.ifg.meaupet.dto.AgendamentoDTO;
import br.ifg.meaupet.model.EAgendamento;
import br.ifg.meaupet.model.enums.TipoServico;
import br.ifg.meaupet.repository.AgendamentoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class AgendamentoService {
    @Autowired
    AgendamentoRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    public Page<AgendamentoDTO> obterAnimal(Pageable paginacao) {
        return repository.findAll(paginacao).map(entidade -> modelMapper.map(entidade, AgendamentoDTO.class));
    }

    public  AgendamentoDTO obterAnimalPorId(Long id) {
        EAgendamento entidade = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return modelMapper.map(entidade,  AgendamentoDTO.class);
    }

    public AgendamentoDTO cadastrarAnimal(AgendamentoDTO dto) {
        EAgendamento entidade = new EAgendamento();
        entidade.setIdAnimal(dto.getIdAnimal());
        entidade.setIdServico(dto.getIdServico());
        entidade.setIdCliente(dto.getIdCliente());
        entidade.setDataHoraAgendamento(dto.getDataHoraAgendamento());
        entidade.setObservacoes(dto.getObservacao());
        entidade.setTipoServico(TipoServico.valueOf(dto.getTipoServico()));

        repository.save(entidade);

        return modelMapper.map(entidade, AgendamentoDTO.class);
    }

    public void deletarAnimal(Long id) {
        EAgendamento entidade = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        repository.delete(entidade);
    }

    //Rever com o Ciro caso erro
    public  AgendamentoDTO atualizarAnimal(Long id,  AgendamentoDTO dto) {
        EAgendamento entidade = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        modelMapper.map(dto, entidade);
        repository.save(entidade);
        return modelMapper.map(entidade,  AgendamentoDTO.class);
    }
}
