package com.example.apirestful.controller;

import com.example.apirestful.entity.Produto;
import com.example.apirestful.service.ProdutoService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class ProdutoControllerTest {

    @Mock
    private ProdutoService produtoService;

    @InjectMocks
    private ProdutoController produtoController;

    @Test
    @DisplayName("Deve consultar todos os produtos")
    void testConsultarProdutos() {
        List<Produto> lista = new ArrayList<>();
        lista.add(new Produto());
        when(produtoService.findAll()).thenReturn(lista);

        ResponseEntity<List<Produto>> response = produtoController.consultarProdutos();

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals(1, response.getBody().size());
    }

    @Test
    @DisplayName("Deve retornar 404 quando produto não existe no GET")
    void testConsultarProdutoNotFound() {
        when(produtoService.findById(1)).thenReturn(null);

        ResponseEntity<?> response = produtoController.consultarProduto(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Erro: produto não encontrado", response.getBody());
    }

    @Test
    @DisplayName("Deve cadastrar produto com sucesso")
    void testCadastrarProduto() {
        Produto p = new Produto();
        when(produtoService.save(any(Produto.class))).thenReturn(p);

        ResponseEntity<Produto> response = produtoController.cadastrarProduto(p);

        assertEquals(HttpStatus.CREATED, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve excluir com sucesso")
    void testExcluirSucesso() {
        Produto p = new Produto();
        when(produtoService.findById(1)).thenReturn(p);

        ResponseEntity<?> response = produtoController.excluirProduto(1);

        assertEquals(HttpStatus.OK, response.getStatusCode());
        assertEquals("Produto excluído com sucesso", response.getBody());
    }

    @Test
    @DisplayName("Deve retornar 404 ao tentar excluir inexistente")
    void testExcluirNotFound() {
        when(produtoService.findById(1)).thenReturn(null);

        ResponseEntity<?> response = produtoController.excluirProduto(1);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve atualizar com sucesso")
    void testAtualizarSucesso() {
        Produto p = new Produto();
        when(produtoService.update(eq(1), any(Produto.class))).thenReturn(p);

        ResponseEntity<?> response = produtoController.atualizarProduto(1, p);

        assertEquals(HttpStatus.OK, response.getStatusCode());
    }

    @Test
    @DisplayName("Deve retornar 404 ao atualizar")
    void testAtualizarNotFound() {
        when(produtoService.update(eq(1), any(Produto.class))).thenReturn(null);

        ResponseEntity<?> response = produtoController.atualizarProduto(1, new Produto());

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        assertEquals("Produto não encontrado", response.getBody());
    }
}