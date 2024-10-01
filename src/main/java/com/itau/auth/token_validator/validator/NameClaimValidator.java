package com.itau.auth.token_validator.validator;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NameClaimValidator implements ClaimValidator {

    private static final int MAX_LENGTH = 256;

    @Override
    public void validate(DecodedJWT jwt) throws InvalidTokenException {
        log.debug("Validating Name claim.");

        String name = jwt.getClaim("Name").asString();
        if (name == null || name.matches(".*\\d.*")) {
            log.debug("Name claim contains numbers or is null: {}", name);
            throw new InvalidTokenException("Name claim contains numbers or is null");
        }

        if (name.length() > MAX_LENGTH) {
            log.debug("Name claim contains more than 256 characters: {}", name);
            throw new InvalidTokenException("Name claim contains more than 256 characters");
        }

        log.debug("Name claim validated successfully.");
    }
}
