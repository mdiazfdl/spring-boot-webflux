package com.example.springbootwebflux.app.models.dao;

import com.example.springbootwebflux.app.models.documents.Producto;
import org.springframework.data.mongodb.repository.ReactiveMongoRepository;

public interface ProductoDao extends ReactiveMongoRepository<Producto,String> {
}
