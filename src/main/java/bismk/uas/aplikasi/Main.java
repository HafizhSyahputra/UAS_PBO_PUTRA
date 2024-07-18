package bismk.uas.aplikasi;

import javafx.application.Application;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.stage.Stage;

import java.io.IOException;

public class Main extends Application {

    @Override
    public void start(Stage primaryStage) throws IOException {
        Parent root = FXMLLoader.load(getClass().getResource("Login.fxml"));

        primaryStage.setTitle("Login Application");

        primaryStage.setScene(new Scene(root, 600, 447));

        primaryStage.show();
    }

    public static void main(String[] args) {
        launch(args);
    }

    public static void handleRegisterLink() throws IOException {
        Stage stage = new Stage();
        Parent root = FXMLLoader.load(Main.class.getResource("register.fxml"));
        stage.setScene(new Scene(root, 600, 400));
        stage.setTitle("Register");
        stage.show();
    }
}
