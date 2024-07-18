package bismk.uas.aplikasi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class Quantity {

    @FXML
    private ComboBox<QuantityItem> productIdComboBox;

    @FXML
    private TextField quantityInStockField;

    @FXML
    private TextField reorderLevelField;

    @FXML
    private ComboBox<String> statusField;

    private ObservableList<QuantityItem> products = FXCollections.observableArrayList();

    @FXML
    private void initialize() {
        loadProducts();
        productIdComboBox.setItems(products);
        productIdComboBox.setConverter(new ProductStringConverter());
    }

    @FXML
    private void handleSave() {
        QuantityItem selectedProduct = productIdComboBox.getValue();
        if (selectedProduct != null) {
            String productId = selectedProduct.getId();
            String quantityInStock = quantityInStockField.getText();
            String reorderLevel = reorderLevelField.getText();
            String status = statusField.getValue();

            // Perform validation if needed

            saveToDatabase(productId, quantityInStock, reorderLevel, status);

            // Close the window after saving
            closeWindow();
        }
    }

    @FXML
    private void handleCancel() {
        // Close the window when cancel button is clicked
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) productIdComboBox.getScene().getWindow();
        stage.close();
    }

    private void saveToDatabase(String productId, String quantityInStock, String reorderLevel, String status) {
        String sql = "INSERT INTO inventory (ProductID, QuantityInStock, ReorderLevel, Status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productId);
            pstmt.setString(2, quantityInStock);
            pstmt.setString(3, reorderLevel);
            pstmt.setString(4, status);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadProducts() {
        String sql = "SELECT ProductID, ProductName FROM product";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                String productId = rs.getString("ProductID");
                String productName = rs.getString("ProductName");
                products.add(new QuantityItem(productId, productName));
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private static class ProductStringConverter extends javafx.util.StringConverter<QuantityItem> {
        @Override
        public String toString(QuantityItem product) {
            return (product != null) ? product.getId() + " - " + product.getName() : null;
        }

        @Override
        public QuantityItem fromString(String string) {
            // Not needed for ComboBox
            return null;
        }
    }
}
