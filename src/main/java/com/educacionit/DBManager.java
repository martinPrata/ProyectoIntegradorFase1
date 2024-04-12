package com.educacionit;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class DBManager {
    private final String url;
    private final String usuario;
    private final String contraseña;

    public DBManager(String url, String usuario, String contraseña) {
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
}
