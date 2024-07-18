package bismk.uas.aplikasi;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.Label;
import javafx.scene.control.TextField;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;
import javafx.scene.text.Font;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Master {
    @FXML
    private Pane profilePane;

    @FXML
    private TextField nameLabel;

    @FXML
    private TextField emailLabel;

    @FXML
    private TextField phoneLabel;

    @FXML
    private TextField addressLabel;

    @FXML
    private Button editButton;

    private final String DB_URL = "jdbc:mysql://localhost:3306/uas_pbo";
    private final String DB_USER = "root";
    private final String DB_PASSWORD = "";

    public void initialize() {
        retrieveAdminData();

        nameLabel.setEditable(false);
        emailLabel.setEditable(false);
        phoneLabel.setEditable(false);
        addressLabel.setEditable(false);
    }


    private void retrieveAdminData() {
        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "SELECT * FROM users WHERE role = 'admin'";
            PreparedStatement statement = conn.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            if (resultSet.next()) {
                nameLabel.setText(resultSet.getString("username"));
                emailLabel.setText(resultSet.getString("email"));
                phoneLabel.setText(resultSet.getString("phone"));
                addressLabel.setText(resultSet.getString("address"));
            } else {

                profilePane.setVisible(false);
            }

            resultSet.close();
            statement.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleEditProfile() {
        nameLabel.setEditable(true);
        emailLabel.setEditable(true);
        phoneLabel.setEditable(true);
        addressLabel.setEditable(true);

        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Success");
        alert.setHeaderText(null);
        alert.setContentText("Silahkan merubah data");
        alert.showAndWait();

        editButton.setText("Save Changes");
        editButton.setOnAction(event -> saveChanges());
    }

    private void saveChanges() {
        String newName = nameLabel.getText();
        String newEmail = emailLabel.getText();
        String newPhone = phoneLabel.getText();
        String newAddress = addressLabel.getText();

        try (Connection conn = DriverManager.getConnection(DB_URL, DB_USER, DB_PASSWORD)) {
            String query = "UPDATE users SET username=?, email=?, phone=?, address=? WHERE role='admin'";
            PreparedStatement statement = conn.prepareStatement(query);
            statement.setString(1, newName);
            statement.setString(2, newEmail);
            statement.setString(3, newPhone);
            statement.setString(4, newAddress);
            statement.executeUpdate();

            editButton.setText("Edit Profile");
            nameLabel.setEditable(false);
            emailLabel.setEditable(false);
            phoneLabel.setEditable(false);
            addressLabel.setEditable(false);

            Alert alert = new Alert(Alert.AlertType.INFORMATION);
            alert.setTitle("Success");
            alert.setHeaderText(null);
            alert.setContentText("Data berhasil diubah!");
            alert.showAndWait();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
