package pl.comp.view.exceptions;


import java.util.ResourceBundle;

public class FxmlLoadException extends RuntimeException {
    private final String messageKey;

    public FxmlLoadException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }

    public FxmlLoadException(String messageKey, Throwable cause) {
        super(messageKey, cause);
        this.messageKey = messageKey;
    }

    @Override
    public String getLocalizedMessage() {
        ResourceBundle bundle = ResourceBundle.getBundle("pl.comp.view.locale");
        return bundle.getString(messageKey);
    }
}
