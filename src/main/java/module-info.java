module main.propertease {
    requires javafx.controls;
    requires javafx.fxml;
    requires java.sql;
    requires org.json;
    requires org.glavo.png;
    exports main.propertease;
    exports main.propertease.builder;
    exports main.propertease.decorator;
    exports main.propertease.memento;
    exports main.propertease.command;
    exports main.propertease.server;
    exports main.propertease.server.strategy;
    exports main.propertease.server.proxy;
    opens main.propertease to javafx.fxml;
    opens main.propertease.server to javafx.fxml;
}
