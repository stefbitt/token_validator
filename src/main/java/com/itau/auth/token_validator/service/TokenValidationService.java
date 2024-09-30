package com.itau.auth.token_validator.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import io.jsonwebtoken.*;
import org.springframework.stereotype.Service;

@Service
public class TokenValidationService {

    public boolean validate(String jwt) throws InvalidTokenException {
        try {
            DecodedJWT claims = getDecodedJWT(jwt);

            validateClaims(claims);

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

    private void validateClaims(DecodedJWT decodedJWT) throws InvalidTokenException {
        // Validação de 3 reivindicações específicas
        if (decodedJWT.getClaims().size() != 3) {
            throw new InvalidTokenException("JWT must contain exactly 3 decodedJWT");
        }

        // Validação do nome da reivindicação não contendo números
        String name = decodedJWT.getClaim("Name").asString();
        if (name == null || name.matches(".*\\d.*")) {
            throw new InvalidTokenException("Name claim contains numbers");
        }

        // Validação do valor da reivindicação de função
        String role = decodedJWT.getClaim("Role").asString();
        if (!"Admin".equals(role) && !"Member".equals(role) && !"External".equals(role)) {
            throw new InvalidTokenException("Role claim is invalid");
        }

        // Validação da semente ser um número primo
        String seed = decodedJWT.getClaim("Seed").asString();
        if (seed == null || !isPrime(Long.parseLong(seed))) {
            throw new InvalidTokenException("Seed claim is not a prime number");
        }
    }

    private boolean isPrime(long number) {
        if (number <= 1) return false;
        for (long i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }
        return true;
    }
}
