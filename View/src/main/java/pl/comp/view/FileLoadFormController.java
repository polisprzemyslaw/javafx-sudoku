package pl.comp.view;

import javafx.fxml.FXML;
import javafx.scene.control.Button;
import javafx.scene.control.ChoiceBox;
import javafx.scene.text.Text;

import java.util.List;
import java.util.ResourceBundle;

public class FileLoadFormController extends LoadSaveController {

    @FXML
    private ChoiceBox<String> choiceBox;

    @FXML
    private Text noFilesPopup;

    @FXML
    private Button confirmButton;

    private boolean loadFromMenu = false;

    @FXML
    public void fillChoiceBox(List<String> fileNames, ResourceBundle bundle, boolean loadFromMenu) {
        choiceBox.getItems().addAll(fileNames);
        if (choiceBox.getItems().isEmpty()) {
            noFilesPopup.setText(bundle.getString("fileLoad.noFiles"));
            confirmButton.setDisable(true);
        }
        this.loadFromMenu =  loadFromMenu;
    }


   @FXML
   @Override
   public void onConfirmButtonClick() {
       if (choiceBox.getValue() == null) {
           return;
       }
       confirmed = true;
       stage.close();
   }

   @Override
   public void onCancelButtonClick() {
       if (loadFromMenu) {
           Main.loadFXtoMainStage("difficultyForm.fxml");
       }
       stage.close();
   }

   @Override
   public String getFileName() {
       return choiceBox.getValue();
   }

}
