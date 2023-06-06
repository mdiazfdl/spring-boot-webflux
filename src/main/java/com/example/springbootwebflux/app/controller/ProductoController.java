package com.example.springbootwebflux.app.controller;

import com.example.springbootwebflux.app.models.dao.ProductoDao;
import com.example.springbootwebflux.app.models.documents.Producto;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import reactor.core.publisher.Flux;

@Controller
public class ProductoController {
    @Autowired
    private ProductoDao productoDao;

    @GetMapping("/listar")
    public String listar(Model model){
        Flux<Producto> productos = productoDao.findAll();
        model.addAttribute("productos",productos);
        model.addAttribute("titulo","listado de productos");
        return "Listar";
    }
}
