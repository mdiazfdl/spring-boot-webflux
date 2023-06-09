package com.example.springbootwebflux.app.service;

import com.example.springbootwebflux.app.models.documents.Categoria;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

public interface CategoriaService {

    public Flux<Categoria> findAll();

    public Mono<Categoria> findById(String id);

    public Mono<Categoria> save(Categoria categoria);

    public Mono<Void> delete(Categoria categoria);

    public Mono<Void> deleteById(String id);

}
