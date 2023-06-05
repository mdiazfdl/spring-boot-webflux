package com.example.springbootwebflux.app.models.documents;

import lombok.Data;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.Date;

@Data
@Document(collection = "productos")
public class Producto {
    @Id
    private String id;

    private String nombre;
    private Double precio;
    private Date createAt;
}
