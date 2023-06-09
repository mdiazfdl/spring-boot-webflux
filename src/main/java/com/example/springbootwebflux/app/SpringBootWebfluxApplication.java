package com.example.springbootwebflux.app;

import com.example.springbootwebflux.app.models.dao.ProductoDao;
import com.example.springbootwebflux.app.models.documents.Categoria;
import com.example.springbootwebflux.app.models.documents.Producto;
import com.example.springbootwebflux.app.service.CategoriaService;
import com.example.springbootwebflux.app.service.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.data.mongodb.core.ReactiveMongoTemplate;
import reactor.core.publisher.Flux;

import java.util.Date;

@SpringBootApplication
public class SpringBootWebfluxApplication implements CommandLineRunner {

    public static void main(String[] args) {
        SpringApplication.run(SpringBootWebfluxApplication.class, args);
    }

    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;

    @Autowired
    private ReactiveMongoTemplate mongoTemplate;
    private static final Logger log = LoggerFactory.getLogger(SpringBootWebfluxApplication.class);

    @Override
    public void run(String... args) {
        mongoTemplate.dropCollection("productos").subscribe();
        mongoTemplate.dropCollection("categorias").subscribe();
        Categoria cat1 = new Categoria("cat1");
        Categoria cat2 = new Categoria("cat2");
        Categoria cat3 = new Categoria("cat3");
        Categoria cat4 = new Categoria("cat4");

        Flux.just(cat1, cat2, cat3, cat4)
                .flatMap(categoriaService::save)
                /*
                TODO: El operador ".thenMany()" es para incluir otro fulo una vez que haya terminado el primero
                TODO: pero que sea del tipo Flux, porque para incluir otro del tipo mono se utiliza el operador ".then()"
                */
                .thenMany(Flux.just(new Producto("mesa", 623.5, cat1),
                                new Producto("silla", 223.5, cat2),
                                new Producto("lapiz", 234.5, cat3),
                                new Producto("vaso", 213.5, cat4)
                        )
                        .flatMap(producto -> {
                            producto.setCreateAt(new Date());
                            return productoService.save(producto);
                        }))
                .subscribe(producto -> log.info("Insert: " + producto.getId() + " nombre: " + producto.getNombre()));
    }
}
