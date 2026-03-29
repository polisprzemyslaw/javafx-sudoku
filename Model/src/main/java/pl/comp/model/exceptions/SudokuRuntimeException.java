package pl.comp.model.exceptions;

import java.util.ResourceBundle;

public class SudokuRuntimeException extends RuntimeException {
    protected String messageKey;

    public SudokuRuntimeException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }

    public SudokuRuntimeException(String messageKey, Throwable cause) {
        super(messageKey, cause);
        this.messageKey = messageKey;
    }

    @Override
    public String getLocalizedMessage() {
        ResourceBundle bundle = ResourceBundle.getBundle("errorsLocale");
        return bundle.getString(messageKey);
    }
}
