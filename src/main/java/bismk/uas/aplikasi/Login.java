package bismk.uas.aplikasi;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.*;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;
import javafx.stage.Stage;
import javafx.beans.binding.Bindings;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Login {

    @FXML
    private TextField username;

    @FXML
    private PasswordField password;

    @FXML
    private TextField passwordText;

    @FXML
    private CheckBox showPassword;

    @FXML
    private Button login;

    @FXML
    private BorderPane ap;

    @FXML
    private Button cancel;

    @FXML
    private Label userLabel;

    @FXML
    private Hyperlink daftar;

    @FXML
    private Pane rootPane;

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
    private void initialize() {
        // Bind the width of the elements to the width of the root pane
        userLabel.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.5));
        username.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.4));
        password.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.4));
        passwordText.prefWidthProperty().bind(rootPane.widthProperty().multiply(0.4));
        login.layoutXProperty().bind(Bindings.subtract(rootPane.widthProperty(), login.widthProperty()).divide(2).subtract(42));
        cancel.layoutXProperty().bind(Bindings.subtract(rootPane.widthProperty(), cancel.widthProperty()).divide(2).add(42));
    }

    @FXML
    private void loadCustomerDashboard(String username) {
        try {
            System.out.println("Loading customer dashboard FXML...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bismk/uas/aplikasi/DashboardCustomer.fxml"));
            BorderPane customerPane = loader.load();

            // Get the controller and set the username
            DashboardCustomer dashboardCustomer = loader.getController();
            dashboardCustomer.setUsername(username);

            // Set the scene to the customerPane
            Stage stage = (Stage) login.getScene().getWindow();
            Scene scene = new Scene(customerPane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the customer dashboard: " + e.getMessage());
        }
    }




    @FXML
    private void loadSupplierDashboard(String username) {
        try {
            System.out.println("Loading Supplier dashboard FXML...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bismk/uas/aplikasi/DashboardSupplier.fxml"));
            BorderPane SupplierPane = loader.load();

            // Get the controller and set the username
            DashboardSupplier DashboardSupplier = loader.getController();
            DashboardSupplier.setUsername(username);

            // Set the scene to the customerPane
            Stage stage = (Stage) login.getScene().getWindow();
            Scene scene = new Scene(SupplierPane);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the customer dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void loadAdminDashboard(String username) {
        try {
            System.out.println("Loading admin dashboard FXML...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bismk/uas/aplikasi/Dashboard.fxml"));
            BorderPane adminDashboard = loader.load();
            System.out.println("Admin dashboard FXML loaded successfully.");

            Dashboard adminDashboardController = loader.getController();
            adminDashboardController.setUsername(username);

            Stage stage = (Stage) login.getScene().getWindow();
            Scene scene = new Scene(adminDashboard);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the admin dashboard: " + e.getMessage());
        }
    }

    @FXML
    private void handleLogin() {
        String sql = "SELECT * FROM users WHERE username = ? AND password = ?";
        Connection conn = this.connect();

        if (conn == null) {
            System.out.println("Connection to database failed. Please check your credentials and database server.");
            return;
        }

        try (PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, username.getText());
            pstmt.setString(2, password.getText());
            ResultSet rs = pstmt.executeQuery();

            if (rs.next()) {
                // Retrieve user role
                String role = rs.getString("role");
                String username = rs.getString("username");

                // Handle successful login
                System.out.println("Login successful!");
                showAlert(Alert.AlertType.INFORMATION, "Login Successful", "Welcome, " + username + "!");

                // Load the appropriate dashboard based on the role
                if ("customer".equalsIgnoreCase(role)) {
                    loadCustomerDashboard(username);
                } else if ("admin".equalsIgnoreCase(role)) {
                    loadAdminDashboard(username);
                } else if ("supplier".equalsIgnoreCase(role)) {
                    loadSupplierDashboard(username);
                } else {
                    showAlert(Alert.AlertType.ERROR, "Login Failed", "Unknown user role.");
                }
            } else {
                showAlert(Alert.AlertType.ERROR, "Login Failed", "Invalid username or password.");
            }
        } catch (SQLException e) {
            System.out.println("Query execution failed: " + e.getMessage());
        } finally {
            try {
                conn.close();
            } catch (SQLException e) {
                System.out.println("Failed to close connection: " + e.getMessage());
            }
        }
    }

    @FXML
    private void handleCancel() {
        // Clear the text fields
        username.clear();
        password.clear();
        passwordText.clear();
    }

    @FXML
    private void handleShowPassword() {
        if (showPassword.isSelected()) {
            passwordText.setText(password.getText());
            passwordText.setVisible(true);
            password.setVisible(false);
        } else {
            password.setText(passwordText.getText());
            password.setVisible(true);
            passwordText.setVisible(false);
        }
    }

    @FXML
    private void handleRegisterLink() {
        try {
            System.out.println("Loading register FXML...");
            FXMLLoader loader = new FXMLLoader(getClass().getResource("/bismk/uas/aplikasi/Register.fxml"));
            Pane register = loader.load();
            System.out.println("Register FXML loaded successfully.");

            Stage stage = (Stage) daftar.getScene().getWindow();
            Scene scene = new Scene(register);
            stage.setScene(scene);
            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
            System.out.println("Failed to load the register page: " + e.getMessage());
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
