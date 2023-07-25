package chatversion3.ohsoonchat.exception;


import chatversion3.ohsoonchat.error.exception.ErrorCode;
import chatversion3.ohsoonchat.error.exception.OhSoonException;

public class ExpiredTokenException extends OhSoonException {

    public static final OhSoonException EXCEPTION = new ExpiredTokenException();

    private ExpiredTokenException() {
        super(ErrorCode.EXPIRED_TOKEN);
    }
}
