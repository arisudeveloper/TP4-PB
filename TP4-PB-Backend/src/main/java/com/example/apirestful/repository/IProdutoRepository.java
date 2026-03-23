package com.example.apirestful.repository;

import com.example.apirestful.entity.Produto;
import org.springframework.data.repository.CrudRepository;

public interface IProdutoRepository extends CrudRepository<Produto, Integer> {
}
