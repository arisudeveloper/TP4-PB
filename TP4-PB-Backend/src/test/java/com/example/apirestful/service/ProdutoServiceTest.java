package com.example.apirestful.service;

import com.example.apirestful.entity.Produto;
import com.example.apirestful.repository.IProdutoRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProdutoServiceTest {

    @Mock
    private IProdutoRepository produtoRepository;

    @InjectMocks
    private ProdutoService produtoService;

    @Test
    @DisplayName("Deve retornar todos os produtos")
    void testFindAll() {
        List<Produto> lista = new ArrayList<>();
        lista.add(new Produto());
        when(produtoRepository.findAll()).thenReturn(lista);
        List<Produto> resultado = produtoService.findAll();
        assertFalse(resultado.isEmpty());
        assertEquals(1, resultado.size());
    }

    @Test
    @DisplayName("Deve retornar produto por ID com sucesso")
    void testFindByIdSucesso() {
        Produto p = new Produto();
        when(produtoRepository.findById(1)).thenReturn(Optional.of(p));
        Produto resultado = produtoService.findById(1);
        assertNotNull(resultado);
    }

    @Test
    @DisplayName("Deve retornar null quando ID não existe")
    void testFindByIdNull() {
        when(produtoRepository.findById(99)).thenReturn(Optional.empty());
        Produto resultado = produtoService.findById(99);
        assertNull(resultado);
    }

    @Test
    @DisplayName("Deve salvar um produto")
    void testSave() {
        Produto p = new Produto();
        when(produtoRepository.save(any(Produto.class))).thenReturn(p);
        Produto salvo = produtoService.save(p);
        assertNotNull(salvo);
    }

    @Test
    @DisplayName("Deve deletar um produto")
    void testDelete() {
        doNothing().when(produtoRepository).deleteById(1);
        produtoService.deleteById(1);
        verify(produtoRepository, times(1)).deleteById(1);
    }

    @Test
    @DisplayName("Deve atualizar produto com sucesso")
    void testUpdateSucesso() {
        Produto existente = new Produto();
        Produto novosDados = new Produto();
        novosDados.setNome("Produto Novo");
        novosDados.setQuantidade(5);
        novosDados.setPreco(100.0);

        when(produtoRepository.findById(1)).thenReturn(Optional.of(existente));
        when(produtoRepository.save(any(Produto.class))).thenAnswer(invocation -> invocation.getArgument(0));

        Produto atualizado = produtoService.update(1, novosDados);

        assertNotNull(atualizado);
        assertEquals("Produto Novo", atualizado.getNome()); 
    }

    @Test
    @DisplayName("Deve retornar null ao atualizar produto inexistente")
    void testUpdateInexistente() {
        when(produtoRepository.findById(1)).thenReturn(Optional.empty());
        Produto resultado = produtoService.update(1, new Produto());
        assertNull(resultado);
    }
}