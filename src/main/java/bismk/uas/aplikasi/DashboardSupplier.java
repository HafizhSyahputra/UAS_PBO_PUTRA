package bismk.uas.aplikasi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.Pane;

import java.io.IOException;

public class DashboardSupplier {

    @FXML
    private AnchorPane contentArea;
    @FXML
    private BorderPane bp;
    @FXML
    private void handleAddSupplier(ActionEvent event) {
        loadContent("Supplier.fxml");
    }

    @FXML
    private void handleManageStock(ActionEvent event) {
        loadContent("Manage Stok.fxml");
    }

    @FXML
    private void handleInventory(ActionEvent event) {
        loadContent("InvList.fxml");
    }
    @FXML
    private void handlePurchaseOrder(ActionEvent event) {
        loadContent("por.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        // Hapus konten area
        contentArea.getChildren().clear();

        // Hapus komponen navbar
        bp.setTop(null); // Hapus bagian atas BorderPane (navbar)

        // Arahkan pengguna ke layar login
        try {
            FXMLLoader loader = new FXMLLoader(getClass().getResource("Login.fxml"));
            Pane loginPane = loader.load();
            contentArea.getChildren().add(loginPane);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    private void loadContent(String fxmlFile) {
        try {
            Pane newLoadedPane = FXMLLoader.load(getClass().getResource(fxmlFile));
            contentArea.getChildren().clear();
            contentArea.getChildren().add(newLoadedPane);
            AnchorPane.setTopAnchor(newLoadedPane, 0.0);
            AnchorPane.setRightAnchor(newLoadedPane, 0.0);
            AnchorPane.setBottomAnchor(newLoadedPane, 0.0);
            AnchorPane.setLeftAnchor(newLoadedPane, 0.0);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public void setUsername(String username) {
    }
}
