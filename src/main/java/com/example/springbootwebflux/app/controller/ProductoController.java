package com.example.springbootwebflux.app.controller;

import com.example.springbootwebflux.app.models.documents.Categoria;
import com.example.springbootwebflux.app.models.documents.Producto;
import com.example.springbootwebflux.app.service.CategoriaService;
import com.example.springbootwebflux.app.service.ProductoService;
import jakarta.validation.Valid;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.http.codec.multipart.FilePart;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;
import org.thymeleaf.spring6.context.webflux.ReactiveDataDriverContextVariable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;

import java.io.File;
import java.net.MalformedURLException;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.time.Duration;
import java.util.Date;
import java.util.UUID;

@Controller
public class ProductoController {
    @Autowired
    private ProductoService productoService;

    private String path = "C:\\Foto\\";

    @Autowired
    private CategoriaService categoriaService;
    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

    @GetMapping("/uploads/img/{nombreFoto:.+}")
    public Mono<ResponseEntity<Resource>> verFoto(@PathVariable String nombreFoto) throws MalformedURLException {
        Path ruta = Paths.get(path).resolve(nombreFoto).toAbsolutePath();

        Resource imagen = new UrlResource(ruta.toUri());

        return Mono.just(
                ResponseEntity.ok()
                        .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + imagen.getFilename() + "\"")
                        .body(imagen)
        );

    }

    @GetMapping("/ver/{id}")
    public Mono<String> ver (Model model,@PathVariable String id ){
        return productoService.findById(id)
                .doOnNext(producto ->{
                    model.addAttribute("producto", producto);
                    model.addAttribute("titulo", "Detalles");
                }).switchIfEmpty(Mono.just(new Producto()))
                .flatMap(p ->{
                    if(p.getId() == null){
                        return Mono.error(new InterruptedException("no existe el producto"));
                    }
                    return Mono.just(p);
                }).then(Mono.just("ver"))
                .onErrorResume(ex -> Mono.just("redirect:/list?error=no+existe+el+producto"));
    }

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
    public Mono<String> guardar(@Valid Producto producto, BindingResult result, Model model,@RequestPart FilePart file) {
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
                        if(!file.filename().isEmpty()){
                            //TODO:UUID.randomUUID().toString() --> Genera un id unico
                            producto.setFoto(UUID.randomUUID().toString() + "-" + file.filename()
                                    .replace(" ","")
                                    .replace(":","")
                                    .replace("\\",""));
                        }
                        producto.setCategoria(categoria1);
                        return productoService.save(producto);
                    }).doOnNext(p -> {
                        log.info("Producto guardado: " + p.getNombre());
                    })
                    .flatMap(producto1 -> {
                        if(!file.filename().isEmpty()){
                            return file.transferTo(new File( path + producto1.getFoto()));
                        }
                        return Mono.empty();
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
