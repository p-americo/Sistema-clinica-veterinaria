package br.ifg.meaupet.services;

import br.ifg.meaupet.dto.AnimalDTO;
import br.ifg.meaupet.dto.FuncionarioDTO;
import br.ifg.meaupet.dto.ServicoDTO;
import br.ifg.meaupet.model.EAnimal;
import br.ifg.meaupet.model.EFuncionario;
import br.ifg.meaupet.model.EServico;
import br.ifg.meaupet.model.enums.PorteAnimal;
import br.ifg.meaupet.model.enums.TipoAnimal;
import br.ifg.meaupet.model.enums.TipoServico;
import br.ifg.meaupet.repository.ServicoRepository;
import jakarta.persistence.EntityNotFoundException;
import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

@Service
public class ServicoService {
    @Autowired
    ServicoRepository repository;
    @Autowired
    private ModelMapper modelMapper;

    public Page<ServicoDTO> obterServico(Pageable paginacao) {
        return repository.findAll(paginacao).map(entidade -> modelMapper.map(entidade, ServicoDTO.class));
    }

    public  ServicoDTO obterServicoPorId(Long id) {
        EServico entidade = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        return modelMapper.map(entidade, ServicoDTO.class);
    }


    public ServicoDTO cadastrarServico(ServicoDTO dto) {

        EServico entidade = modelMapper.map(dto, EServico.class);
        entidade.setTipo(TipoServico.valueOf(dto.getTipo()));

        repository.save(entidade);
        return modelMapper.map(entidade, ServicoDTO.class);
    }

    public void deletarServio(Long id) {
        EServico entidade = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        repository.delete(entidade);
    }

    //Rever com o Ciro caso erro
    public  ServicoDTO atualizarServico(Long id,  ServicoDTO dto) {
        EServico entidade = repository.findById(id).orElseThrow(() -> new EntityNotFoundException());
        modelMapper.map(dto, entidade);
        repository.save(entidade);
        return modelMapper.map(entidade,  ServicoDTO.class);
    }
}
