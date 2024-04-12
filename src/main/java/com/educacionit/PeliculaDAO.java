package com.educacionit;

import java.io.File;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;

public class PeliculaDAO {
    private final String url;
    private final String usuario;
    private final String contraseña;

    public PeliculaDAO(String url, String usuario, String contraseña) {
        this.url = url;
        this.usuario = usuario;
        this.contraseña = contraseña;
    }

    public Connection conectar() throws SQLException {
        return DriverManager.getConnection(url, usuario, contraseña);
    }

    public void desconectar(Connection conexion) throws SQLException {
        if (conexion != null) {
            conexion.close();
        }
    }

    public List<Pelicula> buscarPorTitulo(String titulo) {
        List<Pelicula> peliculas = new ArrayList<>();
        String query = "SELECT * FROM peliculas WHERE titulo LIKE ?";
        try (Connection conexion = conectar();
             PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, "%" + titulo + "%");
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int codigo = resultSet.getInt("codigo");
                String tituloPelicula = resultSet.getString("titulo");
                String url = resultSet.getString("url");
                String imagen = resultSet.getString("imagen");
                
                List<String> generos = obtenerGenerosDePelicula(conexion, codigo);

                peliculas.add(new Pelicula(codigo, tituloPelicula, url, new File(imagen), generos));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return peliculas;
    }


    public List<Pelicula> buscarPorGenero(String genero) {
        List<Pelicula> peliculas = new ArrayList<>();
        String query = "SELECT p.* FROM peliculas p " +
                       "INNER JOIN pelicula_genero pg ON p.codigo = pg.codigo_pelicula " +
                       "INNER JOIN generos g ON pg.id_genero = g.id " +
                       "WHERE g.nombre = ?";
        try (Connection conexion = conectar();
             PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, genero);
            ResultSet resultSet = statement.executeQuery();
            while (resultSet.next()) {
                int codigo = resultSet.getInt("codigo");
                String titulo = resultSet.getString("titulo");
                String url = resultSet.getString("url");
                String imagen = resultSet.getString("imagen");
                
                List<String> generos = obtenerGenerosDePelicula(conexion, codigo);

                peliculas.add(new Pelicula(codigo, titulo, url, new File(imagen), generos));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return peliculas;
    }


	private List<String> obtenerGenerosDePelicula(Connection conexion, int codigoPelicula) throws SQLException {
	    List<String> generos = new ArrayList<>();
	    String query = "SELECT g.nombre FROM generos g " +
	                   "INNER JOIN pelicula_genero pg ON g.id = pg.id_genero " +
	                   "WHERE pg.codigo_pelicula = ?";
	    try (PreparedStatement statement = conexion.prepareStatement(query)) {
	        statement.setInt(1, codigoPelicula);
	        ResultSet resultSet = statement.executeQuery();
	        while (resultSet.next()) {
	            String nombreGenero = resultSet.getString("nombre");
	            generos.add(nombreGenero);
	        }
	    }
	    return generos;
	}


    public void agregarPelicula(Pelicula pelicula) {
        String insertPeliculaSQL = "INSERT INTO peliculas (titulo, url, imagen) VALUES (?, ?, ?)";
        String insertPeliculaGeneroSQL = "INSERT INTO pelicula_genero (codigo_pelicula, id_genero) VALUES (?, ?)";

        try (Connection conexion = conectar();
             PreparedStatement stmtPelicula = conexion.prepareStatement(insertPeliculaSQL, PreparedStatement.RETURN_GENERATED_KEYS);
             PreparedStatement stmtPeliculaGenero = conexion.prepareStatement(insertPeliculaGeneroSQL)) {

            stmtPelicula.setString(1, pelicula.getTitulo());
            stmtPelicula.setString(2, pelicula.getUrl());
            stmtPelicula.setString(3, pelicula.getImagen().getAbsolutePath());
            int filasAfectadasPelicula = stmtPelicula.executeUpdate();

            int codigoPelicula = -1;
            try (ResultSet generatedKeys = stmtPelicula.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    codigoPelicula = generatedKeys.getInt(1);
                }
            }

            for (String genero : pelicula.getGeneros()) {
                int idGenero = obtenerIdGenero(conexion, genero);
                if (idGenero == -1) {
                    idGenero = insertarGenero(conexion, genero);
                }

                stmtPeliculaGenero.setInt(1, codigoPelicula);
                stmtPeliculaGenero.setInt(2, idGenero);
                stmtPeliculaGenero.executeUpdate();
            }

            System.out.println("La película se ha agregado correctamente.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private int obtenerIdGenero(Connection conexion, String genero) throws SQLException {
        String query = "SELECT id FROM generos WHERE nombre = ?";
        try (PreparedStatement statement = conexion.prepareStatement(query)) {
            statement.setString(1, genero);
            ResultSet resultSet = statement.executeQuery();
            if (resultSet.next()) {
                return resultSet.getInt("id");
            }
        }
        return -1;
    }

    private int insertarGenero(Connection conexion, String genero) throws SQLException {
        String insertGeneroSQL = "INSERT INTO generos (nombre) VALUES (?)";
        try (PreparedStatement stmtGenero = conexion.prepareStatement(insertGeneroSQL, PreparedStatement.RETURN_GENERATED_KEYS)) {
            stmtGenero.setString(1, genero);
            stmtGenero.executeUpdate();

            try (ResultSet generatedKeys = stmtGenero.getGeneratedKeys()) {
                if (generatedKeys.next()) {
                    return generatedKeys.getInt(1);
                }
            }
        }
        return -1;
    }

    public void borrarPelicula(int codigo) {
        String deletePeliculaGeneroSQL = "DELETE FROM pelicula_genero WHERE codigo_pelicula = ?";
        String deletePeliculaSQL = "DELETE FROM peliculas WHERE codigo = ?";
        try (Connection conexion = conectar();
             PreparedStatement stmtPeliculaGenero = conexion.prepareStatement(deletePeliculaGeneroSQL);
             PreparedStatement stmtPelicula = conexion.prepareStatement(deletePeliculaSQL)) {

            stmtPeliculaGenero.setInt(1, codigo);
            stmtPeliculaGenero.executeUpdate();

            stmtPelicula.setInt(1, codigo);
            int filasAfectadas = stmtPelicula.executeUpdate();
            if (filasAfectadas > 0) {
                System.out.println("La película con código " + codigo + " ha sido borrada correctamente.");
            } else {
                System.out.println("No se encontró ninguna película con el código " + codigo + ".");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

}
