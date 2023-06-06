package com.example.springbootwebflux.app.controller;

import com.example.springbootwebflux.app.models.dao.ProductoDao;
import com.example.springbootwebflux.app.models.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

@RestController()
@RequestMapping("/api/productos")
public class ProductoRestController {

    @Autowired
    private ProductoDao productoDao;

    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

    @GetMapping("")
    public Flux<Producto> findAll() {
        Flux<Producto> productos = productoDao.findAll()
                .map(producto -> {
                    producto.setNombre(producto.getNombre().toUpperCase());
                    return producto;
                }).doOnNext(producto -> log.info(producto.getNombre()));
        return productos;
    }
    @GetMapping("/{id}")
    public Mono<Producto> findById(@PathVariable String id) {
        /* Esta seria la forma facil de hacerlo
        return productoDao.findById(id);*/

        Flux<Producto> productos = productoDao.findAll();
        Mono<Producto> producto = productos.filter(p -> p.getId().equals(id)).next();
        return producto;
    }

}
