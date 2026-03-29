package pl.comp.view;

import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.ComboBox;
import javafx.scene.control.RadioButton;
import javafx.scene.control.ToggleGroup;
import javafx.stage.Modality;
import javafx.stage.Stage;
import pl.comp.model.*;
import pl.comp.view.exceptions.FxmlLoadException;

import java.io.IOException;
import java.util.Locale;
import java.util.ResourceBundle;

public class DifficultyFormController {
    @FXML
    private RadioButton easyRadioButton;
    @FXML
    private RadioButton mediumRadioButton;
    @FXML
    private RadioButton hardRadioButton;

    @FXML
    private ComboBox<String> languageComboBox;

    private SudokuBoard gameBoard;

    @FXML
    public void initialize() {

        languageComboBox.getItems().addAll("English", "Polski");

        if (Locale.getDefault().getLanguage().equals("pl")) {
            languageComboBox.getSelectionModel().select("Polski");
        } else {
            languageComboBox.getSelectionModel().select("English");
        }

        easyRadioButton.setUserData(DifficultyLevel.EASY);
        mediumRadioButton.setUserData(DifficultyLevel.MEDIUM);
        hardRadioButton.setUserData(DifficultyLevel.HARD);
    }

    @FXML
    public void onLanguageChange() {
        String language = languageComboBox.getValue();
        if (language.equals("Polski")) {
            Locale.setDefault(new Locale("pl", "PL"));
        } else {
            Locale.setDefault(new Locale("en", "US"));
        }
        Main.loadFXtoMainStage("difficultyForm.fxml");
    }

    @FXML
    private ToggleGroup difficultyGroup;

    @FXML
    public void onStartButtonClick() {
        try {
            SudokuBoard mainBoard = new SudokuBoard();
            mainBoard.solveGame();
            gameBoard = mainBoard.clone();

            RadioButton radioButton = (RadioButton) difficultyGroup.getSelectedToggle();

            if (radioButton != null) {
                DifficultyLevel level = (DifficultyLevel) radioButton.getUserData();
                level.removeFields(gameBoard);
            }

            showBoard(false);
        } catch (FxmlLoadException e) {
            Main.handleError(e, true);
        }
    }

    private void showBoard(boolean fromFile) {
        Stage stage = (Stage) mediumRadioButton.getScene().getWindow();
        try {
            FXMLLoader fxmlLoader = Main.loadFXtoMainStage("board.fxml");

            ResourceBundle bundle = fxmlLoader.getResources();

            stage.setWidth(500);
            stage.setHeight(550);

            BoardDisplayController controller = fxmlLoader.getController();
            stage.setTitle(bundle.getString("board.title"));


            if (!fromFile) {
                controller.setBoard(gameBoard);
                controller.loadBoard();
            } else {
                controller.setLoadedFromMenu(true);
                boolean success = controller.onLoadButtonClick();
                if (!success) {
                    Main.loadFXtoMainStage("difficultyForm.fxml");
                }
            }
        } catch (FxmlLoadException e) {
            Main.handleError(e, true);
    }
    }

    @FXML
    public void onLoadButtonClick() {
        showBoard(true);
    }

    @FXML
    public void onCreditsButtonClick()  {
        ResourceBundle bundle = ResourceBundle.getBundle("pl.comp.view.locale");
        FXMLLoader fxmlLoader = new FXMLLoader(getClass().getResource("credits.fxml"));
        fxmlLoader.setResources(bundle);
        Parent creditsRoot;
        try {
            creditsRoot = fxmlLoader.load();
        } catch (IOException e) {
            throw new FxmlLoadException("error.fxmlLoadException", e);
        }
        Stage creditsDialog = new Stage();
        creditsDialog.setTitle(bundle.getString("credits.title"));
        creditsDialog.initModality(Modality.WINDOW_MODAL);
        creditsDialog.initOwner(mediumRadioButton.getScene().getWindow());

        Scene dialogScene = new Scene(creditsRoot);
        creditsDialog.setScene(dialogScene);

        CreditsDisplayController creditsController = fxmlLoader.getController();
        creditsController.setStage(creditsDialog);
        creditsController.setText(bundle);

        creditsDialog.showAndWait();
    }
}
