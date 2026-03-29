package pl.comp.view;

import javafx.fxml.FXML;
import javafx.stage.Stage;

abstract class LoadSaveController {
    protected boolean confirmed =  false;

    protected Stage stage;


    @FXML
    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    abstract void onConfirmButtonClick();

    @FXML
    public void onCancelButtonClick() {
        stage.close();
    }


    public boolean isConfirmed() {
        return confirmed;
    }


    abstract String getFileName();

}
