package com.itau.auth.token_validator.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class TokenValidationService {

    @Autowired
    private ClaimsValidationService claimsValidationService;

    public boolean validate(String jwt) throws InvalidTokenException {
        try {
            DecodedJWT claims = getDecodedJWT(jwt);

            claimsValidationService.validateClaims(claims);

            return true;
        } catch (SignatureException e) {
            throw new InvalidTokenException("Invalid JWT signature");
        } catch (Exception e) {
            throw new InvalidTokenException("Invalid JWT token");
        }
    }

    public DecodedJWT getDecodedJWT(String token) {
        try {
            return JWT.decode(token);
        } catch (JWTDecodeException e) {
            throw new RuntimeException("Assinatura JWT inválida", e);
        } catch (Exception e) {
            throw new RuntimeException("Não foi possível analisar o token JWT", e);
        }
    }
}
