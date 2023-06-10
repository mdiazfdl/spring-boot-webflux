package com.example.springbootwebflux.app.models.documents;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotEmpty;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.Date;

@Data
@NoArgsConstructor
@Document(collection = "productos")
public class Producto {
    @Id
    private String id;

    @NotEmpty
    private String nombre;
    @NotNull
    private Double precio;

    @Valid
    private Categoria categoria;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private Date createAt;

    public Producto(String nombre, Double precio) {
        this.nombre = nombre;
        this.precio = precio;
    }
    public Producto(String nombre, Double precio, Categoria categoria) {
        this.nombre = nombre;
        this.precio = precio;
        this.categoria = categoria;
    }
}
