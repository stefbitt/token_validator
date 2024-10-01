package com.itau.auth.token_validator.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import com.itau.auth.token_validator.validator.ClaimValidator;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;

@Slf4j
@Service
public class ClaimsValidationService {

    private List<ClaimValidator> validators;

    public ClaimsValidationService(List<ClaimValidator> validators) {
        this.validators = validators;
    }

    public void validateClaims(DecodedJWT jwt) throws InvalidTokenException {
        log.info("Validating JWT claims.");

        for (ClaimValidator validator : validators) {
            log.info("Validating claim with {}", validator.getClass().getSimpleName());
            validator.validate(jwt);
        }

        log.info("All claims validated successfully.");
    }
}
