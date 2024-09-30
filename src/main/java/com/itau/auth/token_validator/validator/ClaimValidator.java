package com.itau.auth.token_validator.validator;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;

public interface ClaimValidator {
    void validate(DecodedJWT jwt) throws InvalidTokenException;
}
