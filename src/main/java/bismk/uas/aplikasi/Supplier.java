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

public class Supplier {
    @FXML
    private AnchorPane supplierForm;

    @FXML
    private TextField supplierId;

    @FXML
    private TextField supplierName;

    @FXML
    private TextField contactName;

    @FXML
    private TextField address;

    @FXML
    private TextField city;

    @FXML
    private TextField postalCode;

    @FXML
    private TextField country;

    @FXML
    private TextField phone;

    @FXML
    private Button save;

    @FXML
    private Button cancel;

    @FXML
    private Button newData;

    private String username;

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
        supplierId.clear();
        supplierName.clear();
        contactName.clear();
        address.clear();
        city.clear();
        postalCode.clear();
        country.clear();
        phone.clear();
    }

    private void showAlert(Alert.AlertType alertType, String title, String message) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(null);
        alert.setContentText(message);
        alert.showAndWait();
    }

    private void saveSupplierData(String supplierId, String supplierName, String contactName, String address, String city, String postalCode, String country, String Phone) {
        String sql = "INSERT INTO supplier (SupplierID, SupplierName, ContactName, Address, City, PostalCode, Country, phone) VALUES (?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, supplierId);
            pstmt.setString(2, supplierName);
            pstmt.setString(3, contactName);
            pstmt.setString(4, address);
            pstmt.setString(5, city);
            pstmt.setString(6, postalCode);
            pstmt.setString(7, country);
            pstmt.setString(8, Phone);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            showAlert(Alert.AlertType.ERROR, "Database Error", "Failed to save supplier data: " + e.getMessage());
        }
    }

    @FXML
    private void handleSave() {
        String supplierIdText = supplierId.getText();
        String supplierNameText = supplierName.getText();
        String contactNameText = contactName.getText();
        String addressText = address.getText();
        String cityText = city.getText();
        String postalCodeText = postalCode.getText();
        String countryText = country.getText();
        String phonetext = phone.getText();

        if (supplierIdText.isEmpty() || supplierNameText.isEmpty() || contactNameText.isEmpty() || addressText.isEmpty() || cityText.isEmpty() || postalCodeText.isEmpty() || countryText.isEmpty() || phonetext.isEmpty()) {
            showAlert(Alert.AlertType.ERROR, "Form Error!", "Please enter all the details");
            return;
        }

        saveSupplierData(supplierIdText, supplierNameText, contactNameText, addressText, cityText, postalCodeText, countryText, phonetext);
        showAlert(Alert.AlertType.INFORMATION, "Success", "Supplier data saved successfully");
    }
}
