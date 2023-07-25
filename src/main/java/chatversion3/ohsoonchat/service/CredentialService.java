package chatversion3.ohsoonchat.service;

import chatversion3.ohsoonchat.dto.AccessTokenDto;
import chatversion3.ohsoonchat.exception.UserNotFoundException;
import chatversion3.ohsoonchat.model.Member;
import chatversion3.ohsoonchat.repo.UserRepository;
import chatversion3.ohsoonchat.security.JwtTokenProvider;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.security.NoSuchAlgorithmException;
import java.security.spec.InvalidKeySpecException;
import java.util.Optional;

@Service
@AllArgsConstructor
@Transactional
@Slf4j
public class CredentialService {


    private final UserRepository userRepository;
    private final JwtTokenProvider jwtTokenProvider;

    @Transactional
    public AccessTokenDto login(Long userId){
        Member user = userRepository.findById(userId).orElseThrow(() -> UserNotFoundException.EXCEPTION);
        String accessToken = jwtTokenProvider.generateAccessToken(userId, user.getAccountRole());
        return new AccessTokenDto(accessToken);
    }


}

