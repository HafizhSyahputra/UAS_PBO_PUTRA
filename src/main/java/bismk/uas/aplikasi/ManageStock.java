package bismk.uas.aplikasi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.TextField;
import javafx.scene.control.cell.PropertyValueFactory;
import javafx.stage.Stage;
import javafx.util.StringConverter;

import java.io.IOException;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.HashMap;
import java.util.Map;

public class ManageStock {

    @FXML
    private TextField productIdField;

    @FXML
    private TextField productNameField;

    @FXML
    private ComboBox<Integer> supplierIdField;

    @FXML
    private TextField categoryField;

    @FXML
    private TextField unitPriceField;

    @FXML
    private TableView<Product> stockTable;

    @FXML
    private TableColumn<Product, Integer> productIdColumn;

    @FXML
    private TableColumn<Product, String> productNameColumn;

    @FXML
    private TableColumn<Product, Integer> supplierIdColumn;

    @FXML
    private TableColumn<Product, String> categoryColumn;

    @FXML
    private TableColumn<Product, Double> unitPriceColumn;

    @FXML
    private TableColumn<Product, Integer> quantityColumn;

    private Map<Integer, String> supplierMap = new HashMap<>();

    @FXML
    private void initialize() {
        productIdColumn.setCellValueFactory(new PropertyValueFactory<>("productID"));
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        supplierIdColumn.setCellValueFactory(new PropertyValueFactory<>("supplierID"));
        categoryColumn.setCellValueFactory(new PropertyValueFactory<>("category"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        quantityColumn.setCellValueFactory(new PropertyValueFactory<>("quantity"));

        loadSupplierIds();
        loadProductData();
        setNextProductId();
    }

    @FXML
    private void handleSave() {
        String productId = productIdField.getText();
        String productName = productNameField.getText();
        Integer supplierId = supplierIdField.getValue();
        String category = categoryField.getText();
        String unitPrice = unitPriceField.getText();

        // Perform validation if needed

        saveToDatabase(productId, productName, supplierId, category, unitPrice);

        // Reload product data
        loadProductData();

        showInventoryForm();
    }

    @FXML
    private void handleCancel() {
        // Close the window when cancel button is clicked
        closeWindow();
    }

    private void closeWindow() {
        Stage stage = (Stage) productIdField.getScene().getWindow();
        stage.close();
    }

    private void saveToDatabase(String productId, String productName, Integer supplierId, String category, String unitPrice) {
        String sql = "INSERT INTO product (ProductID, ProductName, SupplierID, Category, UnitPrice) VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, productId);
            pstmt.setString(2, productName);
            pstmt.setInt(3, supplierId);
            pstmt.setString(4, category);
            pstmt.setString(5, unitPrice);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void loadProductData() {
        ObservableList<Product> productList = FXCollections.observableArrayList();
        String sql = "SELECT p.ProductID, p.ProductName, p.SupplierID, p.Category, p.UnitPrice, i.QuantityInStock " +
                "FROM product p " +
                "JOIN inventory i ON p.ProductID = i.ProductID";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int productID = rs.getInt("ProductID");
                String productName = rs.getString("ProductName");
                int supplierID = rs.getInt("SupplierID");
                String category = rs.getString("Category");
                double unitPrice = rs.getDouble("UnitPrice");
                int quantity = rs.getInt("QuantityInStock");

                Product product = new Product(productID, productName, supplierID, category, unitPrice, quantity);
                productList.add(product);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        stockTable.setItems(productList);
    }

    private void loadSupplierIds() {
        ObservableList<Integer> supplierIds = FXCollections.observableArrayList();
        String sql = "SELECT SupplierID, SupplierName FROM supplier";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            while (rs.next()) {
                int supplierID = rs.getInt("SupplierID");
                String supplierName = rs.getString("SupplierName");
                supplierIds.add(supplierID);
                supplierMap.put(supplierID, supplierName);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        supplierIdField.setItems(supplierIds);

        // Set StringConverter to display ID and name in ComboBox
        supplierIdField.setConverter(new StringConverter<Integer>() {
            @Override
            public String toString(Integer supplierId) {
                if (supplierId != null) {
                    return supplierId + " - " + supplierMap.get(supplierId);
                }
                return null;
            }

            @Override
            public Integer fromString(String string) {
                return null; // Not needed for this case
            }
        });
    }



    private void setNextProductId() {
        String sql = "SELECT MAX(ProductID) AS maxProductId FROM product";

        try (Connection conn = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {

            if (rs.next()) {
                int maxProductId = rs.getInt("maxProductId");
                productIdField.setText(String.valueOf(maxProductId + 1));
            } else {
                productIdField.setText("1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showInventoryForm() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Quantity.fxml"));
            Parent root = loader.load();

            Stage stage = new Stage();
            stage.setScene(new Scene(root));
            stage.setTitle("Inventory Form");

            stage.show();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
