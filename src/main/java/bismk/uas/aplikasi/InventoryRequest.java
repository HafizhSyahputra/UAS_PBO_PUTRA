package bismk.uas.aplikasi;

import javafx.fxml.FXML;
import javafx.scene.control.Alert;
import javafx.scene.control.Button;

public class InventoryRequest {

    @FXML
    private Button sendButton;

    @FXML
    public void initialize() {
        sendButton.setOnAction(event -> {
            showAlert();
        });
    }

    private void showAlert() {
        Alert alert = new Alert(Alert.AlertType.INFORMATION);
        alert.setTitle("Request Sent");
        alert.setHeaderText(null);
        alert.setContentText("Successfully sent the inventory request!");

        alert.showAndWait();
    }
}
