package com.itau.auth.token_validator.validator;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class RootClaimValidator implements ClaimValidator {

    @Override
    public void validate(DecodedJWT jwt) throws InvalidTokenException {
        log.debug("Validating Root claim.");

        if (jwt.getClaims().size() != 3) {
            log.debug("JWT must contain exactly 3 Claims: {}", jwt.getClaims().size());
            throw new InvalidTokenException("JWT must contain exactly 3 Claims");
        }

        log.debug("Role claim validated successfully.");
    }
}
