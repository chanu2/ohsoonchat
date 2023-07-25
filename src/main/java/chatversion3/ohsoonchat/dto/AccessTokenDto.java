package chatversion3.ohsoonchat.dto;

import lombok.Getter;

@Getter
public class AccessTokenDto {

    private String accessToken;

    public AccessTokenDto(String accessToken){
        this.accessToken = accessToken;
    }

}
