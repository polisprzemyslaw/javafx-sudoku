package pl.comp.view;

import javafx.application.Application;
import javafx.application.Platform;
import javafx.fxml.FXMLLoader;
import javafx.scene.Scene;
import javafx.scene.control.Alert;
import javafx.stage.Stage;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import pl.comp.model.exceptions.SudokuDaoException;
import pl.comp.model.exceptions.SudokuRuntimeException;
import pl.comp.view.exceptions.BeanPBuildException;
import pl.comp.view.exceptions.FxmlLoadException;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class Main extends Application {

    private static Scene scene;
    private static Stage stage;

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public void start(Stage stage) {
        Thread.setDefaultUncaughtExceptionHandler((thread, throwable) ->
                handleError(throwable, true));

        try {
            Main.stage = stage;
            Locale.setDefault(new Locale("en", "US"));
            ResourceBundle bundle = ResourceBundle.getBundle("pl.comp.view.locale");
            FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("difficultyForm.fxml"));
            fxmlLoader.setResources(bundle);
            stage.setTitle(bundle.getString("difficulty.title"));
            scene = new Scene(fxmlLoader.load(), 400, 300);
        } catch (IOException | IllegalStateException e) {
            logger.error("Error loading difficultyForm.fxml file", e);
            handleError(new FxmlLoadException("error.fxmlLoadException", e), true);
        }
        stage.setScene(scene);
        stage.show();
    }

    public static FXMLLoader loadFXtoMainStage(String fxml) {
        ResourceBundle bundle = ResourceBundle.getBundle("pl.comp.view.locale");
        FXMLLoader fxmlLoader = new FXMLLoader(Main.class.getResource(fxml));
        fxmlLoader.setResources(bundle);

        try {
            scene.setRoot(fxmlLoader.load());
            stage.sizeToScene();
        } catch (IOException | IllegalStateException e) {
            logger.error("Error loading {} file", fxml, e);
            throw new FxmlLoadException("error.fxmlLoadException", e);
        }
        return fxmlLoader;

    }


    public static void handleError(Throwable e, boolean critical) {
        Alert.AlertType alertType = critical ? Alert.AlertType.ERROR : Alert.AlertType.WARNING;
        Alert alert = new Alert(alertType);

        ResourceBundle bundle = ResourceBundle.getBundle("pl.comp.view.locale");

        if (critical) {
            alert.setTitle(bundle.getString("error.critical.title"));
            alert.setHeaderText(bundle.getString("error.critical.title"));
        } else {
            alert.setTitle(bundle.getString("error.normal.title"));
            alert.setHeaderText(bundle.getString("error.normal.title"));
        }

        if (e instanceof BeanPBuildException
        ||  e instanceof FxmlLoadException
        ||  e instanceof SudokuDaoException
        ||  e instanceof SudokuRuntimeException) {
            alert.setContentText(e.getLocalizedMessage());
        } else {
            alert.setContentText(bundle.getString("error.unexpected"));
        }

        alert.showAndWait();
        if (critical) {
            logger.error(e.getLocalizedMessage(), e);
            Platform.exit();
        }
    }

    public static void handleError(Throwable e, boolean critical, boolean cleanLoad) {
        handleError(e, critical);
        if (e instanceof SudokuDaoException && cleanLoad) {
            Main.loadFXtoMainStage("difficultyForm.fxml");
        }
    }

    public static void main() {
        launch();
    }
}
