package Vistas;

import java.io.IOException;
import java.net.URL;
import java.util.ResourceBundle;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.fxml.Initializable;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import Conector.Conector;
import java.sql.Connection;
import java.sql.SQLException;

/**
 * FXML Controller class
 *
 * @author Kevin
 */
public class LoginController implements Initializable {

    private Conector conector;

    @FXML
    private TextField txtURL;

    @FXML
    private TextField txtPuerto;

    @FXML
    private TextField txtUsuario;

    @FXML
    private TextField txtContraseña;

    @FXML
    private Button btnConectar;
    
    @Override
    public void initialize(URL url, ResourceBundle rb) {
        conector = new Conector();
    }


    @FXML
    private void conectar(ActionEvent event) throws IOException, SQLException {
        String url = txtURL.getText();
        String puerto = txtPuerto.getText();
        String urlCompleta = "jdbc:mysql://" + url + ":" + puerto;
        String usuario = txtUsuario.getText();
        String contraseña = txtContraseña.getText();

        Connection connection = conector.conectar(urlCompleta, usuario, contraseña);
        if (connection != null) {
            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Éxito");
            alert.setContentText("Conexión Exitosa");

            ButtonType buttonTypeAceptar = new ButtonType("Aceptar");

            alert.getButtonTypes().setAll(buttonTypeAceptar);

            alert.showAndWait().ifPresent(buttonType -> {
                if (buttonType == buttonTypeAceptar) {
                    lanzarPrincipal();
                }
            });

        } else {
            Alert alert = new Alert(Alert.AlertType.ERROR);
            alert.setTitle("Error");
            alert.setContentText("Error en la conexión");
            alert.show();
        }
    }

    @FXML
    private void lanzarPrincipal() {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Principal.fxml"));
        Parent root;
        try {
            root = loader.load();
            
            
            PrincipalController principalController = loader.getController();
            principalController.setConector(conector);
            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.show();
            stage.setTitle("Gestor de Bases de Datos");
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public void setConector(Conector conector) {
        this.conector = conector;
    }

}
