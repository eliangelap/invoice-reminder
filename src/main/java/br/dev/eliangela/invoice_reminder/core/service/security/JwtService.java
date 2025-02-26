package br.dev.eliangela.invoice_reminder.core.service.security;

import java.util.Date;
import java.util.function.Function;

import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.interfaces.DecodedJWT;

@Service
public class JwtService {

    public String extractSubject(String token) {
        return extractClaim(token, DecodedJWT::getSubject);
    }

    public boolean isTokenValid(String token) {
        return !isTokenExpired(token);
    }

    private <T> T extractClaim(String token, Function<DecodedJWT, T> claimsResolvers) {
        final DecodedJWT claims = extractAllClaims(token);
        return claimsResolvers.apply(claims);
    }

    private boolean isTokenExpired(String token) {
        return extractExpiration(token).before(new Date());
    }

    public Date extractExpiration(String token) {
        return extractClaim(token, DecodedJWT::getExpiresAt);
    }

    private DecodedJWT extractAllClaims(String token) {
        JWT jwt = new JWT();
        return jwt.decodeJwt(token);
    }

}
