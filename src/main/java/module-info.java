module com.uprojects.amongus {
    requires javafx.controls;
    requires javafx.swing;
    requires libtiled;
    requires java.desktop;
    requires java.xml.bind;
    requires javafx.fxml;
    requires javafx.graphics;

    exports com.uprojects.screens;
    exports com.uprojects.entities;
    exports com.uprojects.helpers;
    opens com.uprojects.launcher to javafx.graphics;
    opens com.uprojects.screens to javafx.fxml;
    opens maps to libtiled, javafx.graphics;

}