package bismk.uas.aplikasi;

import java.sql.*;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import javafx.collections.FXCollections;
import javafx.collections.ObservableList;
import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.scene.control.*;
import javafx.scene.control.cell.PropertyValueFactory;

public class Order {

    @FXML
    private ComboBox<String> customerComboBox;

    @FXML
    private TextField addressTextField;

    @FXML
    private ComboBox<String> productComboBox;

    @FXML
    private TextField quantityTextField;

    @FXML
    private TextField orderNoTextField;

    @FXML
    private TextField dateTextField;

    @FXML
    private TextField statusTextField;

    @FXML
    private TextField priceTextField;

    @FXML
    private TextField amountTextField;

    @FXML
    private TableView<ORDERLIST> OrderTableHistory;


    private Connection connection;
    private PreparedStatement preparedStatement;


    private void populateTable() {
        ObservableList<ORDERLIST> orderList = FXCollections.observableArrayList();

        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             Statement statement = connection.createStatement();
             ResultSet resultSet = statement.executeQuery("SELECT * FROM orderhistory")) {

            while (resultSet.next()) {
                ORDERLIST order = new ORDERLIST(
                        resultSet.getInt("NO"),
                        resultSet.getInt("ProductID"),
                        resultSet.getString("ProductName"),
                        resultSet.getString("CustomerName"),
                        resultSet.getInt("Qty"),
                        resultSet.getDouble("Price"),
                        resultSet.getString("Amount")
                );
                orderList.add(order);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }

        OrderTableHistory.setItems(orderList);
    }
    @FXML
    private void initialize() {
        try {
            connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Populate customer names
        ObservableList<String> customerNames = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT name FROM customer");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                customerNames.add(resultSet.getString("name"));
            }
            customerComboBox.setItems(customerNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Populate product names
        ObservableList<String> productNames = FXCollections.observableArrayList();
        try {
            preparedStatement = connection.prepareStatement("SELECT ProductName FROM product");
            ResultSet resultSet = preparedStatement.executeQuery();
            while (resultSet.next()) {
                productNames.add(resultSet.getString("ProductName"));
            }
            productComboBox.setItems(productNames);
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Listener for customerComboBox to set address
        customerComboBox.setOnAction(event -> {
            String selectedCustomer = customerComboBox.getValue();
            if (selectedCustomer != null) {
                try {
                    preparedStatement = connection.prepareStatement("SELECT address FROM customer WHERE name = ?");
                    preparedStatement.setString(1, selectedCustomer);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        addressTextField.setText(resultSet.getString("address"));
                    } else {
                        addressTextField.clear();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        // Listener for productComboBox to set status and price
        productComboBox.setOnAction(event -> {
            String selectedProduct = productComboBox.getValue();
            if (selectedProduct != null) {
                try {
                    // Get ProductID, status, and price
                    preparedStatement = connection.prepareStatement(
                            "SELECT p.ProductID, p.UnitPrice, i.status " +
                                    "FROM product p " +
                                    "JOIN inventory i ON p.ProductID = i.ProductID " +
                                    "WHERE p.ProductName = ?"
                    );
                    preparedStatement.setString(1, selectedProduct);
                    ResultSet resultSet = preparedStatement.executeQuery();
                    if (resultSet.next()) {
                        int productId = resultSet.getInt("ProductID");
                        String status = resultSet.getString("status");
                        double unitPrice = resultSet.getDouble("UnitPrice");

                        statusTextField.setText(status);
                        priceTextField.setText(String.valueOf(unitPrice));
                        calculateAmount();
                    } else {
                        statusTextField.clear();
                        priceTextField.clear();
                        amountTextField.clear();
                    }
                } catch (SQLException e) {
                    e.printStackTrace();
                }
            }
        });

        // Listener for quantityTextField to calculate amount
        quantityTextField.textProperty().addListener((observable, oldValue, newValue) -> {
            calculateAmount();
        });

        // Set order number, date, and amount
        setOrderDetails();

        populateTable();

    }

    private void calculateAmount() {
        try {
            int quantity = Integer.parseInt(quantityTextField.getText());
            double price = Double.parseDouble(priceTextField.getText());
            double amount = quantity * price;
            amountTextField.setText(String.valueOf(amount));
        } catch (NumberFormatException e) {
            amountTextField.clear();
        }
    }

    private void setOrderDetails() {
        // Set order number
        try {
            preparedStatement = connection.prepareStatement("SELECT MAX(NO) AS maxNo FROM orderhistory");
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                int maxNo = resultSet.getInt("maxNo");
                orderNoTextField.setText(String.valueOf(maxNo + 1));
            } else {
                orderNoTextField.setText("1");
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }

        // Set current date
        LocalDate currentDate = LocalDate.now();
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd");
        dateTextField.setText(currentDate.format(formatter));
    }

    @FXML
    private void handleEnterList(ActionEvent event) {
        try {
            String productName = productComboBox.getValue();
            String customerName = customerComboBox.getValue();
            int quantity = Integer.parseInt(quantityTextField.getText());
            double price = Double.parseDouble(priceTextField.getText());
            double amount = Double.parseDouble(amountTextField.getText());

            // Get ProductID
            int productId = 0;
            preparedStatement = connection.prepareStatement("SELECT ProductID FROM product WHERE ProductName = ?");
            preparedStatement.setString(1, productName);
            ResultSet resultSet = preparedStatement.executeQuery();
            if (resultSet.next()) {
                productId = resultSet.getInt("ProductID");
            }

            // Insert into orderhistory
            String sql = "INSERT INTO orderhistory (ProductID, ProductName, CustomerName, Qty, Price, Amount) VALUES (?, ?, ?, ?, ?, ?)";
            preparedStatement = connection.prepareStatement(sql);
            preparedStatement.setInt(1, productId);
            preparedStatement.setString(2, productName);
            preparedStatement.setString(3, customerName);
            preparedStatement.setInt(4, quantity);
            preparedStatement.setDouble(5, price);
            preparedStatement.setDouble(6, amount);
            preparedStatement.executeUpdate();

            System.out.println("Data inserted into orderhistory successfully");
            populateTable();

            showAlert(Alert.AlertType.INFORMATION, "Success", "Purchasing Data", "Data successfully purchased.");

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    public void onClose() {
        try {
            if (preparedStatement != null) {
                preparedStatement.close();
            }
            if (connection != null) {
                connection.close();
            }
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
