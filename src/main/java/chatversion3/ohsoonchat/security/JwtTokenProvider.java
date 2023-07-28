package chatversion3.ohsoonchat.security;


import chatversion3.ohsoonchat.common.AccountRole;
import chatversion3.ohsoonchat.exception.ExpiredTokenException;
import chatversion3.ohsoonchat.exception.InvalidTokenException;
import chatversion3.ohsoonchat.exception.RefreshTokenExpiredException;
import chatversion3.ohsoonchat.property.JwtProperties;
import chatversion3.ohsoonchat.security.auth.AuthDetails;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;
import org.springframework.messaging.simp.stomp.StompHeaderAccessor;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.nio.charset.StandardCharsets;
import java.security.Key;
import java.util.Date;

@RequiredArgsConstructor
@Component
public class JwtTokenProvider {

    private final JwtProperties jwtProperties;
    private final String ACCESS_TOKEN = "access_token";
    private final String REFRESH_TOKEN = "refresh_token";
    private final String ROLE = "role";
    private final String TYPE = "type";
    private final String ISSUER = "knockknock";

    public String resolveTokenWeb(StompHeaderAccessor accessor) {

        String rawHeader = accessor.getFirstNativeHeader(jwtProperties.getHeader());

        if (rawHeader != null
                && rawHeader.length() > jwtProperties.getPrefix().length()
                && rawHeader.startsWith(jwtProperties.getPrefix())) {
            return rawHeader.substring(jwtProperties.getPrefix().length() + 1);
        }
        return null;
    }


    public String resolveToken(HttpServletRequest request) {
        String rawHeader = request.getHeader(jwtProperties.getHeader());

        if (rawHeader != null
                && rawHeader.length() > jwtProperties.getPrefix().length()
                && rawHeader.startsWith(jwtProperties.getPrefix())) {
            return rawHeader.substring(jwtProperties.getPrefix().length() + 1);
        }
        return null;
    }

    public Authentication getAuthentication(String token) {
        String id = getJws(token).getBody().getSubject();
        String role = (String) getJws(token).getBody().get(ROLE);
        UserDetails userDetails = new AuthDetails(id, role);
        return new UsernamePasswordAuthenticationToken(
                userDetails, "", userDetails.getAuthorities());
    }

    public Jws<Claims> getJws(String token) {
        try {
            return Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
        } catch (ExpiredJwtException e) {
            throw ExpiredTokenException.EXCEPTION;
        } catch (Exception e) {
            throw InvalidTokenException.EXCEPTION;
        }
    }


    public void validateToken(final String token) {
        try {
           Jwts.parserBuilder().setSigningKey(getSecretKey()).build().parseClaimsJws(token);
        } catch (io.jsonwebtoken.security.SecurityException | MalformedJwtException e) {
            throw InvalidTokenException.EXCEPTION;
        } catch (ExpiredJwtException e) {
            throw ExpiredTokenException.EXCEPTION;
        } catch (UnsupportedJwtException e) {
            throw InvalidTokenException.EXCEPTION;
        } catch (IllegalArgumentException e) {
            throw InvalidTokenException.EXCEPTION;
        }
    }



    public String getUserId(String token){
        return getJws(token).getBody().getSubject();
    }

    private Key getSecretKey() {
        return Keys.hmacShaKeyFor(jwtProperties.getSecretKey().getBytes(StandardCharsets.UTF_8));
    }


    private String buildAccessToken(
            Long id, Date issuedAt, Date accessTokenExpiresIn, AccountRole accountRole) {
        final Key encodedKey = getSecretKey();
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(issuedAt)
                .setSubject(id.toString())
                .claim(TYPE, ACCESS_TOKEN)
                .claim(ROLE, accountRole.getValue())
                .setExpiration(accessTokenExpiresIn)
                .signWith(encodedKey)
                .compact();
    }

    private String buildRefreshToken(Long id, Date issuedAt, Date accessTokenExpiresIn) {
        final Key encodedKey = getSecretKey();
        return Jwts.builder()
                .setIssuer(ISSUER)
                .setIssuedAt(issuedAt)
                .setSubject(id.toString())
                .claim(TYPE, REFRESH_TOKEN)
                .setExpiration(accessTokenExpiresIn)
                .signWith(encodedKey)
                .compact();
    }

    public String generateAccessToken(Long id, AccountRole accountRole) {
        final Date issuedAt = new Date();
        final Date accessTokenExpiresIn =
                new Date(issuedAt.getTime() + jwtProperties.getAccessExp() * 1000);

        return buildAccessToken(id, issuedAt, accessTokenExpiresIn, accountRole);
    }

    public String generateRefreshToken(Long id) {
        final Date issuedAt = new Date();
        final Date refreshTokenExpiresIn =
                new Date(issuedAt.getTime() + jwtProperties.getRefreshExp() * 1000);
        return buildRefreshToken(id, issuedAt, refreshTokenExpiresIn);
    }

    public boolean isAccessToken(String token) {
        return getJws(token).getBody().get(TYPE).equals(ACCESS_TOKEN);
    }

    public boolean isRefreshToken(String token) {
        return getJws(token).getBody().get(TYPE).equals(REFRESH_TOKEN);
    }

    public Long parseAccessToken(String token) {
        if (isAccessToken(token)) {
            Claims claims = getJws(token).getBody();
            return Long.parseLong(claims.getSubject());
        }
        throw InvalidTokenException.EXCEPTION;
    }

    public Long parseRefreshToken(String token) {
        try {
            if (isRefreshToken(token)) {
                Claims claims = getJws(token).getBody();
                return Long.parseLong(claims.getSubject());
            }
        } catch (ExpiredTokenException e) {
            throw RefreshTokenExpiredException.EXCEPTION;
        }
        throw InvalidTokenException.EXCEPTION;
    }

    public Long getRefreshTokenTTlSecond() {
        return jwtProperties.getRefreshExp();
    }
}
