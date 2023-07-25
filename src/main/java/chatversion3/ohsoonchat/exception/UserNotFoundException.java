package chatversion3.ohsoonchat.exception;


import chatversion3.ohsoonchat.error.exception.ErrorCode;
import chatversion3.ohsoonchat.error.exception.OhSoonException;

public class UserNotFoundException extends OhSoonException {

    public static final OhSoonException EXCEPTION = new UserNotFoundException();

    private UserNotFoundException() {
        super(ErrorCode.USER_NOT_FOUND);
    }
}
