package com.educacionit;

import java.util.List;
import java.util.Scanner;

public class Main {

	public static void main(String[] args) {
		String url = "jdbc:mysql://localhost:3306/integradorFase1";
		String usuario = "usuario";
		String contraseña = "contraseña";
		PeliculaDAO peliculaDAO = new PeliculaDAOImpl(url, usuario, contraseña);
		 InterfazUsuario interfazUsuario = new InterfazUsuario(peliculaDAO);

	        Scanner scanner = new Scanner(System.in);
	        int opcion;
	        do {
	            System.out.println("Menú:");
	            System.out.println("1. Buscar películas por título");
	            System.out.println("2. Buscar películas por género");
	            System.out.println("3. Agregar una nueva película");
	            System.out.println("4. Borrar una película");
	            System.out.println("5. Salir");
	            System.out.print("Ingrese la opción deseada: ");
	            opcion = scanner.nextInt();
	            scanner.nextLine();

	            switch (opcion) {
	                case 1:
	                    System.out.print("Ingrese el título de la película a buscar: ");
	                    String tituloBusqueda = scanner.nextLine();
	                    List<Pelicula> peliculasPorTitulo = peliculaDAO.buscarPorTitulo(tituloBusqueda);
	                    interfazUsuario.mostrarListado(peliculasPorTitulo);
	                    break;
	                case 2:
	                    System.out.print("Ingrese el género de la película a buscar: ");
	                    String generoBusqueda = scanner.nextLine();
	                    List<Pelicula> peliculasPorGenero = peliculaDAO.buscarPorGenero(generoBusqueda);
	                    interfazUsuario.mostrarListado(peliculasPorGenero);
	                    break;
	                case 3:
	                    interfazUsuario.agregarPelicula();
	                    break;
	                case 4:
	                    System.out.print("Ingrese el código de la película a borrar: ");
	                    int codigoBorrar = scanner.nextInt();
	                    peliculaDAO.borrarPelicula(codigoBorrar);
	                    break;
	                case 5:
	                    System.out.println("Saliendo del programa...");
	                    break;
	                default:
	                    System.out.println("Opción no válida. Por favor, ingrese una opción válida.");
	                    break;
	            }
	        } while (opcion != 5);
	    }
	}