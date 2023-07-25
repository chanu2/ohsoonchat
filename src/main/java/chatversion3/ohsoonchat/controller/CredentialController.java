package chatversion3.ohsoonchat.controller;

import chatversion3.ohsoonchat.dto.AccessTokenDto;
import chatversion3.ohsoonchat.service.CredentialService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.bind.annotation.*;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;

@RestController
@RequestMapping("/api/v1/credentials")
@RequiredArgsConstructor
@Slf4j
public class CredentialController {

    private final CredentialService credentialService;

    @PostMapping("/login2/{userId}")
    public AccessTokenDto login(@PathVariable("userId") Long userId){
        AccessTokenDto result = credentialService.login(userId);
        return result;
    }

}
