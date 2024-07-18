package bismk.uas.aplikasi;

import javafx.event.ActionEvent;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.layout.AnchorPane;
import javafx.scene.layout.BorderPane;
import javafx.scene.layout.HBox;
import javafx.scene.layout.Pane;

import javax.security.auth.login.AccountNotFoundException;
import java.io.IOException;

public class DashboardCustomer {

    @FXML
    private AnchorPane contentArea;

    @FXML
    private BorderPane bp;

    @FXML
    private void handleCustomer(ActionEvent event) {
        loadContent("Customer.fxml");
    }


    @FXML
    private void handleOrder(ActionEvent event) {
        loadContent("Order.fxml");
    }

    @FXML
    private void handleLogout(ActionEvent event) {
        contentArea.getChildren().clear();

        bp.setTop(null); // Hapus bagian atas BorderPane (navbar)

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
