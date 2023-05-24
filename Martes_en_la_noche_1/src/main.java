
import Conector.Conector;
import Vistas.LoginController;
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Kevin
 */
public class main extends Application {

    @Override
    public void start(Stage ventana) throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("/Vistas/Login.fxml"));
        Parent root = loader.load();
        
        Scene scene = new Scene(root);
        ventana.setScene(scene);
        
        ventana.setTitle("Login");
        ventana.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
