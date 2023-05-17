package connection;

import java.sql.Connection;
import java.sql.DatabaseMetaData;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;

/**
 *
 * @author Alejo
 */
public class connector {

    public static final String URL = //acá va la url
    public static final String USER = //el usuario
    public static final String PASSWORD = //la contraseña

    public static void main(String[] args) {
        Connection con = null;
        try {
            Connection connection = DriverManager.getConnection(URL, USER, PASSWORD);
            DatabaseMetaData metaData = connection.getMetaData();

            ResultSet resultSet = metaData.getCatalogs();
            while (resultSet.next()) {
                String databaseName = resultSet.getString("TABLE_CAT");
                System.out.println(databaseName);
            }

            resultSet.close();
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
