package com.example.springbootwebflux.app.service;

import com.example.springbootwebflux.app.models.dao.CategoriaDao;
import com.example.springbootwebflux.app.models.documents.Categoria;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@Service
public class CategoriaServiceimpl implements CategoriaService{
    @Autowired
    private CategoriaDao categoriaDao;

    @Override
    public Flux<Categoria> findAll() {
        return categoriaDao.findAll();
    }

    @Override
    public Mono<Categoria> findById(String id) {
        return categoriaDao.findById(id);
    }

    @Override
    public Mono<Categoria> save(Categoria categoria) {
        return categoriaDao.save(categoria);
    }

    @Override
    public Mono<Void> delete(Categoria categoria) {
        return categoriaDao.delete(categoria);
    }

    @Override
    public Mono<Void> deleteById(String id) {
        return categoriaDao.deleteById(id);
    }
}
