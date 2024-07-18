package bismk.uas.aplikasi;

import javafx.fxml.FXML;
import javafx.scene.control.TableColumn;
import javafx.scene.control.TableView;
import javafx.scene.control.cell.PropertyValueFactory;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

public class Transaction {

    @FXML
    private TableView<TransactionLIST> OrderTableView;

    @FXML
    private void initialize() {
        // Panggil method untuk menampilkan data ke dalam tabel
        showOrders();
    }

    private void showOrders() {
        try (Connection connection = DriverManager.getConnection("jdbc:mysql://localhost:3306/uas_pbo", "root", "");
             Statement statement = connection.createStatement()) {
            String query = "SELECT * FROM `orderhistory`";
            ResultSet resultSet = statement.executeQuery(query);

            OrderTableView.getItems().clear();

            while (resultSet.next()) {
                int No = resultSet.getInt("NO");
                int productID = resultSet.getInt("ProductID");
                String productName = resultSet.getString("ProductName");
                String customerName = resultSet.getString("CustomerName");
                int qty = resultSet.getInt("Qty");
                double price = resultSet.getDouble("Price");
                double amount = resultSet.getDouble("Amount");

                // Buat objek TransactionLIST dari data yang diperoleh
                TransactionLIST transactionList = new TransactionLIST(No, productID, productName, customerName, qty, price, amount);

                // Tambahkan objek TransactionLIST ke dalam tabel
                OrderTableView.getItems().add(transactionList);
            }

        } catch (SQLException e) {
            e.printStackTrace();
        }
    }
}
