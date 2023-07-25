package chatversion3.ohsoonchat.common;

import lombok.AllArgsConstructor;
import lombok.Getter;

@AllArgsConstructor
@Getter
public enum AccountRole {

    USER("USER"),
    ADMIN("ADMIN");

    private String value;
}
