package chatversion3.ohsoonchat.error.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public class OhSoonException extends RuntimeException{

    private ErrorCode errorCode;
}
