package com.example.apirestful.controller;

import com.example.apirestful.entity.Produto;
import com.example.apirestful.service.ProdutoService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/produtos")
@CrossOrigin(origins = "http://localhost:5173")
public class ProdutoController {
    @Autowired
    private ProdutoService produtoService;

    @GetMapping
    public ResponseEntity<List<Produto>> consultarProdutos() {

        List<Produto> produtos = produtoService.findAll();

        return ResponseEntity.ok(produtos);
    }

    @GetMapping("{id}")
    public ResponseEntity<?> consultarProduto (@PathVariable Integer id){
        Produto produto = produtoService.findById(id);
        if (produto == null){
            return new ResponseEntity<>("Erro: produto não encontrado", HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(produto, HttpStatus.OK);
    }

    @DeleteMapping("{id}")
    public ResponseEntity<?> excluirProduto(@PathVariable Integer id){
        Produto produto = produtoService.findById(id);

        if (produto == null){
            return new ResponseEntity<>("Erro: produto não encontrado", HttpStatus.NOT_FOUND);
        }

        produtoService.deleteById(id);
        return ResponseEntity.ok("Produto excluído com sucesso");
    }

    @PostMapping
    public ResponseEntity<Produto> cadastrarProduto(@RequestBody Produto p){
        Produto novo = produtoService.save(p);
        return new ResponseEntity<>(novo, HttpStatus.CREATED);
    }

    @PutMapping("{id}")
    public ResponseEntity<?> atualizarProduto(@PathVariable Integer id, @RequestBody Produto p){

        Produto atualizado = produtoService.update(id, p);

        if (atualizado == null){
            return new ResponseEntity<>("Produto não encontrado", HttpStatus.NOT_FOUND);
        }

        return ResponseEntity.ok(atualizado);
    }


}