package bismk.uas.aplikasi;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;
import javafx.scene.control.TextField;
import javafx.scene.layout.AnchorPane;
import javafx.stage.Stage;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class Customer {

    @FXML
    private TextField id;

    @FXML
    private TextField name;

    @FXML
    private TextField address;

    @FXML
    private TextField telp;

    @FXML
    private TextField email;

    @FXML
    private Button save;

    @FXML
    private Button order;

    @FXML
    private Button cancel;

    @FXML
    private Button newData;

    private String username;

    @FXML
    private AnchorPane customerFormPane;


    // Method to set the username
    public void setUsername(String username) {
        this.username = username;
        // Optionally, you can set this username to a UI element if needed
        System.out.println("Username set to: " + username);
    }


    @FXML
    private void handleCancel() {
        clearFields();
    }

    @FXML
    private void handleNewData() {
        clearFields();
    }

    private void clearFields() {
        id.clear();
        name.clear();
        address.clear();
        telp.clear();
        email.clear();
    }


    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveCustomerData(String id, String name, String address, String telp, String email) {
        String sql = "INSERT INTO customer (customer_id, name, email, phone, address) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, id);
            pstmt.setString(2, name);
            pstmt.setString(3, email);
            pstmt.setString(4, telp);  // Correct position for phone
            pstmt.setString(5, address);  // Correct position for address
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();  // Print stack trace for debugging
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save customer data: " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        String customerId = id.getText();
        String customerName = name.getText();
        String customerAddress = address.getText();
        String customerTelp = telp.getText();
        String customerEmail = email.getText();

        if (customerId.isEmpty() || customerName.isEmpty() || customerAddress.isEmpty() || customerTelp.isEmpty() || customerEmail.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter all the details");
            return;
        }

        saveCustomerData(customerId, customerName, customerAddress, customerTelp, customerEmail);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Customer data saved successfully");
    }


}
