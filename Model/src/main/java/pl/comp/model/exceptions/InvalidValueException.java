package pl.comp.model.exceptions;

import java.util.ResourceBundle;

public class InvalidValueException extends SudokuRuntimeException {

    private final int value;

    public InvalidValueException(String messageKey, int value) {
        super(messageKey);
        this.messageKey = messageKey;
        this.value = value;
    }

    @Override
    public String getLocalizedMessage() {
        ResourceBundle bundle = ResourceBundle.getBundle("errorsLocale");
        return bundle.getString(messageKey) + value;
    }
}
