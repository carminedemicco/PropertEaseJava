module main.propertease {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    opens main.propertease to javafx.fxml;
    exports main.propertease;
    exports main.propertease.builder;
    exports main.propertease.decorator;
}
