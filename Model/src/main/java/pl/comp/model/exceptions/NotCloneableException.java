package pl.comp.model.exceptions;


public class NotCloneableException extends SudokuRuntimeException {


    public NotCloneableException(String messageKey, Throwable cause) {
        super(messageKey, cause);
        this.messageKey = messageKey;
    }


}
