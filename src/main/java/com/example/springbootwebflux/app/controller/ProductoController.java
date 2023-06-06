package com.example.springbootwebflux.app.controller;

import com.example.springbootwebflux.app.models.dao.ProductoDao;
import com.example.springbootwebflux.app.models.documents.Producto;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

@Controller
public class ProductoController {
    @Autowired
    private ProductoDao productoDao;

    private static final Logger log = LoggerFactory.getLogger(ProductoController.class);

    @GetMapping("/listar")
    public String listar(Model model){
        Flux<Producto> productos = productoDao.findAll()
                .map(producto -> {
                    producto.setNombre(producto.getNombre().toUpperCase());
                    return producto;
                });
        productos.subscribe(producto -> log.info(producto.getNombre()));
        model.addAttribute("productos",productos);
        model.addAttribute("titulo","listado de productos");
        return "Listar";
    }
}
