package Vistas;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import Conector.Conector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;

/**
 * FXML Controller class
 *
 * Controlador de la vista Principal (vista B)
 */
public class PrincipalController implements Initializable {

    @FXML
    private TableView<String> tablaBases;

    @FXML
    private Button btnCargarBases;

    private Conector conector;

    public PrincipalController() {
        conector = new Conector();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn<String, String> column = new TableColumn<>("Bases de datos");
        column.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        tablaBases.getColumns().add(column);
    }

    @FXML
    private void cargarBasesDeDatos() {
        try {
            Connection connection = conector.getConnection();
            if (connection != null) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT schema_name FROM information_schema.schemata");
                ResultSet resultSet = preparedStatement.executeQuery();

                ObservableList<String> basesDeDatos = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    String nombre = resultSet.getString("schema_name");
                    basesDeDatos.add(nombre);
                    System.out.println(nombre);
                }
                tablaBases.setItems(basesDeDatos);
            } else {
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No se pudo establecer la conexión");
                alert.show();
            }
        } catch (SQLException e) {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText(e.getMessage());
            alert.show();
            System.out.println("El objeto conector no ha sido configurado correctamente.");
        }
    }

    public void setConector(Conector conector) {
        this.conector = conector;
    }
}
