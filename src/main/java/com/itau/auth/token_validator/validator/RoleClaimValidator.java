package com.itau.auth.token_validator.validator;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import org.springframework.stereotype.Service;

import lombok.extern.slf4j.Slf4j;

@Slf4j
@Service
public class RoleClaimValidator implements ClaimValidator {

    @Override
    public void validate(DecodedJWT jwt) throws InvalidTokenException {
        log.debug("Validating Role claim.");

        String role = jwt.getClaim("Role").asString();
        if (role == null || (!"Admin".equals(role) && !"Member".equals(role) && !"External".equals(role))) {
            log.debug("Role claim is invalid or null: {}", role);
            throw new InvalidTokenException("Role claim is invalid or null");
        }

        log.debug("Role claim validated successfully.");
    }
}


