package com.example.gutendex.principal;

import com.example.gutendex.models.Datos;
import com.example.gutendex.models.DatosLibros;
import com.example.gutendex.service.ConsumoAPI;
import com.example.gutendex.service.ConvierteDatos;
import java.util.Comparator;
import java.util.DoubleSummaryStatistics;
import java.util.Optional;
import java.util.Scanner;
import java.util.stream.Collectors;

public class Principal {

    private static final String URL_BASE = "https://gutendex.com/books/";
    private ConsumoAPI consumoAPI = new ConsumoAPI();
    private ConvierteDatos convierteDatos = new ConvierteDatos();
    Scanner teclado = new Scanner(System.in);
    public void muestraElMenu(){
        var json =consumoAPI.obtenerDatos(URL_BASE);
        System.out.println(json);
        var datos = convierteDatos.obtenerDatos(json, Datos.class);
        System.out.println(datos);
        //Top 10 libros mas descargados
        datos.libros().stream()
                .sorted(Comparator.comparing(DatosLibros::numeroDeDescargas).reversed())
                .limit(10)
                .map(l->l.numeroDeDescargas()+" - "+l.titulo().toUpperCase())
                .forEach(System.out::println);
        //Busqueda de libros
        System.out.println("Ingrse el nombre de un libro a buscar : ");
        var tituloLibro = teclado.nextLine();
        json =consumoAPI.obtenerDatos(URL_BASE+"?search="+tituloLibro.replace(" ","+"));
        var datosBusqueda = convierteDatos.obtenerDatos(json, Datos.class);
        Optional<DatosLibros> libroBuscado = datosBusqueda.libros().stream()
                .filter(l->l.titulo().toUpperCase().contains(tituloLibro.toUpperCase()))
                .findFirst();
        if (libroBuscado.isPresent()){
            System.out.println("Libro en contrado : ");
            System.out.println(libroBuscado);
        }else{
            System.out.println("Libro no encontrado...");
        }

        //Trabajando con estadisticas
        DoubleSummaryStatistics est = datos.libros().stream()
                .filter(d-> d.numeroDeDescargas()>0)
                .collect(Collectors.summarizingDouble(DatosLibros::numeroDeDescargas));
        System.out.println("Cantidad media de descargas : "+est.getAverage());
        System.out.println("Cantidad maxima de descargas : "+est.getMax());
        System.out.println("Cantidad minima de descargas : "+est.getMin());

        System.out.println("Extrayendo datos interesantes");
        datos.libros().stream()
                .flatMap(dt->dt.autor().stream()
                        .map(mi->
                                {
                    int aniosVividos = mi.anio_fallece()-mi.anio_nacimiento();
                            return "Nacimiento : "+mi.anio_nacimiento()+" - Mierte :"+mi.anio_fallece()+" - Nombre : " + mi.nombre()+" - Años : "+aniosVividos;
                        }
                        ))

                .forEach(System.out::println);

    }

}
