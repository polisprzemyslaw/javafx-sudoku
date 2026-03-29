module View {
    requires ModelProject;
    requires javafx.graphics;
    requires javafx.controls;
    requires javafx.fxml;
    requires org.slf4j;
    exports pl.comp.view;
    exports pl.comp.view.exceptions;
    opens pl.comp.view to javafx.fxml;
}