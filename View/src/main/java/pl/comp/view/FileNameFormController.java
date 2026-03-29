package pl.comp.view;

import javafx.fxml.FXML;
import javafx.scene.control.TextField;

public class FileNameFormController extends LoadSaveController {

    @FXML
    private TextField fileNameField;


    @FXML
    public void onConfirmButtonClick() {
        if (fileNameField == null || fileNameField.getText().isBlank()) {
            return;
        }

        confirmed = true;
        stage.close();
    }

    @Override
    public String getFileName() {
        return fileNameField.getText();
    }
}
