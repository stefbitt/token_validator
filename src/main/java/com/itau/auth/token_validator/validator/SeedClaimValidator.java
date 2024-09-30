package com.itau.auth.token_validator.validator;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import org.springframework.stereotype.Service;

@Service
public class SeedClaimValidator implements ClaimValidator {

    @Override
    public void validate(DecodedJWT jwt) throws InvalidTokenException {
        String seed = jwt.getClaim("Seed").asString();
        if (seed == null) {
            throw new InvalidTokenException("Seed claim is null");
        }

        try {
            long seedValue = Long.parseLong(seed);
            if (!isPrime(seedValue)) {
                throw new InvalidTokenException("Seed claim is not a prime number");
            }
        } catch (NumberFormatException e) {
            throw new InvalidTokenException("Seed claim is not a valid number", e);
        }
    }

    private boolean isPrime(long number) {
        if (number <= 1) return false;
        for (long i = 2; i <= Math.sqrt(number); i++) {
            if (number % i == 0) return false;
        }
        return true;
    }
}

