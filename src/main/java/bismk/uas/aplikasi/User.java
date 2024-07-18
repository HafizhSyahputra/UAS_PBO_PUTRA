package bismk.uas.aplikasi;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.ButtonType;
import javafx.scene.control.TableView;
import javafx.stage.Modality;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.util.Optional;

public class User {
    @FXML
    private TableView<USERLIST> userTable;

    public void initialize() {
        populateUserTable();
    }

    private void populateUserTable() {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery("SELECT * FROM users")) {

            while (rs.next()) {
                USERLIST user = new USERLIST();
                user.setId(rs.getInt("id"));
                user.setUsername(rs.getString("username"));
                user.setPassword(rs.getString("password"));
                user.setPhone(rs.getString("phone"));
                user.setEmail(rs.getString("email"));
                user.setAddress(rs.getString("address"));
                user.setRole(rs.getString("role"));

                // Tambahkan user ke dalam TableView
                userTable.getItems().add(user);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleAddNewUser() {
        try {
            // Load the FXML file
            FXMLLoader loader = new FXMLLoader(getClass().getResource("AddUser.fxml"));
            Parent root = loader.load();

            // Create a new stage
            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL); // Prevent interaction with other windows
            stage.setTitle("Add New User");
            stage.setScene(new Scene(root));


            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open Add User window.");
        }
    }

    @FXML
    private void handleDelete() {
        USERLIST selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to delete.");
            return;
        }

        // Tampilkan dialog konfirmasi
        Alert confirmation = new Alert(Alert.AlertType.CONFIRMATION);
        confirmation.setTitle("Delete Confirmation");
        confirmation.setHeaderText(null);
        confirmation.setContentText("Are you sure you want to delete this user?");

        Optional<ButtonType> result = confirmation.showAndWait();
        if (result.isPresent() && result.get() == ButtonType.OK) {
            // Jika pengguna mengkonfirmasi penghapusan, lakukan penghapusan
            deleteUser(selectedUser);
        }
    }

    private void deleteUser(USERLIST user) {
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             Statement stmt = conn.createStatement()) {

            String deleteQuery = "DELETE FROM users WHERE id = " + user.getId();
            int affectedRows = stmt.executeUpdate(deleteQuery);

            if (affectedRows > 0) {
                userTable.getItems().remove(user);
                showAlert(Alert.AlertType.INFORMATION, "Success", "User successfully deleted.");
            } else {
                showAlert(Alert.AlertType.ERROR, "Delete Error", "Failed to delete user.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to delete user: " + e.getMessage());
        }
    }

    @FXML
    private void handleEdit() {
        USERLIST selectedUser = userTable.getSelectionModel().getSelectedItem();
        if (selectedUser == null) {
            showAlert(Alert.AlertType.WARNING, "No Selection", "Please select a user to edit.");
            return;
        }

        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("EditUser.fxml"));
            Parent root = loader.load();

            EditUser controller = loader.getController();


            controller.setUser(selectedUser);

            Stage stage = new Stage();
            stage.initModality(Modality.APPLICATION_MODAL);
            stage.setTitle("Edit User");
            stage.setScene(new Scene(root));

            stage.showAndWait();

        } catch (IOException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Error", "Failed to open Edit User window.");
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
