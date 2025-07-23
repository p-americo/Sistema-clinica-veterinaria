package br.com.clinicavet.clinica_api.service;

import br.com.clinicavet.clinica_api.dto.ProntuarioRequestDTO;
import br.com.clinicavet.clinica_api.dto.ProntuarioResponseDTO;
import br.com.clinicavet.clinica_api.dto.ProntuarioUpdateDTO;
import br.com.clinicavet.clinica_api.model.TipoAnimal;
import br.com.clinicavet.clinica_api.model.TipoProntuario;
import br.com.clinicavet.clinica_api.repository.AnimalRepository;
import br.com.clinicavet.clinica_api.repository.ProntuarioRepository;
import br.com.clinicavet.clinica_api.service.Interface.MedicamentoService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.modelmapper.ModelMapper;

import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProntuarioServiceTest {

    @Mock
    private MedicamentoService medicamentoService;
    @Mock
    private ProntuarioRepository prontuarioRepository;
    @Mock
    private AnimalRepository animalRepository;
    @Mock
    private ModelMapper modelMapper;
    
 // Inject mocks for the service being tested in constructor
    @InjectMocks
    private ProntuarioService prontuarioService;

    @Test
    void criarProntuario_DeveRetornarResponseDTO_QuandoDadosValidos() {
        // 1. Preparar os dados de entrada
        ProntuarioRequestDTO request = new ProntuarioRequestDTO();
        request.setAnimalId(1L);

        TipoAnimal animal = new TipoAnimal();
        animal.setId(1L);

        TipoProntuario prontuario = new TipoProntuario();
        prontuario.setAnimal(animal);

        // 2. Definir o comportamento dos mocks
        when(animalRepository.findById(1L)).thenReturn(Optional.of(animal));
        when(prontuarioRepository.existsByAnimalId(1L)).thenReturn(false);
        when(modelMapper.map(request, TipoProntuario.class)).thenReturn(prontuario);
        when(prontuarioRepository.save(any(TipoProntuario.class))).thenReturn(prontuario);
        when(modelMapper.map(prontuario, ProntuarioResponseDTO.class)).thenReturn(new ProntuarioResponseDTO());

        // 3. Executar o método a ser testado
        ProntuarioResponseDTO response = prontuarioService.criarProntuario(request);

        // 4. Verificar o resultado
        assertNotNull(response);
        verify(prontuarioRepository).save(any(TipoProntuario.class));
    }

    @Test
    void atualizarProntuario_DeveRetornarResponseDTO_QuandoProntuarioExiste() {
        ProntuarioUpdateDTO updateDTO = new ProntuarioUpdateDTO();
        TipoProntuario prontuarioExistente = new TipoProntuario();
        prontuarioExistente.setId(1L);
        when(prontuarioRepository.findById(1L)).thenReturn(Optional.of(prontuarioExistente));
        when(prontuarioRepository.save(any(TipoProntuario.class))).thenReturn(prontuarioExistente);
        when(modelMapper.map(prontuarioExistente, ProntuarioResponseDTO.class)).thenReturn(new ProntuarioResponseDTO());
        ProntuarioResponseDTO response = prontuarioService.atualizarProntuario(1L, updateDTO);
        assertNotNull(response);
        verify(prontuarioRepository).save(any(TipoProntuario.class));
    }

    @Test
    void atualizarProntuario_DeveLancarExcecao_QuandoProntuarioNaoExiste() {
        ProntuarioUpdateDTO updateDTO = new ProntuarioUpdateDTO();
        when(prontuarioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> prontuarioService.atualizarProntuario(1L, updateDTO));
    }

    @Test
    void deletarProntuario_DeveDeletar_QuandoExiste() {
        when(prontuarioRepository.existsById(1L)).thenReturn(true);
        prontuarioService.deletarProntuario(1L);
        verify(prontuarioRepository).deleteById(1L);
    }

    @Test
    void deletarProntuario_DeveLancarExcecao_QuandoNaoExiste() {
        when(prontuarioRepository.existsById(1L)).thenReturn(false);
        assertThrows(NoSuchElementException.class, () -> prontuarioService.deletarProntuario(1L));
    }

    @Test
    void buscarPorId_DeveRetornarResponseDTO_QuandoExiste() {
        TipoProntuario prontuario = new TipoProntuario();
        prontuario.setId(1L);
        when(prontuarioRepository.findById(1L)).thenReturn(Optional.of(prontuario));
        when(modelMapper.map(prontuario, ProntuarioResponseDTO.class)).thenReturn(new ProntuarioResponseDTO());
        ProntuarioResponseDTO response = prontuarioService.buscarPorId(1L);
        assertNotNull(response);
    }

    @Test
    void buscarPorId_DeveLancarExcecao_QuandoNaoExiste() {
        when(prontuarioRepository.findById(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> prontuarioService.buscarPorId(1L));
    }

    @Test
    void buscarPorAnimalId_DeveRetornarResponseDTO_QuandoExiste() {
        TipoProntuario prontuario = new TipoProntuario();
        prontuario.setId(1L);
        when(prontuarioRepository.findByAnimalId(1L)).thenReturn(Optional.of(prontuario));
        when(modelMapper.map(prontuario, ProntuarioResponseDTO.class)).thenReturn(new ProntuarioResponseDTO());
        ProntuarioResponseDTO response = prontuarioService.buscarPorAnimalId(1L);
        assertNotNull(response);
    }

    @Test
    void buscarPorAnimalId_DeveLancarExcecao_QuandoNaoExiste() {
        when(prontuarioRepository.findByAnimalId(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> prontuarioService.buscarPorAnimalId(1L));
    }

    @Test
    void listarTodos_DeveRetornarListaDeDTOs() {
        TipoProntuario prontuario1 = new TipoProntuario();
        TipoProntuario prontuario2 = new TipoProntuario();
        List<TipoProntuario> lista = List.of(prontuario1, prontuario2);
        when(prontuarioRepository.findAll()).thenReturn(lista);
        when(modelMapper.map(any(TipoProntuario.class), eq(ProntuarioResponseDTO.class))).thenReturn(new ProntuarioResponseDTO());
        List<ProntuarioResponseDTO> result = prontuarioService.listarTodos();
        assertEquals(2, result.size());
    }

    @Test
    void buscarPorIdComRegistros_DeveRetornarResponseDTO_QuandoExiste() {
        TipoProntuario prontuario = new TipoProntuario();
        prontuario.setId(1L);
        prontuario.setRegistros(List.of());
        when(prontuarioRepository.findByIdWithRegistros(1L)).thenReturn(Optional.of(prontuario));
        when(modelMapper.map(prontuario, ProntuarioResponseDTO.class)).thenReturn(new ProntuarioResponseDTO());
        ProntuarioResponseDTO response = prontuarioService.buscarPorIdComRegistros(1L);
        assertNotNull(response);
    }

    @Test
    void buscarPorIdComRegistros_DeveLancarExcecao_QuandoNaoExiste() {
        when(prontuarioRepository.findByIdWithRegistros(1L)).thenReturn(Optional.empty());
        assertThrows(NoSuchElementException.class, () -> prontuarioService.buscarPorIdComRegistros(1L));
    }
}