package pl.comp.model.exceptions;


public class SerialReadException extends SudokuRuntimeException {

    public SerialReadException(String messageKey, Throwable cause) {
        super(messageKey, cause);
        this.messageKey = messageKey;
    }
}
