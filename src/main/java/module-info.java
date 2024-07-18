module bismk.uas.aplikasi {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;


    opens bismk.uas.aplikasi to javafx.fxml;
    exports bismk.uas.aplikasi;
}