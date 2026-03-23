package com.example.apirestful.service;

import com.example.apirestful.entity.Produto;
import com.example.apirestful.repository.IProdutoRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class ProdutoService {
    @Autowired
    private IProdutoRepository produtoRepository;

    public List<Produto> findAll() {
        return (List<Produto>) produtoRepository.findAll();
    }

    public Produto findById(Integer id){
        Optional<Produto> produto = produtoRepository.findById(id);
        if (produto.isPresent()){
            return produto.get();
        }
        return null;
    }

    public Produto save(Produto p){
        return produtoRepository.save(p);
    }

    public void deleteById(Integer id){
        produtoRepository.deleteById(id);
    }

    public Produto update(Integer id, Produto p){
        Produto existente = findById(id);

        if (existente == null){
            return null;
        }

        existente.setNome(p.getNome());
        existente.setQuantidade(p.getQuantidade());
        existente.setPreco(p.getPreco());

        return produtoRepository.save(existente);
    }
}