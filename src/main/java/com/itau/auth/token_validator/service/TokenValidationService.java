package com.itau.auth.token_validator.service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.exceptions.JWTDecodeException;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import lombok.extern.log4j.Log4j2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
@Log4j2
public class TokenValidationService {

    @Autowired
    private ClaimsValidationService claimsValidationService;

    public boolean validate(String jwt) throws InvalidTokenException {
        log.info("Starting JWT validation.");

        try {
            DecodedJWT claims = getDecodedJWT(jwt);

            log.debug("JWT decoded successfully. Claims: {}", claims);

            claimsValidationService.validateClaims(claims);

            log.info("JWT validation passed.");

            return true;

        } catch (InvalidTokenException e) {
            log.error("JWT validation failed: {}", e.getMessage());
            throw e;
        }
    }

    public DecodedJWT getDecodedJWT(String token) {
        try {
            log.debug("Decoding JWT token.");
            return JWT.decode(token);
        } catch (JWTDecodeException e) {
            log.error("Invalid JWT", e);
            throw new InvalidTokenException("Invalid JWT", e);
        } catch (Exception e) {
            log.error("Unable to parse JWT token", e);
            throw new InvalidTokenException("Unable to parse JWT token", e);
        }
    }
}
