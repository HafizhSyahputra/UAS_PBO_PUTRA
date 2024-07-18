package bismk.uas.aplikasi;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class EditUser {

    @FXML
    private TextField usernameTextField;

    @FXML
    private TextField passwordTextField;

    @FXML
    private TextField phoneTextField;

    @FXML
    private TextField emailTextField;

    @FXML
    private TextField addressTextField;

    @FXML
    private Button saveButton;

    @FXML
    private Button cancelButton;

    private TableView<USERLIST> userTable;

    private USERLIST user;


    @FXML
    private void initialize() {
        userTable = new TableView<>();
    }

    public void setUserTable(TableView<USERLIST> userTable) {
        this.userTable = userTable;
    }

    public void setUser(USERLIST user) {
        this.user = user;

        usernameTextField.setText(user.getUsername());
        passwordTextField.setText(user.getPassword());
        phoneTextField.setText(user.getPhone());
        emailTextField.setText(user.getEmail());
        addressTextField.setText(user.getAddress());
    }

    @FXML
    private void handleCancel() {
        // Tutup jendela tanpa menyimpan perubahan
        Stage stage = (Stage) cancelButton.getScene().getWindow();
        stage.close();
    }

    @FXML
    private void handleSave() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement(
                     "UPDATE users SET username=?, password=?, phone=?, email=?, address=? WHERE id=?")) {

            preparedStatement.setString(1, usernameTextField.getText());
            preparedStatement.setString(2, passwordTextField.getText());
            preparedStatement.setString(3, phoneTextField.getText());
            preparedStatement.setString(4, emailTextField.getText());
            preparedStatement.setString(5, addressTextField.getText());
            preparedStatement.setInt(6, user.getId());

            int rowsUpdated = preparedStatement.executeUpdate();

            if (rowsUpdated > 0) {
                userTable.getItems().remove(user);

                USERLIST updatedUser = new USERLIST(
                        user.getId(),
                        usernameTextField.getText(),
                        passwordTextField.getText(),
                        phoneTextField.getText(),
                        emailTextField.getText(),
                        addressTextField.getText(),
                        user.getRole()
                );

                userTable.getItems().add(updatedUser);

                showAlert(Alert.AlertType.INFORMATION, "Success", "User data updated successfully.");

                Stage stage = (Stage) saveButton.getScene().getWindow();
                stage.close();
            } else {
                showAlert(Alert.AlertType.ERROR, "Error", "Failed to update user data.");
            }

        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to update user data: " + e.getMessage());
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
