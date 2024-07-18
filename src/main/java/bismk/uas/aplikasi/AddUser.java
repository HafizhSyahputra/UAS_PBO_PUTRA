package bismk.uas.aplikasi;

import javafx.fxml.FXML;
import javafx.scene.Node;
import javafx.scene.control.Button;
import javafx.scene.control.ComboBox;
import javafx.scene.control.TextField;
import javafx.stage.Stage;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.PreparedStatement;
import java.sql.SQLException;

public class AddUser {

    // Koneksi database
    private final String url = "jdbc:mysql://localhost:3306/uas_pbo";
    private final String user = "root";
    private final String password = "";

    // Daftar peran
    private final String[] roles = {"supplier", "admin", "customer"};

    @FXML
    private TextField username;

    @FXML
    private TextField password1;

    @FXML
    private TextField phone;

    @FXML
    private TextField email;

    @FXML
    private TextField address;

    @FXML
    private ComboBox<String> role;

    @FXML
    private Button login;

    @FXML
    private Button cancel;

    @FXML
    void initialize() {
        // Menginisialisasi ComboBox peran dengan opsi yang tersedia
        role.getItems().addAll(roles);
    }

    @FXML
    void handleRegister() {
        // Mendapatkan nilai dari bidang teks
        String usernameValue = username.getText();
        String passwordValue = password1.getText();
        String phoneValue = phone.getText();
        String emailValue = email.getText();
        String addressValue = address.getText();
        String roleValue = role.getValue(); // Mendapatkan peran yang dipilih dari ComboBox

        // Melakukan koneksi ke database dan menyimpan data pengguna
        try (Connection connection = DriverManager.getConnection(url, user, password)) {
            String query = "INSERT INTO users (username, password, phone, email, address, role) VALUES (?, ?, ?, ?, ?, ?)";
            PreparedStatement preparedStatement = connection.prepareStatement(query);
            preparedStatement.setString(1, usernameValue);
            preparedStatement.setString(2, passwordValue);
            preparedStatement.setString(3, phoneValue);
            preparedStatement.setString(4, emailValue);
            preparedStatement.setString(5, addressValue);
            preparedStatement.setString(6, roleValue);
            preparedStatement.executeUpdate();
            System.out.println("User added successfully.");

            // Tutup jendela setelah menambahkan pengguna
            closeWindow();
        } catch (SQLException e) {
            System.err.println("Error: " + e.getMessage());
        }
    }

    @FXML
    void handleCancel() {
        // Tutup jendela ketika tombol cancel diklik
        closeWindow();
    }

    private void closeWindow() {
        // Mendapatkan referensi ke Stage
        Stage stage = (Stage) login.getScene().getWindow();
        // Tutup jendela
        stage.close();
    }
}
