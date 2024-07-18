package bismk.uas.aplikasi;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.control.MenuButton;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.stage.Stage;

import java.io.IOException;

public class Dashboard {

    @FXML
    private BorderPane bp;

    @FXML
    private MenuButton profileMenuButton;

    private String username;

    public void setUsername(String username) {
        this.username = username;
        profileMenuButton.setText(username);
    }

    @FXML
    private void loadDashboard() throws IOException {
        FXMLLoader loader = new FXMLLoader(getClass().getResource("Dashboard.fxml"));
        BorderPane root = loader.load();
        bp.getChildren().setAll(root);
    }

    @FXML
    private void handleMaster() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Master.fxml"));
            Parent masterPane = loader.load();
            bp.setCenter(masterPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleTransaction() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("transaction.fxml"));
            Parent transactionPane = loader.load();
            bp.setCenter(transactionPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleManageStok() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Manage Stok.fxml"));
            Parent manageStokPane = loader.load();
            bp.setCenter(manageStokPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleSupplier() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Supplier.fxml"));
            Parent supplierPane = loader.load();
            bp.setCenter(supplierPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleCustomer() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Customer.fxml"));
            Parent customerPane = loader.load();
            bp.setCenter(customerPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleInventory() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InvList.fxml"));
            Parent inventoryPane = loader.load();
            bp.setCenter(inventoryPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleInventoryCheckout() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InventoryCheckOut.fxml"));
            Parent inventoryCPane = loader.load();
            bp.setCenter(inventoryCPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    @FXML
    private void handleInventoryRequest() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("InventoryRequest.fxml"));
            Parent inventoryRPane = loader.load();
            bp.setCenter(inventoryRPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleUser() {
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("user.fxml"));
            Parent userPane = loader.load();
            bp.setCenter(userPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }


    @FXML
    private void handleLogout() {
        try {
            Parent loginPane = FXMLLoader.load(getClass().getResource("/bismk/uas/aplikasi/login.fxml"));
            Stage stage = (Stage) bp.getScene().getWindow();
            stage.getScene().setRoot(loginPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}
