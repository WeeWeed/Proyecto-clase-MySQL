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
import java.sql.Statement;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import javafx.beans.property.SimpleObjectProperty;
import javafx.beans.property.SimpleStringProperty;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonBar;
import javafx.scene.control.ButtonType;
import javafx.scene.control.ComboBox;
import javafx.scene.control.Dialog;
import javafx.scene.control.Label;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.TextInputDialog;
import javafx.scene.layout.GridPane;

/**
 * FXML Controller class
 *
 * Controlador de la vista Principal (vista B)
 */
public class PrincipalController implements Initializable {

    @FXML
    private TableView<String> tablaBases;
    
    @FXML
    private TableView<String> tablaBases2;

    @FXML
    private TableView<String> tablaTablas;
    
    @FXML
    private TableView<String> tablaTablas2;

    @FXML
    private TableView<Map<String, Object>> tablaRegistros;
    
        @FXML
    private TableView<Map<String, Object>> tablaRegistros2;


    @FXML
    private Button btnCargarBases;

    @FXML
    private Button btnMostrarTablas;

    @FXML
    private Label lblBase;

    @FXML
    private Button btnUsarBase;
    
    @FXML
    private Button btnUsarBase2;

    @FXML
    private Button btnNuevaBase;

    @FXML
    private Button btnEliminarBase;
    
    @FXML
    private Button btnNuevaTabla;

    private Conector conector;

    private String baseActual;
    
     private String baseActual2;

    @FXML
    private Button btnMostrarRegistros;

    public PrincipalController() {
        conector = new Conector();
    }

    @Override
   public void initialize(URL url, ResourceBundle rb) {
    TableColumn<String, String> basesColumn = new TableColumn<>("Bases de datos");
    basesColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
    basesColumn.setMinWidth(200); // Establecer tamaño mínimo de 70
    tablaBases.getColumns().add(basesColumn);

    TableColumn<String, String> tablasColumn = new TableColumn<>("Tablas");
    tablasColumn.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
    tablasColumn.setMinWidth(200); // Establecer tamaño mínimo de 70
    tablaTablas.getColumns().add(tablasColumn);
    
    TableColumn<String, String> basesColumn2 = new TableColumn<>("Bases de datos 2");
    basesColumn2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
    basesColumn2.setMinWidth(200); // Establecer tamaño mínimo de 70
    tablaBases2.getColumns().add(basesColumn2);

    TableColumn<String, String> tablasColumn2 = new TableColumn<>("Tablas 2");
    tablasColumn2.setCellValueFactory(data -> new SimpleStringProperty(data.getValue()));
    tablasColumn2.setMinWidth(200); // Establecer tamaño mínimo de 70
    tablaTablas2.getColumns().add(tablasColumn2);
}

    @FXML
    private void usarBase() {
        baseActual = tablaBases.getSelectionModel().getSelectedItem();
        baseActual2= baseActual;
        lblBase.setText(baseActual);
    }
    
        @FXML
    private void usarBase2() {
        baseActual2 = tablaBases2.getSelectionModel().getSelectedItem();
        baseActual= baseActual2;
        lblBase.setText(baseActual);
    }

    @FXML
    private void crearNuevaBase() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nueva Base de Datos");
        dialog.setHeaderText("Ingrese el nombre de la base de datos");
        dialog.setContentText("Nombre:");

        dialog.showAndWait().ifPresent(nombreBaseDatos -> {
            if (!nombreBaseDatos.trim().isEmpty()) {
                try {
                    Connection connection = conector.getConnection();
                    if (connection != null) {
                        Statement statement = connection.createStatement();
                        String sql = "CREATE DATABASE " + nombreBaseDatos;
                        statement.executeUpdate(sql);

                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Nueva Base de Datos");
                        alert.setHeaderText("La base de datos se creó exitosamente.");
                        alert.setContentText("Nombre: " + nombreBaseDatos);
                        alert.showAndWait();
                        cargarBasesDeDatos();
                    } else {
                        mostrarAlertaError("No se pudo establecer la conexión");
                    }
                } catch (SQLException e) {
                    mostrarAlertaError(e.getMessage());
                }
            } else {
                mostrarAlertaAdvertencia("Nombre de base de datos inválido", "Debe ingresar un nombre de base de datos válido.");
            }
        });
    }

    private void mostrarAlertaError(String mensaje) {
        Alert alert = new Alert(Alert.AlertType.ERROR);
        alert.setTitle("Error");
        alert.setContentText("mensaje");
        alert.showAndWait();
    }

    private void mostrarAlertaAdvertencia(String titulo, String mensaje) {
        Alert alert = new Alert(Alert.AlertType.WARNING);
        alert.setTitle("Advertencia");
        alert.setHeaderText(titulo);
        alert.setContentText(mensaje);
        alert.showAndWait();
    }

    @FXML
    private void eliminarTabla() {
        String nombreTabla = tablaTablas.getSelectionModel().getSelectedItem();
        if (nombreTabla != null) {
            try {
                Connection connection = conector.getConnection();
                if (connection != null) {
                    // Preparar la sentencia SQL para eliminar la tabla
                    String sql = "DROP TABLE " + nombreTabla;
                    PreparedStatement preparedStatement = connection.prepareStatement(sql);
                    preparedStatement.executeUpdate();

                    // Actualizar la lista de tablas después de eliminar
                    mostrarTablas();

                    // Mostrar un mensaje de éxito
                    Alert alert = new Alert(Alert.AlertType.INFORMATION);
                    alert.setTitle("Eliminación completa");
                    alert.setHeaderText("La tabla se eliminó exitosamente.");
                    alert.setContentText("Nombre: " + nombreTabla);
                    alert.showAndWait();
                } else {
                    mostrarAlertaError("No se pudo establecer la conexión");
                }
            } catch (SQLException e) {
                mostrarAlertaError(e.getMessage());
            }
        } else {
            mostrarAlertaAdvertencia("No ha seleccionado una tabla", "Seleccione una tabla para eliminar");
        }
    }

    /* Inicio crear tabla*/
    

    @FXML
    private void nuevaTabla() {
        String nombreTabla = mostrarDialogoNombreTabla();
        if (nombreTabla != null) {
            int cantidadColumnas = mostrarDialogoCantidadColumnas();
            if (cantidadColumnas > 0) {
                List<Columna> columnas = mostrarDialogoDetallesColumnas(cantidadColumnas);
                if (!columnas.isEmpty()) {
                    try {
                        Connection connection = conector.getConnection();
                        if (connection != null) {
                            // Preparar la sentencia SQL para crear la nueva tabla
                            String crear = "CREATE TABLE " + nombreTabla + " (";
                            for (int i = 0; i < columnas.size(); i++) {
                                Columna columna = columnas.get(i);
                                crear = crear + columna.getNombre() + " " + columna.getTipo();
                                if (i < columnas.size() - 1) {
                                    crear = crear + ", ";
                                }
                            }
                            crear = crear + ")";
                            System.out.println(crear);
                            
                            Statement Statement = connection.createStatement();
                            connection.createStatement().executeUpdate("USE " + baseActual);
                            Statement.executeUpdate(crear);

                            // Actualizar la lista de tablas después de crear la nueva tabla
                            mostrarTablas();

                            // Mostrar un mensaje de éxito
                            Alert alert = new Alert(Alert.AlertType.INFORMATION);
                            alert.setTitle("Creación completa");
                            alert.setHeaderText("La tabla se creó correctamente");
                            alert.setContentText("Nombre: " + nombreTabla);
                            alert.showAndWait();                            
                        } else {
                            mostrarAlertaError("No se pudo establecer la conexión");
                        }
                    } catch (SQLException e) {
                        mostrarAlertaError(e.getMessage());
                        e.printStackTrace();
                    }
                }
            }
        }
    }

    private String mostrarDialogoNombreTabla() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nueva Tabla");
        dialog.setHeaderText("Ingrese el nombre de la nueva tabla");
        dialog.setContentText("Nombre:");

        Optional<String> resultado = dialog.showAndWait();
        return resultado.orElse(null);
    }

    private int mostrarDialogoCantidadColumnas() {
        TextInputDialog dialog = new TextInputDialog();
        dialog.setTitle("Nueva Tabla");
        dialog.setHeaderText("Ingrese la cantidad de columnas");
        dialog.setContentText("Cantidad:");

        Optional<String> resultado = dialog.showAndWait();
        if (resultado.isPresent()) {
            try {
                return Integer.parseInt(resultado.get());
            } catch (NumberFormatException e) {
                // El valor ingresado no es un número válido
                mostrarAlertaError("Ingrese un valor numérico válido");
            }
        }
        return 0;
    }

    private List<Columna> mostrarDialogoDetallesColumnas(int cantidadColumnas) {
        List<Columna> columnas = new ArrayList<>();

        for (int i = 0; i < cantidadColumnas; i++) {
            Dialog<Columna> dialog = new Dialog<>();
            dialog.setTitle("Nueva Tabla");
            dialog.setHeaderText("Ingrese los detalles de la columna " + (i + 1));

            ButtonType crearButton = new ButtonType("Crear", ButtonBar.ButtonData.OK_DONE);
            dialog.getDialogPane().getButtonTypes().addAll(crearButton, ButtonType.CANCEL);

            GridPane gridPane = new GridPane();
            gridPane.setHgap(10);
            gridPane.setVgap(10);

            TextField nombreField = new TextField();
            ComboBox<String> tipoComboBox = new ComboBox<>();
            tipoComboBox.getItems().addAll("VARCHAR(30)", "INTEGER", "BOOLEAN", "FLOAT", "DOUBLE");

            gridPane.add(new Label("Nombre:"), 0, 0);
            gridPane.add(nombreField, 1, 0);
            gridPane.add(new Label("Tipo:"), 0, 1);
            gridPane.add(tipoComboBox, 1, 1);

            dialog.getDialogPane().setContent(gridPane);

            dialog.setResultConverter(dialogButton -> {
                if (dialogButton == crearButton) {
                    String nombre = nombreField.getText();
                    String tipo = tipoComboBox.getValue();

                    if (nombre.isEmpty() || tipo == null) {
                        mostrarAlertaError("Ingrese todos los detalles de la columna");
                        return null;
                    }

                    return new Columna(nombre, tipo);
                }
                return null;
            });

            Optional<Columna> resultado = dialog.showAndWait();
            resultado.ifPresent(columnas::add);
        }

        return columnas;
    }

    private static class Columna {

        private final String nombre;
        private final String tipo;

        public Columna(String nombre, String tipo) {
            this.nombre = nombre;
            this.tipo = tipo;
        }

        public String getNombre() {
            return nombre;
        }

        public String getTipo() {
            return tipo;
        }
    }
    /*Fin crear tabla*/
    
    
    @FXML
    private void mostrarRegistros() throws SQLException {
        String nombreTabla = tablaTablas.getSelectionModel().getSelectedItem();
        if (nombreTabla != null) {
            Connection connection = conector.getConnection();
            if (connection != null) {
                // Obtener los registros de la tabla
                List<Map<String, Object>> registros = obtenerRegistros(baseActual, nombreTabla);

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
                mostrarAlertaError("No se pudo establecer la conexión");
            }
        } else {
            // No se ha seleccionado ninguna tabla
            mostrarAlertaAdvertencia("No se ha seleccionado una tabla", "Seleccione una tabla primero");
        }
    }
    
    @FXML
    private void mostrarEstructura2() throws SQLException {
        String nombreTabla = tablaTablas2.getSelectionModel().getSelectedItem();
        if (nombreTabla != null) {
            Connection connection = conector.getConnection();
            if (connection != null) {
                // Obtener los registros de la tabla
                List<Map<String, Object>> registros = obtenerRegistros(baseActual2, nombreTabla);

                // Crear una lista observable para los registros
                ObservableList<Map<String, Object>> registrosObservable = FXCollections.observableArrayList(registros);

                // Limpiar las columnas existentes en la tabla
                tablaRegistros2.getColumns().clear();

                // Crear columnas dinámicamente basadas en los nombres de columna
                for (String columnName : registros.get(0).keySet()) {
                    TableColumn<Map<String, Object>, Object> column = new TableColumn<>(columnName);
                    column.setCellValueFactory(data -> new SimpleObjectProperty<>(data.getValue().get(columnName)));
                    tablaRegistros2.getColumns().add(column);
                }

                // Establecer los registros en la tabla
                tablaRegistros2.setItems(registrosObservable);
            } else {
                // Manejo del error de conexión
                mostrarAlertaError("No se pudo establecer la conexión");
            }
        } else {
            // No se ha seleccionado ninguna tabla
            mostrarAlertaAdvertencia("No se ha seleccionado una tabla", "Seleccione una tabla primero");
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
    private void mostrarTablas2() {
        if (baseActual2 != null) {
            try {
                Connection connection = conector.getConnection();
                if (connection != null) {
                    PreparedStatement preparedStatement = connection.prepareStatement("SHOW TABLES IN " + baseActual2);
                    ResultSet resultSet = preparedStatement.executeQuery();

                    ObservableList<String> tablas2 = FXCollections.observableArrayList();
                    while (resultSet.next()) {
                        String nombreTabla = resultSet.getString(1);
                        tablas2.add(nombreTabla);
                        System.out.println(nombreTabla);
                    }

                    tablaTablas2.setItems(tablas2);
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
    
        @FXML
    private void cargarBasesDeDatos2() {
        try {
            Connection connection = conector.getConnection();
            if (connection != null) {
                PreparedStatement preparedStatement = connection.prepareStatement("SELECT schema_name FROM information_schema.schemata");
                ResultSet resultSet = preparedStatement.executeQuery();

                ObservableList<String> basesDeDatos2 = FXCollections.observableArrayList();
                while (resultSet.next()) {
                    String nombre = resultSet.getString("schema_name");
                    basesDeDatos2.add(nombre);
                    System.out.println(nombre);
                }
                tablaBases2.setItems(basesDeDatos2);
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

    @FXML
    private void eliminarBaseDatos() {
        String baseDatosSeleccionada = tablaBases.getSelectionModel().getSelectedItem();
        if (baseDatosSeleccionada != null) {
            Alert confirmacion = new Alert(Alert.AlertType.CONFIRMATION);
            confirmacion.setTitle("Confirmar eliminación");
            confirmacion.setHeaderText("Eliminar base de datos");
            confirmacion.setContentText("¿Estás seguro de que quieres eliminar la base de datos '" + baseDatosSeleccionada + "'?");
            Optional<ButtonType> resultado = confirmacion.showAndWait();

            if (resultado.isPresent() && resultado.get() == ButtonType.OK) {
                try {
                    Connection connection = conector.getConnection();
                    if (connection != null) {
                        Statement statement = connection.createStatement();
                        String sql = "DROP DATABASE " + baseDatosSeleccionada;
                        statement.executeUpdate(sql);

                        // Eliminar la base de datos de la lista
                        tablaBases.getItems().remove(baseDatosSeleccionada);

                        // Mostrar una alerta de éxito
                        Alert alert = new Alert(Alert.AlertType.INFORMATION);
                        alert.setTitle("Base de Datos Eliminada");
                        alert.setHeaderText("La base de datos se eliminó exitosamente.");
                        alert.setContentText("Nombre: " + baseDatosSeleccionada);
                        alert.showAndWait();
                        if (lblBase.getText() == baseDatosSeleccionada) {
                            lblBase.setText("Ninguna");
                        }
                    } else {
                        mostrarAlertaError("No se pudo establecer la conexión");
                    }
                } catch (SQLException e) {
                    mostrarAlertaError(e.getMessage());
                }
            }
        } else {
            mostrarAlertaAdvertencia("Selección de Base de Datos", "Seleccione una base de datos para eliminar.");
        }
    }

    public void setConector(Conector conector) {
        this.conector = conector;
    }
}
