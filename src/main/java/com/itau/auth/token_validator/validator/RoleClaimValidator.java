package com.itau.auth.token_validator.validator;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import org.springframework.stereotype.Service;

@Service
public class RoleClaimValidator implements ClaimValidator {

    @Override
    public void validate(DecodedJWT jwt) throws InvalidTokenException {
        String role = jwt.getClaim("Role").asString();
        if (role == null || (!"Admin".equals(role) && !"Member".equals(role) && !"External".equals(role))) {
            throw new InvalidTokenException("Role claim is invalid or null");
        }
    }
}

