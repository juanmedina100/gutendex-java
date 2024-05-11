package com.example.gutendex.models;

import com.fasterxml.jackson.annotation.JsonAlias;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import com.fasterxml.jackson.databind.annotation.JsonAppend;

import java.util.List;
@JsonIgnoreProperties(ignoreUnknown = true)
public record Datos(

        @JsonAlias("results") List<DatosLibros> libros
) {
}
