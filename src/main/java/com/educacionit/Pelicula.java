package com.educacionit;

import java.io.File;
import java.util.List;

public class Pelicula {
	private int codigo;
	private String titulo;
	private String url;
	private File imagen;
	private List<String> generos;
	
	public Pelicula(int codigo, String titulo, String url, File imagen, List<String> generos) {
		this.codigo = codigo;
		this.titulo = titulo;
		this.url = url;
		this.imagen = imagen;
		this.generos = generos;
	}

	public int getCodigo() {
		return codigo;
	}

	public void setCodigo(int codigo) {
		this.codigo = codigo;
	}

	public String getTitulo() {
		return titulo;
	}

	public void setTitulo(String titulo) {
		this.titulo = titulo;
	}

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

	public File getImagen() {
		return imagen;
	}

	public void setImagen(File imagen) {
		this.imagen = imagen;
	}

	public List<String> getGeneros() {
		return generos;
	}

	public void setGeneros(List<String> generos) {
		this.generos = generos;
	}
	
	
	
}

