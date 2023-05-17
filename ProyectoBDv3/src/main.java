
import java.io.IOException;
import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

/**
 *
 * @author Alejo
 */
public class main extends Application {

    @Override
    public void start(Stage ventana) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("/vistas/Login.fxml"));
        Scene scene = new Scene(root);
        ventana.setScene(scene);
        
        ventana.setTitle("Login");
        ventana.show();
    }
    
    public static void main(String[] args) {
        launch(args);
    }
}
    