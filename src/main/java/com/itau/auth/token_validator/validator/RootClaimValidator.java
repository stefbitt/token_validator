package com.itau.auth.token_validator.validator;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import org.springframework.stereotype.Service;

@Service
public class RootClaimValidator implements ClaimValidator {
    @Override
    public void validate(DecodedJWT jwt) throws InvalidTokenException {
        if (jwt.getClaims().size() != 3) {
            throw new InvalidTokenException("JWT must contain exactly 3 decodedJWT");
        }
    }
}
