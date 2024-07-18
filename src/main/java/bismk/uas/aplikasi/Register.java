package bismk.uas.aplikasi;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Hyperlink;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Register {

    @FXML
    private TextField username;

    @FXML
    private TextField password;

    @FXML
    private TextField phone;

    @FXML
    private TextField email;

    @FXML
    private TextField address;

    @FXML
    private Hyperlink masuk;

    private Connection connect() {
        String url = "jdbc:mysql://localhost:3306/uas_pbo";
        String user = "root";
        String pass = "";
        Connection conn = null;
        try {
            conn = DriverManager.getConnection(url, user, pass);
        } catch (SQLException e) {
            System.out.println("Connection failed: " + e.getMessage());
        }
        return conn;
    }

    @FXML
    private void handleRegister() {
        String sql = "INSERT INTO users (username, password, phone, email, address, role) VALUES (?, ?, ?, ?, ?, 'customer')";
        Connection conn = this.connect();

        if (conn == null) {
            System.out.println("Connection to database failed. Please check your credentials and database server.");
            return;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username.getText());
            pstmt.setString(2, password.getText());
            pstmt.setString(3, phone.getText());
            pstmt.setString(4, email.getText());
            pstmt.setString(5, address.getText());
            pstmt.executeUpdate();
            System.out.println("Registration successful!");
            showAlert(Alert.AlertType.INFORMATION, "Registration Successful", "User registered successfully as a customer.");
        } catch (SQLException e) {
            System.out.println("Registration failed: " + e.getMessage());
            showAlert(Alert.AlertType.ERROR, "Registration Failed", "An error occurred during registration. Please try again.");
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Failed to close connection: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handlemasuklink() {
        try {
            System.out.println("Loading Login FXML...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bismk/uas/aplikasi/Login.fxml"));
            if (loader.getLocation() == null) {
                System.out.println("FXML location is null. Check the path.");
                return;
            }
            Pane login = loader.load();
            System.out.println("Login FXML loaded successfully.");

            Stage stage = (Stage) masuk.getScene().getWindow();
            Scene scene = new Scene(login);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the login page: " + e.getMessage());
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }
}

