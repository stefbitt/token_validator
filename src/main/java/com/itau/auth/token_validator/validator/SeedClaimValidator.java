package com.itau.auth.token_validator.validator;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class SeedClaimValidator implements ClaimValidator {

    @Override
    public void validate(DecodedJWT jwt) throws InvalidTokenException {
        log.debug("Validating Name claim.");

        String seed = jwt.getClaim("Seed").asString();
        if (seed == null) {
            log.debug("Seed claim is null");
            throw new InvalidTokenException("Seed claim is null");
        }

        try {
            long seedValue = Long.parseLong(seed);
            if (!isPrime(seedValue)) {
                log.debug("Seed claim is not a prime number");
                throw new InvalidTokenException("Seed claim is not a prime number");
            }
        } catch (NumberFormatException e) {
            log.debug("Seed claim is not a valid number");
            throw new InvalidTokenException("Seed claim is not a valid number", e);
        }
    }

    private boolean isPrime(long number) {
        //TODO Cache com numeros primos já conhecidos
        if (number <= 1) return false;
        for (long i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }
        return true;
    }
}

