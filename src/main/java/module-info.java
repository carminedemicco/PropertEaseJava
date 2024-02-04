module main.propertease {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;
    exports main.propertease;
    exports main.propertease.builder;
    exports main.propertease.decorator;
    exports main.propertease.server;
    opens main.propertease to javafx.fxml;
    opens main.propertease.server to javafx.fxml;
}
