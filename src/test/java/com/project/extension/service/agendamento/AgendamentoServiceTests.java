// java
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

import java.time.LocalDateTime;
import java.util.*;

import static org.junit.jupiter.api.Assertions.*;
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
        agendamento.setDataAgendamento(LocalDateTime.now());
        agendamento.setObservacao("Teste Obs");
    }

    // -------------------------------------------------------------
    // TESTE SALVAR
    // -------------------------------------------------------------
    @Test
    void deveSalvarAgendamentoComSucesso() {
        when(agendamentoContext.processarAgendamento(any()))
                .thenReturn(agendamento);
        when(repository.save(any()))
                .thenReturn(agendamento);

        Agendamento salvo = service.salvar(new Agendamento());

        assertNotNull(salvo);
        assertEquals(1, salvo.getId());
        verify(logService, times(1)).success(anyString());
    }

    // -------------------------------------------------------------
    // TESTE EDITAR
    // -------------------------------------------------------------
    @Test
    void deveEditarAgendamento() {
        Agendamento origem = new Agendamento();
        origem.setTipoAgendamento(TipoAgendamento.SERVICO);
        origem.setDataAgendamento(LocalDateTime.now());

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
        // agora verifica que o tipo foi atualizado conforme a origem
        assertEquals(origem.getTipoAgendamento(), atualizado.getTipoAgendamento());
        verify(logService, times(1)).info(anyString());
    }

    // -------------------------------------------------------------
    // TESTE DELETAR
    // -------------------------------------------------------------
    @Test
    void deveDeletarAgendamento() {
        agendamento.setFuncionarios(new ArrayList<>());
        agendamento.getFuncionarios().add(new Funcionario());

        when(repository.findById(1)).thenReturn(Optional.of(agendamento));
        when(repository.save(any())).thenReturn(agendamento);

        service.deletar(1);

        assertEquals(0, agendamento.getFuncionarios().size());
        // ajustado para 1 chamada de info (comportamento atual do serviço)
        verify(logService, times(1)).info(anyString());
        verify(logService, times(1)).warning(anyString());
    }

    // -------------------------------------------------------------
    // TESTE BUSCAR POR ID (SUCESSO)
    // -------------------------------------------------------------
    @Test
    void deveBuscarAgendamentoPorId() {
        when(repository.findById(1)).thenReturn(Optional.of(agendamento));

        Agendamento encontrado = service.buscarPorId(1);

        assertNotNull(encontrado);
        assertEquals(1, encontrado.getId());
    }

    // -------------------------------------------------------------
    // TESTE BUSCAR POR ID (FALHA)
    // -------------------------------------------------------------
    @Test
    void deveLancarExcecaoQuandoNaoEncontrarAgendamento() {
        when(repository.findById(999)).thenReturn(Optional.empty());

        assertThrows(AgendamentoNaoEncontradoException.class,
                () -> service.buscarPorId(999));

        verify(logService, times(1)).error(anyString());
    }

    // -------------------------------------------------------------
    // TESTE BUSCAR TODOS
    // -------------------------------------------------------------
    @Test
    void deveBuscarTodosAgendamentos() {
        when(repository.findAll()).thenReturn(List.of(agendamento, agendamento));

        List<Agendamento> lista = service.buscarTodos();

        assertEquals(2, lista.size());
        verify(logService, times(1)).info(anyString());
    }
}
