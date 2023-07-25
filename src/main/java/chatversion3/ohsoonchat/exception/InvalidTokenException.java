package chatversion3.ohsoonchat.exception;


import chatversion3.ohsoonchat.error.exception.ErrorCode;
import chatversion3.ohsoonchat.error.exception.OhSoonException;

public class InvalidTokenException extends OhSoonException {

    public static final OhSoonException EXCEPTION = new InvalidTokenException();

    private InvalidTokenException() {
        super(ErrorCode.INVALID_TOKEN);
    }
}
