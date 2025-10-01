package com.project.extension.controller.estoque;

import com.project.extension.entity.Estoque;
import com.project.extension.service.EstoqueService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/estoque")
public class EstoqueControllerImpl implements EstoqueControllerDoc {
    private final EstoqueService estoqueService;

    public EstoqueControllerImpl(EstoqueService estoqueService) {
        this.estoqueService = estoqueService;
    }

    // GET /estoque
    @Override
    @GetMapping
    public List<Estoque> listarProdutosEstoque() {
        return estoqueService.listarProdutosEstoque();
    }

    // GET /estoque/{id}
    @Override
    @GetMapping("/{id}")
    public ResponseEntity<Estoque> buscarProdutoEstoquePorId(@PathVariable Long id) {
        return estoqueService.buscarProdutoEstoquePorId(id)
                .map(ResponseEntity::ok)
                .orElse(ResponseEntity.notFound().build());
    }

    // POST /estoque
    @Override
    @PostMapping
    public Estoque salvarProdutosEstoque(@RequestBody Estoque estoque) {
        return estoqueService.salvarProdutosEstoque(estoque);
    }

    // PUT /estoque/{id}
    @Override
    @PutMapping("/{id}")
    public ResponseEntity<Estoque> atualizarProdutosEstoque(@PathVariable Long id, @RequestBody Estoque estoque) {
        try {
            return ResponseEntity.ok(estoqueService.atualizarProdutosEstoque(id, estoque));
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // status code: 404
        }
    }

    // DELETE /estoque/{id}
    @Override
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deletarProdutoEstoque(@PathVariable Long id) {
        try {
            estoqueService.deletarProdutoEstoque(id);
            return ResponseEntity.noContent().build(); // status code: 204
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build(); // status code: 404
        }
    }
}