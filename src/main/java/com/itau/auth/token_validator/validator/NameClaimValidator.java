package com.itau.auth.token_validator.validator;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class NameClaimValidator implements ClaimValidator {

    @Override
    public void validate(DecodedJWT jwt) throws InvalidTokenException {
        log.debug("Validating Name claim.");

        String name = jwt.getClaim("Name").asString();
        if (name == null || name.matches(".*\\d.*")) {
            log.debug("Name claim contains numbers or is null: {}", name);
            throw new InvalidTokenException("Name claim contains numbers or is null");
        }

        log.debug("Name claim validated successfully.");
    }
}
