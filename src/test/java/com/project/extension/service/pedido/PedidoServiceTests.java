package com.project.extension.service.pedido;

import com.project.extension.entity.*;
import com.project.extension.exception.naoencontrado.PedidoNaoEncontradoException;
import com.project.extension.repository.PedidoRepository;
import com.project.extension.service.*;
import com.project.extension.strategy.pedido.PedidoContext;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.*;
import org.mockito.junit.jupiter.MockitoExtension;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class PedidoServiceTests {

    @Mock
    private PedidoRepository repository;
    @Mock
    private EtapaService etapaService;
    @Mock
    private StatusService statusService;
    @Mock
    private ClienteService clienteService;
    @Mock
    private PedidoContext pedidoContext;
    @Mock
    private LogService logService;

    @InjectMocks
    private com.project.extension.service.PedidoService service;

    private Pedido pedido;
    private Pedido pedidoSalvo;

    @BeforeEach
    void setUp() {
        pedido = new Pedido();
        pedido.setTipoPedido("VENDAS");
        pedido.setValorTotal(BigDecimal.valueOf(100));

        // cliente sem id para forçar criação
        Cliente cliente = new Cliente();
        cliente.setNome("Cliente Teste");
        pedido.setCliente(cliente);

        // status presente para teste de criação automática
        Status status = new Status();
        status.setTipo("PEDIDO");
        status.setNome("PENDENTE");
        pedido.setStatus(status);

        // servico + etapa para teste de criação automática
        Servico servico = new Servico();
        Etapa etapa = new Etapa();
        etapa.setTipo("PEDIDO");
        etapa.setNome("ANALISE");
        servico.setEtapa(etapa);
        pedido.setServico(servico);

        // pedido salvo retornado pelo repository
        pedidoSalvo = new Pedido();
        pedidoSalvo.setId(123);
        pedidoSalvo.setTipoPedido(pedido.getTipoPedido());
        pedidoSalvo.setValorTotal(pedido.getValorTotal());
        pedidoSalvo.setCliente(new Cliente());
        pedidoSalvo.getCliente().setId(1);
    }

    @Test
    void deveCadastrar_PedidoComCriacoesAutomaticaDeStatusEtapaECliente() {
        // mocks: status/etapa ausentes no serviço -> criar
        Status statusCriado = new Status();
        statusCriado.setTipo("PEDIDO");
        statusCriado.setNome("PENDENTE");
        when(statusService.buscarPorTipoAndStatus("PEDIDO", "PENDENTE")).thenReturn(null);
        when(statusService.cadastrar(any(Status.class))).thenReturn(statusCriado);

        Etapa etapaCriada = new Etapa();
        etapaCriada.setTipo("PEDIDO");
        etapaCriada.setNome("ANALISE");
        when(etapaService.buscarPorTipoAndEtapa("PEDIDO", "ANALISE")).thenReturn(null);
        when(etapaService.cadastrar(any(Etapa.class))).thenReturn(etapaCriada);

        Cliente clienteCriado = new Cliente();
        clienteCriado.setId(50);
        clienteCriado.setNome("Cliente Teste");
        when(clienteService.cadastrar(any(Cliente.class))).thenReturn(clienteCriado);

        when(pedidoContext.criar(any(Pedido.class))).thenAnswer(inv -> inv.getArgument(0));
        when(repository.save(any(Pedido.class))).thenReturn(pedidoSalvo);

        Pedido resultado = service.cadastrar(pedido);

        assertNotNull(resultado);
        assertEquals(123, resultado.getId());
        verify(statusService).cadastrar(any());
        verify(etapaService).cadastrar(any());
        verify(clienteService).cadastrar(any());
        verify(logService).success(anyString());
    }

    @Test
    void deveBuscarPorId_Sucesso() {
        when(repository.findById(123)).thenReturn(Optional.of(pedidoSalvo));

        Pedido encontrado = service.buscarPorId(123);

        assertNotNull(encontrado);
        assertEquals(123, encontrado.getId());
    }

    @Test
    void deveBuscarPorId_LancarExcecaoQuandoNaoEncontrar() {
        when(repository.findById(999)).thenReturn(Optional.empty());

        assertThrows(PedidoNaoEncontradoException.class, () -> service.buscarPorId(999));
        verify(logService).error(anyString());
    }

    @Test
    void deveListar_TodosPedidos() {
        when(repository.findAll()).thenReturn(List.of(pedidoSalvo));

        List<Pedido> lista = service.listar();

        assertEquals(1, lista.size());
        assertEquals(pedidoSalvo, lista.get(0));
        verify(logService).info(anyString());
    }

    @Test
    void deveEditar_PedidoExistente() {
        Pedido atualizado = new Pedido();
        atualizado.setValorTotal(BigDecimal.valueOf(200));

        when(repository.findById(123)).thenReturn(Optional.of(pedidoSalvo));
        when(pedidoContext.editar(any(Pedido.class), any(Pedido.class))).thenReturn(atualizado);
        when(repository.save(any(Pedido.class))).thenReturn(atualizado);

        Pedido resultado = service.editar(123, atualizado);

        assertNotNull(resultado);
        assertEquals(BigDecimal.valueOf(200), resultado.getValorTotal());
        verify(logService).info(anyString());
    }

    @Test
    void deveListarPedidosPorTipoENomeDaEtapa() {
        Etapa etapa = new Etapa();
        etapa.setNome("ANALISE");
        when(etapaService.buscarPorTipoAndEtapa("PEDIDO", "ANALISE")).thenReturn(etapa);
        when(repository.findAllByServico_Etapa(etapa)).thenReturn(List.of(pedidoSalvo));

        List<Pedido> resultado = service.listarPedidosPorTipoENomeDaEtapa("ANALISE");

        assertEquals(1, resultado.size());
        assertEquals(pedidoSalvo, resultado.get(0));
    }

    @Test
    void deveListarPedidosPorTipo() {
        when(repository.findByTipoPedidoIgnoreCase("VENDAS")).thenReturn(List.of(pedidoSalvo));

        List<Pedido> resultado = service.listarPedidosPorTipo("VENDAS");

        assertEquals(1, resultado.size());
        assertEquals(pedidoSalvo, resultado.get(0));
    }
}