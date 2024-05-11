package com.example.gutendex.service;

public interface IConvierteDatos {

    <T> T obtenerDatos(String json, Class<T> Clase);

}
