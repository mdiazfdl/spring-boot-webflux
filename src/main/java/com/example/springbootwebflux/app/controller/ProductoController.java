package com.example.springbootwebflux.app.controller;

import com.example.springbootwebflux.app.models.documents.Categoria;
import com.example.springbootwebflux.app.models.documents.Producto;
import com.example.springbootwebflux.app.service.CategoriaService;
import com.example.springbootwebflux.app.service.ProductoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.time.Duration;
import java.util.Date;

@Controller
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    @Autowired
    private CategoriaService categoriaService;
    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

    @GetMapping("/listar")
    public Mono<String> listar(Model model) {
        Flux<Producto> productos = productoService.findAllConNombreUpperCase();
        productos.subscribe(producto -> log.info(producto.getNombre()));
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "listado de productos");
        return Mono.just("listar");
    }

    @ModelAttribute("categorias")
    public Flux<Categoria> categoria() {
        return categoriaService.findAll();
    }

    @GetMapping("/form")
    public Mono<String> crear(Model model) {
        model.addAttribute("producto", new Producto());
        model.addAttribute("titulo", "formulario de productos");
        return Mono.just("form");
    }

    @GetMapping("/form/{id}")
    public Mono<String> editar(@PathVariable String id, Model model) {
        Mono<Producto> productoMono = productoService.findById(id);
        model.addAttribute("producto", productoMono);
        model.addAttribute("titulo", "editar de producto");
        return Mono.just("form");
    }

    @GetMapping("/eliminar/{id}")
    public Mono<String> Eliminar(@PathVariable String id) {
        return productoService.findById(id).flatMap(producto ->
        {
            return productoService.delete(producto);
        }).then(Mono.just("redirect:/listar?success=producto+eliminado+con+exito"));
    }

    @PostMapping("/form")
    public Mono<String> guardar(@Valid Producto producto, BindingResult result, Model model) {
        if (result.hasErrors()) {
            model.addAttribute("boton", "Guardar");
            model.addAttribute("titulo", "errores de producto");
            return Mono.just("form");
        } else {
            Mono<Categoria> categoria = categoriaService.findById(producto.getCategoria().getId());
            return categoria.flatMap(categoria1 -> {
                        if (producto.getCreateAt() == null) {
                            producto.setCreateAt(new Date());
                        }
                        producto.setCategoria(categoria1);
                        return productoService.save(producto);
                    }).doOnNext(p -> {
                        log.info("Producto guardado: " + p.getNombre());
                    })
                    .thenReturn("redirect:/listar");
        }
    }

    @GetMapping("/listar_datadriver")
    public String listarDataDrive(Model model) {
        Flux<Producto> productos = productoService.findAllConNombreUpperCase()
                .delayElements(Duration.ofSeconds(1));
        productos.subscribe(producto -> log.info(producto.getNombre()));
        model.addAttribute("productos", new ReactiveDataDriverContextVariable(productos, 2));
        model.addAttribute("titulo", "listado de productos");
        return "listar";
    }

    @GetMapping("/listar_full")
    public String listarFull(Model model) {
        Flux<Producto> productos = productoService.findAllConNombreUpperCaseRepeat();
        productos.subscribe(producto -> log.info(producto.getNombre()));
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "listado de productos");
        return "listar";
    }

    @GetMapping("/listar_chunked")
    public String listarChunked(Model model) {
        Flux<Producto> productos = productoService.findAllConNombreUpperCaseRepeat();
        productos.subscribe(producto -> log.info(producto.getNombre()));
        model.addAttribute("productos", productos);
        model.addAttribute("titulo", "listado de productos");
        return "listarChunked";
    }
}
