package bismk.uas.aplikasi;

import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.time.LocalDate;

public class Por {

    @FXML
    private ComboBox<String> supplierNameComboBox;

    @FXML
    private TextArea addressField;

    @FXML
    private ComboBox<String> productNameComboBox;

    @FXML
    private TextField quantityField;

    @FXML
    private TextField phoneField;

    @FXML
    private TextField priceField;

    @FXML
    private DatePicker dateField;

    @FXML
    private TextField orderNoField;

    @FXML
    private TextField AmountTextField;
    @FXML
    private TableView<PORLIST> PurchaseTableView;

    @FXML
    private void initialize() {
        populateSupplierNames();
        populateProductNames();
        setCurrentDate();
        setOrderNo(); // Set the order number
        populateTable();
        quantityField.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateAmount();
        });
        priceField.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateAmount();
        });
    }

    private void populateTable() {
        ObservableList<PORLIST> purchaseList = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM `purchaseorderhistory`")) {

            while (resultSet.next()) {
                PORLIST purchaseOrder = new PORLIST(
                        resultSet.getInt("NO"),
                        resultSet.getInt("ProductID"),
                        resultSet.getString("ProductName"),
                        resultSet.getString("Supplier"),
                        resultSet.getInt("Qty"),
                        resultSet.getDouble("Price"),
                        resultSet.getString("Amount")
                );
                purchaseList.add(purchaseOrder);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        PurchaseTableView.setItems(purchaseList);
    }


    private void populateSupplierNames() {
        ObservableList<String> supplierNames = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT supplierName FROM supplier")) {

            while (resultSet.next()) {
                supplierNames.add(resultSet.getString("supplierName"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        supplierNameComboBox.setItems(supplierNames);
    }

    @FXML
    private void handleSupplierSelection() {
        String selectedSupplier = supplierNameComboBox.getSelectionModel().getSelectedItem();
        if (selectedSupplier != null) {
            fillSupplierAddress(selectedSupplier);
            fillSupplierPhone(selectedSupplier);
        }
    }

    private void fillSupplierAddress(String supplierName) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT address FROM supplier WHERE supplierName = '" + supplierName + "'")) {

            if (resultSet.next()) {
                addressField.setText(resultSet.getString("address"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillSupplierPhone(String supplierName) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT phone FROM supplier WHERE supplierName = '" + supplierName + "'")) {

            if (resultSet.next()) {
                phoneField.setText(resultSet.getString("phone"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void populateProductNames() {
        ObservableList<String> productNames = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT productName FROM product")) {

            while (resultSet.next()) {
                productNames.add(resultSet.getString("productName"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        productNameComboBox.setItems(productNames);
    }

    @FXML
    private void handleProductSelection() {
        String selectedProduct = productNameComboBox.getSelectionModel().getSelectedItem();
        if (selectedProduct != null) {
            fillProductPrice(selectedProduct);
        }
    }

    private void fillProductQuantity(String productName) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT quantityInStock FROM inventory WHERE productID = (SELECT productID FROM product WHERE productName = '" + productName + "')")) {

            if (resultSet.next()) {
                quantityField.setText(String.valueOf(resultSet.getInt("quantityInStock")));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void fillProductPrice(String productName) {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT UnitPrice FROM product WHERE productName = '" + productName + "'")) {

            if (resultSet.next()) {
                priceField.setText(resultSet.getString("UnitPrice"));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void setCurrentDate() {
        dateField.setValue(LocalDate.now());
    }

    private void setOrderNo() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT MAX(NO) AS max_no FROM purchaseorderhistory")) {

            if (resultSet.next()) {
                int maxNo = resultSet.getInt("max_no");
                orderNoField.setText(String.valueOf(maxNo + 1));
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void calculateAmount() {
        try {
            int quantity = Integer.parseInt(quantityField.getText());
            double price = Double.parseDouble(priceField.getText());
            double amount = quantity * price;
            AmountTextField.setText(String.valueOf(amount));
        } catch (NumberFormatException e) {
            AmountTextField.clear();
        }
    }


    @FXML
    private void handleEnterList() {
        String orderNo = orderNoField.getText();
        String productName = productNameComboBox.getSelectionModel().getSelectedItem();
        String supplierName = supplierNameComboBox.getSelectionModel().getSelectedItem();
        int quantity = Integer.parseInt(quantityField.getText());
        double price = Double.parseDouble(priceField.getText());
        double amount = quantity * price;

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             PreparedStatement preparedStatement = connection.prepareStatement("INSERT INTO purchaseorderhistory (NO, `ProductID`, `ProductName`, Supplier, Qty, Price, Amount) VALUES (?, (SELECT productID FROM product WHERE productName = ?), ?, ?, ?, ?, ?)")) {

            preparedStatement.setInt(1, Integer.parseInt(orderNo));
            preparedStatement.setString(2, productName);
            preparedStatement.setString(3, productName);
            preparedStatement.setString(4, supplierName);
            preparedStatement.setInt(5, quantity);
            preparedStatement.setDouble(6, price);
            preparedStatement.setDouble(7, amount);

            preparedStatement.executeUpdate();

            // Menambahkan ini untuk memperbarui TableView setelah data dimasukkan
            populateTable();


            showAlert(Alert.AlertType.INFORMATION, "Success", "Purchasing Data", "Data successfully purchased.");
            setOrderNo();

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    private void showAlert(Alert.AlertType alertType, String title, String headerText, String contentText) {
        Alert alert = new Alert(alertType);
        alert.setTitle(title);
        alert.setHeaderText(headerText);
        alert.setContentText(contentText);
        alert.showAndWait();
    }

}
