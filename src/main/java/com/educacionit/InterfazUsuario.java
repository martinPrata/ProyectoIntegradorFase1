package com.educacionit;

import java.io.File;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class InterfazUsuario {
    private PeliculaDAO peliculaDAO;

    public InterfazUsuario(PeliculaDAO peliculaDAO) {
        this.peliculaDAO = peliculaDAO;
    }

    public void mostrarListado(List<Pelicula> peliculas) {
        for (Pelicula pelicula : peliculas) {
            System.out.println("Código: " + pelicula.getCodigo());
            System.out.println("Título: " + pelicula.getTitulo());
            System.out.println("URL: " + pelicula.getUrl());
            System.out.println("Imagen: " + pelicula.getImagen().getPath());
            System.out.println("Géneros: " + pelicula.getGeneros());
            System.out.println();
        }
    }

    public void mostrarDetalles(Pelicula pelicula) {
        System.out.println("Título: " + pelicula.getTitulo());
        System.out.println("URL: " + pelicula.getUrl());
        System.out.println("Géneros: " + pelicula.getGeneros());
    }

    public void agregarPelicula() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Ingrese el título de la película:");
        String titulo = scanner.nextLine();
        System.out.println("Ingrese la URL de la película:");
        String url = scanner.nextLine();
        System.out.println("Ingrese la ruta del archivo de imagen de la película:");
        String rutaImagen = scanner.nextLine();
        File imagen = new File(rutaImagen);
        System.out.println("Ingrese los géneros de la película (separados por comas):");
        String generosStr = scanner.nextLine();
        List<String> generos = new ArrayList<>();
        for (String genero : generosStr.split(",")) {
            generos.add(genero.trim());
        }
        peliculaDAO.agregarPelicula(new Pelicula(0, titulo, url, imagen, generos));
        System.out.println("La película se ha agregado correctamente.");
    }
}
