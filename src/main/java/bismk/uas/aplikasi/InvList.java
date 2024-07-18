package bismk.uas.aplikasi;

import bismk.uas.aplikasi.InventoryItem;
import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class InvList {

    @FXML
    private TableView<InventoryItem> inventoryTableView;

    @FXML
    private TableColumn<InventoryItem, String> productNameColumn;

    @FXML
    private TableColumn<InventoryItem, String> supplierNameColumn;

    @FXML
    private TableColumn<InventoryItem, Integer> quantityInStockColumn;

    @FXML
    private TableColumn<InventoryItem, Double> unitPriceColumn;

    @FXML
    private TableColumn<InventoryItem, Integer> qtyOutColumn;

    @FXML
    private void initialize() {
        // Koneksi ke database
        productNameColumn.setCellValueFactory(new PropertyValueFactory<>("productName"));
        supplierNameColumn.setCellValueFactory(new PropertyValueFactory<>("supplierName"));
        quantityInStockColumn.setCellValueFactory(new PropertyValueFactory<>("quantityInStock"));
        unitPriceColumn.setCellValueFactory(new PropertyValueFactory<>("unitPrice"));
        qtyOutColumn.setCellValueFactory(new PropertyValueFactory<>("qtyOut")); // Menghubungkan TableColumn dengan property qtyOut

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "")) {
            String query = "SELECT p.ProductName, s.SupplierName, i.QuantityInStock, p.UnitPrice, q.Qty " +
                    "FROM product p " +
                    "JOIN inventory i ON p.ProductID = i.ProductID " +
                    "JOIN supplier s ON p.SupplierID = s.SupplierID " +
                    "JOIN orderhistory q ON p.ProductID = q.ProductID"; // Menggabungkan dengan tabel Qty-out
            PreparedStatement statement = connection.prepareStatement(query);
            ResultSet resultSet = statement.executeQuery();

            // Menambahkan item-item hasil query ke dalam TableView
            while (resultSet.next()) {
                String productName = resultSet.getString("ProductName");
                String supplierName = resultSet.getString("SupplierName");
                int quantityInStock = resultSet.getInt("QuantityInStock");
                double unitPrice = resultSet.getDouble("UnitPrice");
                int qtyOut = resultSet.getInt("Qty");

                inventoryTableView.getItems().add(new InventoryItem(productName, supplierName, quantityInStock, unitPrice, qtyOut)); // Menambahkan qtyOut ke InventoryItem
            }

            // Menutup statement dan resultSet
            statement.close();
            resultSet.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
