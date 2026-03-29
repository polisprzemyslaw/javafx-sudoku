package pl.comp.model.exceptions;


public class NullFieldException extends SudokuRuntimeException {

    public NullFieldException(String messageKey) {
        super(messageKey);
        this.messageKey = messageKey;
    }


}
