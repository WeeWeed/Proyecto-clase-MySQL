package Conector;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

/**
 *
 * @author Kevin
 */
public class Conector {
    private Connection connection;
       
    public Connection conectar(String url, String usuario, String contraseña) {
        try {
            connection = DriverManager.getConnection(url, usuario, contraseña);
            return connection;
        } catch (SQLException e) {
            return null;
        }
    }

    public Connection getConnection() {
        return connection;
    }
}
