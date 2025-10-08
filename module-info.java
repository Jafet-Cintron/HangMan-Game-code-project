module com.example.hangmangamejavafx {
    requires javafx.controls;
    requires javafx.fxml;

    requires org.controlsfx.controls;
    requires com.dlsc.formsfx;

    opens com.example.hangmangamejavafx to javafx.fxml;
    exports com.example.hangmangamejavafx;
    exports com.example.hangmanjavafx;
    opens com.example.hangmanjavafx to javafx.fxml;
}