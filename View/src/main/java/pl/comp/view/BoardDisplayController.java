package pl.comp.view;

import javafx.beans.property.adapter.JavaBeanIntegerProperty;
import javafx.beans.property.adapter.JavaBeanIntegerPropertyBuilder;
import javafx.fxml.FXML;
import javafx.fxml.FXMLLoader;
import javafx.scene.Node;
import javafx.scene.Parent;
import javafx.scene.Scene;
import javafx.scene.control.TextField;
import javafx.scene.control.TextFormatter;
import javafx.scene.layout.GridPane;
import javafx.stage.Modality;
import javafx.stage.Stage;
import javafx.util.StringConverter;
import pl.comp.model.*;
import pl.comp.model.exceptions.SerialReadException;
import pl.comp.model.exceptions.SudokuDaoException;
import pl.comp.view.exceptions.BeanPBuildException;
import pl.comp.view.exceptions.FxmlLoadException;

import java.io.IOException;
import java.util.List;
import java.util.ResourceBundle;
import java.util.function.UnaryOperator;

public class BoardDisplayController {

    private SudokuBoard board;
    private SudokuBoard originalBoard;

    @FXML
    private final TextField[][] fields = new TextField[9][9];

    @FXML
    private GridPane mainGrid;

    public void setLoadedFromMenu(boolean loadedFromMenu) {
        this.loadedFromMenu = loadedFromMenu;
    }

    private boolean loadedFromMenu = false;

    private final ResourceBundle bundle = ResourceBundle.getBundle("pl.comp.view.locale");

    public void setBoard(SudokuBoard newBoard) {
        this.board = newBoard;
        this.originalBoard = board.clone();
    }

    @FXML
    public void initialize() {
        for (Node box : mainGrid.getChildren()) {
            if (box instanceof GridPane) {
                for (Node field : ((GridPane) box).getChildren()) {
                    Integer boxRow = GridPane.getRowIndex(box);
                    if (boxRow == null) {
                        boxRow = 0;
                    }

                    Integer boxCol = GridPane.getColumnIndex(box);
                    if (boxCol == null) {
                        boxCol = 0;
                    }

                    Integer fieldRow = GridPane.getRowIndex(field);
                    if (fieldRow == null) {
                        fieldRow = 0;
                    }

                    Integer fieldCol = GridPane.getColumnIndex(field);
                    if (fieldCol == null) {
                        fieldCol = 0;
                    }

                    fields[boxRow * 3 + fieldRow][boxCol * 3 + fieldCol] = (TextField) field;
                }
            }
        }
    }

    private final JavaBeanIntegerProperty[][] properties = new JavaBeanIntegerProperty[9][9];

    @FXML
    public void loadBoard() {
        resetFields();
        JavaBeanIntegerPropertyBuilder integerPropertyBuilder = JavaBeanIntegerPropertyBuilder.create();
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (originalBoard.getField(i,j) != 0) {
                    fields[i][j].setText(String.valueOf(originalBoard.getField(i,j)));
                    fields[i][j].setEditable(false);

                    fields[i][j].setStyle(
                            "-fx-background-color: lightgray; -fx-border-color: black; -fx-alignment: center;"
                    );

                } else {
                    fields[i][j].setEditable(true);
                    fields[i][j].setText("");

                    if (fieldCorrect(i,j)) {
                        fields[i][j].setStyle(
                                "-fx-background-color: white; -fx-border-color: black; -fx-alignment: center;"
                        );
                    } else {
                        fields[i][j].setStyle(
                                "-fx-background-color: red; -fx-border-color: black; -fx-alignment: center;"
                        );
                    }

                    UnaryOperator<TextFormatter.Change> filter = change -> {
                        if (change.getControlNewText().matches("[1-9]?")) {
                            return change;
                        }
                        return null;
                    };

                    fields[i][j].setTextFormatter(new TextFormatter<>(filter));

                    JavaBeanIntegerProperty beanProperty;
                    try {
                        beanProperty = integerPropertyBuilder
                                .bean(board.getFieldObject(i,j))
                                .name("fieldValue")
                                .setter("setFieldValue")
                                .getter("getFieldValue")
                                .build();
                    } catch (NoSuchMethodException e) {
                        throw new BeanPBuildException("error.beanException", e);
                    }

                    properties[i][j] = beanProperty;

                    StringConverter stringConverter = new CustomIntegerStringConverter();
                    fields[i][j].textProperty().bindBidirectional(beanProperty, stringConverter);


                    beanProperty.addListener((observable, oldValue, newValue) -> {
                        for (int x = 0; x < 9; x++) {
                            for (int y = 0; y < 9; y++) {
                                if (fields[x][y].isEditable()) {
                                    if (fieldCorrect(x,y)) {
                                        fields[x][y].setStyle(
                                        "-fx-background-color: white; -fx-border-color: black; -fx-alignment: center;"
                                        );
                                    } else {
                                        fields[x][y].setStyle(
                                        "-fx-background-color: red; -fx-border-color: black; -fx-alignment: center;"
                                        );
                                    }
                                }
                            }
                        }
                    });
                }
            }
        }
    }

    private void resetFields() {
        for (int i = 0; i < 9; i++) {
            for (int j = 0; j < 9; j++) {
                if (properties[i][j] != null) {
                    fields[i][j].textProperty().unbindBidirectional(properties[i][j]);
                    properties[i][j].dispose();
                    properties[i][j] = null;
                }
                fields[i][j].setText("");
                fields[i][j].setEditable(true);
                fields[i][j].setTextFormatter(null);
                fields[i][j].setStyle("-fx-background-color: white; -fx-border-color: black; -fx-alignment: center;");
            }
        }
    }

    private boolean fieldCorrect(int row, int col) {
        int value = board.getField(row, col);

        if (value == 0) {
            return true;
        }

        for (int j = 0; j < 9; j++) {
            if (j != col && board.getField(row, j) == value) {
                return false;
            }
        }

        for (int i = 0; i < 9; i++) {
            if (i != row && board.getField(i, col) == value) {
                return false;
            }
        }

        int startRow = (row / 3) * 3;
        int startCol = (col / 3) * 3;

        for (int i = startRow; i < startRow + 3; i++) {
            for (int j = startCol; j < startCol + 3; j++) {
                if (i == row && j == col) {
                    continue;
                }
                if (board.getField(i, j) == value) {
                    return false;
                }
            }
        }

        return true;
    }

    private String fileNameInput(boolean load) {

        FXMLLoader fxmlLoader;

        if (load) {
           fxmlLoader = new FXMLLoader(getClass().getResource("fileLoadForm.fxml"));
        } else {
           fxmlLoader = new FXMLLoader(getClass().getResource("fileNameForm.fxml"));
        }

        fxmlLoader.setResources(bundle);
        Parent dialogRoot;
        try {
            dialogRoot = fxmlLoader.load();
        } catch (IOException e) {
            throw new FxmlLoadException("error.fxmlLoadException", e);
        }
        Stage fileNameDialog = new Stage();
        fileNameDialog.setTitle(bundle.getString("file.title"));
        fileNameDialog.initModality(Modality.WINDOW_MODAL);
        fileNameDialog.initOwner(mainGrid.getScene().getWindow());

        Scene dialogScene = new Scene(dialogRoot);
        fileNameDialog.setScene(dialogScene);

        if (load) {
            FileLoadFormController formController = fxmlLoader.getController();
            formController.setStage(fileNameDialog);

            DaoFactory<SudokuBoard> factory = new JdbcSudokuBoardDaoFactory();

            try (Dao<SudokuBoard> dao = factory.getDao("savedBoards")) {

                List<String> savedNames = dao.names();
                if (savedNames == null || savedNames.isEmpty()) {
                    javafx.scene.control.Alert alert = new javafx.scene.control.Alert(javafx.scene.control.Alert.AlertType.INFORMATION);
                    alert.setTitle(bundle.getString("file.title"));
                    alert.setHeaderText(null);
                    alert.setContentText(bundle.getString("fileLoad.noFile"));
                    alert.showAndWait();
                    return null;
                }
                formController.fillChoiceBox(dao.names(), bundle, loadedFromMenu);
            } catch (Exception e) {
                Main.handleError(e, false, loadedFromMenu);
            }
            fileNameDialog.showAndWait();

            if (formController.isConfirmed()) {
                return formController.getFileName();
            }
        } else {
            FileNameFormController formController = fxmlLoader.getController();
            formController.setStage(fileNameDialog);
            fileNameDialog.showAndWait();

            if (formController.isConfirmed()) {
                return formController.getFileName();
            }
        }

        return null;
    }

    @FXML
    public boolean onLoadButtonClick() {

        String fileName = fileNameInput(true);

        if (fileName == null) {
            return false;
        }

        DaoFactory<SudokuBoard> factory = new JdbcSudokuBoardDaoFactory();

        try (Dao<SudokuBoard> daoS = factory.getDao("savedBoards");
             Dao<SudokuBoard> daoO = factory.getDao("originalBoards")) {

            board = daoS.read(fileName);
            originalBoard = daoO.read(fileName + "-original");
        } catch (Exception e) {
            Main.handleError(e, false, loadedFromMenu);
            loadedFromMenu = false;
            return false;
        }
        loadedFromMenu = false;
        loadBoard();
        return true;
    }

    @FXML
    public void onSaveButtonClick() {
        String fileName = fileNameInput(false);
        if (fileName == null) {
            return;
        }
        DaoFactory<SudokuBoard> factory = new JdbcSudokuBoardDaoFactory();

        try (Dao<SudokuBoard> daoS = factory.getDao("savedBoards");
             Dao<SudokuBoard> daoO = factory.getDao("originalBoards")) {

            daoS.write(fileName, board);
            daoO.write(fileName + "-original", originalBoard);
        } catch (Exception e) {
            Main.handleError(e, false);
        }
    }


}
