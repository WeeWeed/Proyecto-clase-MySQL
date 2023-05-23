package Vistas;

import java.net.URL;
import java.sql.Connection;
import java.util.ResourceBundle;
import javafx.fxml.Initializable;
import Conector.Conector;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.ResultSetMetaData;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TableCell;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.util.Callback;

/**
 * FXML Controller class
 *
 * Controlador de la vista Principal (vista B)
 */
public class PrincipalController implements Initializable {

    @FXML
    private TableView<String> tablaBases;

    @FXML
    private TableView<String> tablaTablas;

    @FXML
    private TableView<Map<String, Object>> tablaRegistros;

    @FXML
    private Button btnCargarBases;

    @FXML
    private Button btnMostrarTablas;
    
    @FXML
    private Label lblBase;
    
    @FXML
    private Button btnUsarBase;

    private Conector conector;
    
    private String baseActual;
    
    @FXML
    private Button btnMostrarRegistros;

    public PrincipalController() {
        conector = new Conector();
    }

    @Override
    public void initialize(URL url, ResourceBundle rb) {
        TableColumn<String, String> basesColumn = new TableColumn<>("Bases de datos");
        basesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        tablaBases.getColumns().add(basesColumn);
        TableColumn<String, String> tablasColumn = new TableColumn<>("Tablas");
        tablasColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
        tablaTablas.getColumns().add(tablasColumn);
    }

    @FXML 
    private void usarBase(){
        baseActual = tablaBases.getSelectionModel().getSelectedItem();
        lblBase.setText(baseActual);
    }

    @FXML
    private void mostrarRegistros() throws SQLException {
        String nombreTabla = tablaTablas.getSelectionModel().getSelectedItem();        
        if (nombreTabla != null) {
            Connection connection = conector.getConnection(); // Manejo del error de SQL
            if (connection != null) {
                // Obtener los registros de la tabla
                List<Map<String, Object>> registros = obtenerRegistros(baseActual, nombreTabla);
                // Aquí reemplaza "nombre_base_datos" con el nombre real de la base de datos

                // Crear una lista observable para los registros
                ObservableList<Map<String, Object>> registrosObservable = FXCollections.observableArrayList(registros);

                // Limpiar las columnas existentes en la tabla
                tablaRegistros.getColumns().clear();

                // Crear columnas dinámicamente basadas en los nombres de columna
                for (String columnName : registros.get(0).keySet()) {
                    TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);
                    column.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().get(columnName)));
                    tablaRegistros.getColumns().add(column);
                }

                // Establecer los registros en la tabla
                tablaRegistros.setItems(registrosObservable);
            } else {
                // Manejo del error de conexión
                Alert alert = new Alert(Alert.AlertType.ERROR);
                alert.setTitle("Error");
                alert.setContentText("No se pudo establecer la conexión");
                alert.show();
            }
        } else {
            // No se ha seleccionado ninguna tabla
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setContentText("Seleccione una tabla primero");
            alert.show();
        }
    }

    private List<Map<String, Object>> obtenerRegistros(String nombreBaseDatos, String nombreTabla) throws SQLException {
        List<Map<String, Object>> registros = new ArrayList<>();
        Connection connection = conector.getConnection();
        if (connection != null) {
            String query = "SELECT * FROM " + nombreBaseDatos + "." + nombreTabla;
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            ResultSet resultSet = preparedStatement.executeQuery();

            ResultSetMetaData metaData = resultSet.getMetaData();
            int columnCount = metaData.getColumnCount();

            while (resultSet.next()) {
                Map<String, Object> registro = new HashMap<>();
                for (int i = 1; i <= columnCount; i++) {
                    String columnName = metaData.getColumnName(i);
                    Object columnValue = resultSet.getObject(i);
                    registro.put(columnName, columnValue);
                }
                registros.add(registro);
            }
        } else {
            throw new SQLException("No se pudo establecer la conexión");
        }
        return registros;
    }

    @FXML
    private void mostrarTablas() {      
        if (baseActual != null) {
            try {
                Connection connection = conector.getConnection();
                if (connection != null) {
                    PreparedStatement preparedStatement = connection.prepareStatement("SHOW TABLES IN " + baseActual);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    ObservableList<String> tablas = FXCollections.observableArrayList();
                    while (resultSet.next()) {
                        String nombreTabla = resultSet.getString(1);
                        tablas.add(nombreTabla);
                        System.out.println(nombreTabla);
                    }

                    tablaTablas.setItems(tablas);
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
        } else {
            Alert alert = new Alert(Alert.AlertType.WARNING);
            alert.setTitle("Advertencia");
            alert.setContentText("Seleccione una base de datos primero");
            alert.show();
        }
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
