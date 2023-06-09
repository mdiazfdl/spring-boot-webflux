package com.example.springbootwebflux.app.models.dao;

import com.example.springbootwebflux.app.models.documents.Categoria;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface CategoriaDao extends ReactiveMongoRepository<Categoria,String> {
}
