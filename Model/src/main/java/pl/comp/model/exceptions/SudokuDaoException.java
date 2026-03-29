package pl.comp.model.exceptions;

import java.util.ResourceBundle;

public class SudokuDaoException extends Exception {

    private final String messageKey;

    public SudokuDaoException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }

    public SudokuDaoException(String messageKey, Throwable cause) {
        super(messageKey, cause);
        this.messageKey = messageKey;
    }

    @Override
    public String getLocalizedMessage() {
        ResourceBundle bundle = ResourceBundle.getBundle("errorsLocale");
        return bundle.getString(messageKey);
    }
}