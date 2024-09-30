package com.itau.auth.token_validator.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import com.itau.auth.token_validator.validator.ClaimValidator;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class ClaimsValidationService {

    @Autowired
    private List<ClaimValidator> validators;

    public void validateClaims(DecodedJWT jwt) throws InvalidTokenException {
        for (ClaimValidator validator : validators) {
            validator.validate(jwt);
        }
    }
}
