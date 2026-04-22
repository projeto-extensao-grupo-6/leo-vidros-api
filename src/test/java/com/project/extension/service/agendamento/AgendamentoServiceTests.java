package com.project.extension.service.agendamento;

import com.project.extension.entity.*;
import com.project.extension.exception.naoencontrado.AgendamentoNaoEncontradoException;
import com.project.extension.repository.AgendamentoRepository;
import com.project.extension.service.*;
import com.project.extension.strategy.agendamento.AgendamentoContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AgendamentoServiceTests {

    @Mock
    private AgendamentoRepository repository;
    @Mock
    private EnderecoService enderecoService;
    @Mock
    private FuncionarioService funcionarioService;
    @Mock
    private StatusService statusService;
    @Mock
    private AgendamentoContext agendamentoContext;
    @Mock
    private ServicoService servicoService;
    @Mock
    private EtapaService etapaService;
    @Mock
    private LogService logService;

    @InjectMocks
    private AgendamentoService service;

    private Agendamento agendamento;

    @BeforeEach
    void setUp() {
        // Não inicializar o contexto Spring aqui; apenas preparar o objeto de teste
        agendamento = new Agendamento();
        agendamento.setId(1);
        agendamento.setTipoAgendamento(TipoAgendamento.ORCAMENTO);
        agendamento.setDataAgendamento(LocalDate.now());
        agendamento.setObservacao("Teste Obs");
    }

    @Test
    void deveSalvarAgendamentoComSucesso() {
        Funcionario funcionario = new Funcionario();
        funcionario.setId(1);

        Agendamento entrada = new Agendamento();
        entrada.setTipoAgendamento(TipoAgendamento.ORCAMENTO);
        entrada.setDataAgendamento(LocalDate.now());
        entrada.setFuncionarios(new ArrayList<>(List.of(funcionario)));
        // sem serviço para evitar chamada ao servicoService

        when(agendamentoContext.processarAgendamento(any())).thenReturn(agendamento);
        when(repository.save(any())).thenReturn(agendamento);

        Agendamento salvo = service.salvar(entrada);

        assertNotNull(salvo);
        assertEquals(1, salvo.getId());
        verify(logService, times(1)).success(anyString());
    }

    @Test
    void deveEditarAgendamento() {
        Agendamento origem = new Agendamento();
        origem.setTipoAgendamento(TipoAgendamento.SERVICO);
        origem.setDataAgendamento(LocalDate.now());

        // Mock: buscar por id
        when(repository.findById(1)).thenReturn(Optional.of(agendamento));

        // Mock updates
        Status statusMock = new Status();
        statusMock.setId(10);
        statusMock.setNome("Em andamento");

        origem.setStatusAgendamento(statusMock);

        when(statusService.buscarOuCriarPorTipoENome(any(), any()))
                .thenReturn(statusMock);

        when(repository.save(any()))
                .thenReturn(agendamento);

        Agendamento atualizado = service.editar(origem, 1);

        assertNotNull(atualizado);

        assertEquals(origem.getTipoAgendamento(), atualizado.getTipoAgendamento());
        verify(logService, times(2)).info(anyString());
    }

    @Test
    void deveDeletarAgendamento() {
        agendamento.setFuncionarios(new ArrayList<>());
        agendamento.getFuncionarios().add(new Funcionario());

        when(repository.findById(1)).thenReturn(Optional.of(agendamento));

        service.deletar(1);

        assertEquals(0, agendamento.getFuncionarios().size());
        logService.info(anyString());
        logService.warning(anyString());
    }

    @Test
    void deveBuscarAgendamentoPorId() {
        when(repository.findById(1)).thenReturn(Optional.of(agendamento));

        Agendamento encontrado = service.buscarPorId(1);

        assertNotNull(encontrado);
        assertEquals(1, encontrado.getId());
    }

    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAgendamento() {
        when(repository.findById(999)).thenReturn(Optional.empty());

        assertThrows(AgendamentoNaoEncontradoException.class,
                () -> service.buscarPorId(999));

        verify(logService, times(1)).error(anyString());
    }

    @Test
    void deveBuscarTodosAgendamentos() {
        Page<Agendamento> page = new PageImpl<>(List.of(agendamento, agendamento));
        when(repository.findAll(any(Pageable.class))).thenReturn(page);

        Page<Agendamento> lista = service.buscarTodos(Pageable.unpaged());

        assertEquals(2, lista.getContent().size());
        verify(logService, times(1)).info(anyString());
    }
}
