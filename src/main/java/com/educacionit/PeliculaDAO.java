package com.educacionit;

import java.util.List;

public interface PeliculaDAO {
    List<Pelicula> buscarPorTitulo(String titulo);

    List<Pelicula> buscarPorGenero(String genero);

    void agregarPelicula(Pelicula pelicula);

    void borrarPelicula(int codigo);
}
