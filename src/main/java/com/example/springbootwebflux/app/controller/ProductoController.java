package com.example.springbootwebflux.app.controller;

import com.example.springbootwebflux.app.models.documents.Producto;
import com.example.springbootwebflux.app.service.ProductoService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;

@Controller
public class ProductoController {
    @Autowired
    private ProductoService productoService;
    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

    @GetMapping("/listar")
    public Mono<String> listar(Model model){
        Flux<Producto> productos = productoService.findAllConNombreUpperCase();
        productos.subscribe(producto -> log.info(producto.getNombre()));
        model.addAttribute("productos",productos);
        model.addAttribute("titulo","listado de productos");
        return Mono.just("Listar");
    }
    @GetMapping("/form")
    public Mono<String> crear(Model model){
        model.addAttribute("producto",new Producto());
        model.addAttribute("titulo","formulario de productos");
        return Mono.just("Form");
    }
    @PostMapping("/form")
    public Mono<String> guardar(Producto producto){
        return productoService.save(producto).doOnNext(p -> {
            log.info("Producto guardado: " + p.getNombre() );
        })
                .thenReturn("redirect:/listar");
    }

    @GetMapping("/listar_datadriver")
    public String listarDataDrive(Model model){
        Flux<Producto> productos = productoService.findAllConNombreUpperCase()
                .delayElements(Duration.ofSeconds(1));
        productos.subscribe(producto -> log.info(producto.getNombre()));
        model.addAttribute("productos", new ReactiveDataDriverContextVariable(productos,2));
        model.addAttribute("titulo","listado de productos");
        return "Listar";
    }

    @GetMapping("/listar_full")
    public String listarFull(Model model){
        Flux<Producto> productos = productoService.findAllConNombreUpperCaseRepeat();
        productos.subscribe(producto -> log.info(producto.getNombre()));
        model.addAttribute("productos",productos);
        model.addAttribute("titulo","listado de productos");
        return "Listar";
    }
    @GetMapping("/listar_chunked")
    public String listarChunked(Model model){
        Flux<Producto> productos = productoService.findAllConNombreUpperCaseRepeat();
        productos.subscribe(producto -> log.info(producto.getNombre()));
        model.addAttribute("productos",productos);
        model.addAttribute("titulo","listado de productos");
        return "ListarChunked";
    }
}
