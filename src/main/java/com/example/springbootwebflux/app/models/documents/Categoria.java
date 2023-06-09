package com.example.springbootwebflux.app.models.documents;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "categorias")
public class Categoria {
    @Id
    private String id;

    private String nombre;

    public Categoria(String nombre) {
        this.nombre = nombre;
    }
}
