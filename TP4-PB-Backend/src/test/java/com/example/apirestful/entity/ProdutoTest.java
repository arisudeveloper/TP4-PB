package com.example.apirestful.entity;

import org.junit.jupiter.api.Test;
import static org.junit.jupiter.api.Assertions.*;

class ProdutoTest {
    @Test
    void testGettersESetters() {
        Produto p = new Produto();
        p.setId(1);
        p.setNome("Teste");
        p.setQuantidade(10);
        p.setPreco(50.0);

        assertEquals(1, p.getId());
        assertEquals("Teste", p.getNome());
        assertEquals(10, p.getQuantidade());
        assertEquals(50.0, p.getPreco());
    }
}