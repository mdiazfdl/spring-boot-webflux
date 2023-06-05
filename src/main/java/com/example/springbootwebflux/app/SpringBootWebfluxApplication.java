package com.example.springbootwebflux.app;

import com.example.springbootwebflux.app.models.dao.ProductoDao;
import com.example.springbootwebflux.app.models.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import reactor.core.publisher.Flux;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebfluxApplication.class, args);
    }

    @Autowired
    private ProductoDao productoDao;

    private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);
    @Override
    public void run(String... args) {
        Flux.just(new Producto("mesa", 623.5),
                new Producto("silla", 223.5),
                new Producto("lapiz", 234.5),
                new Producto("vaso", 213.5)
        )
                .flatMap(producto -> productoDao.save(producto))
                .subscribe(producto -> log.info("Insert: " + producto.getId() + " nombre: " + producto.getNombre()));
    }
}
