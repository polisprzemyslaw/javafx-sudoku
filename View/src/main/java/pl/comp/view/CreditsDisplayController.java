package pl.comp.view;

import javafx.fxml.FXML;
import javafx.scene.text.Text;
import javafx.stage.Stage;

import java.util.Locale;
import java.util.ResourceBundle;

public class CreditsDisplayController {

    @FXML
    private Text textAuthors;
    @FXML
    private Text textUniversity;

    @FXML
    private Stage stage;

    @FXML
    public void setText(ResourceBundle bundle) {
        ResourceBundle rb = ResourceBundle.getBundle("pl.comp.bundles.Authors", Locale.getDefault());

        StringBuilder authorsString = new StringBuilder();
        for (String author: rb.getStringArray("authors")) {
            authorsString.append(author).append(", ");
        }

        textAuthors.setWrappingWidth(330);
        textUniversity.setWrappingWidth(330);

        textAuthors.setText(bundle.getString("credits.authors") + ": " + authorsString);
        textUniversity.setText(bundle.getString("credits.university") + ": " + rb.getString("university"));
    }

    public void setStage(Stage stage) {
        this.stage = stage;
    }

    @FXML
    public void onExitButtonClick() {
        stage.close();
    }
}
