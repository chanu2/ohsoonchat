package chatversion3.ohsoonchat.exception;


import chatversion3.ohsoonchat.error.exception.ErrorCode;
import chatversion3.ohsoonchat.error.exception.OhSoonException;

public class RefreshTokenExpiredException extends OhSoonException {
    public static final OhSoonException EXCEPTION = new RefreshTokenExpiredException();

    private RefreshTokenExpiredException() {
        super(ErrorCode.REGISTER_EXPIRED_TOKEN);
    }
}
