package com.project.extension.service.estoque;

import static org.mockito.Mockito.*;
import static org.junit.jupiter.api.Assertions.*;

import com.project.extension.entity.Estoque;
import com.project.extension.entity.Produto;
import com.project.extension.entity.Usuario;
import com.project.extension.repository.EstoqueRepository;
import com.project.extension.service.*;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.math.BigDecimal;
import java.util.List;
import java.util.Optional;

public class EstoqueServiceTests {

    @Mock
    private EstoqueRepository repository;

    @Mock
    private ProdutoService produtoService;

    @Mock
    private UsuarioService usuarioService;

    @Mock
    private LogService logService;

    @InjectMocks
    private EstoqueService service;

    private Produto produto;
    private Estoque estoque;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        produto = new Produto();
        produto.setId(1);
        produto.setNome("Produto Teste");

        estoque = new Estoque();
        estoque.setId(1);
        estoque.setProduto(produto);
        estoque.setLocalizacao("Depósito A");
        estoque.setQuantidadeTotal(BigDecimal.valueOf(10.0));
        estoque.setQuantidadeDisponivel(BigDecimal.valueOf(10));
        estoque.setReservado(BigDecimal.valueOf(0));

        usuario = new Usuario();
        usuario.setId(99);
        usuario.setNome("Usuário Teste");

        when(usuarioService.buscarPorId(99)).thenReturn(usuario);
    }

    @Test
    void testMovimentacaoComQuantidadeZero() {
        Estoque request = new Estoque();
        request.setProduto(produto);
        request.setLocalizacao("Depósito A");
        request.setQuantidadeTotal(BigDecimal.valueOf(0));

        when(produtoService.buscarPorId(produto.getId())).thenReturn(produto);
        when(repository.findByProdutoAndLocalizacao(produto, "Depósito A")).thenReturn(Optional.of(estoque));

        assertThrows(IllegalArgumentException.class, () -> service.entrada(request));
        verify(logService).error(contains("quantidade menor ou igual a zero"));
    }

    @Test
    void testListarEstoques() {
        when(repository.findAll()).thenReturn(List.of(estoque));

        List<Estoque> resultado = service.listar();

        assertEquals(1, resultado.size());
        assertEquals(estoque, resultado.get(0));
        verify(logService).info(contains("Busca por todos os registros de estoque realizada"));
    }
}
