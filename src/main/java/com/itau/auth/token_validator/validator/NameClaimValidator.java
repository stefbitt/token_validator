package com.itau.auth.token_validator.validator;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import org.springframework.stereotype.Service;

@Service
public class NameClaimValidator implements ClaimValidator {

    @Override
    public void validate(DecodedJWT jwt) throws InvalidTokenException {
        String name = jwt.getClaim("Name").asString();
        if (name == null || name.matches(".*\\d.*")) {
            throw new InvalidTokenException("Name claim contains numbers or is null");
        }
    }
}
