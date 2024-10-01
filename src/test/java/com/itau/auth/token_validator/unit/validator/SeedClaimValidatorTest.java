package com.itau.auth.token_validator.unit.validator;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import com.itau.auth.token_validator.validator.SeedClaimValidator;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class SeedClaimValidatorTest {

    @InjectMocks
    private SeedClaimValidator seedClaimValidator;

    @Mock
    private DecodedJWT jwt;

    @Mock
    private Claim seedClaim;

    @Test
    void shouldValidateSeedSuccessfully_WhenSeedIsPrime() {
        when(jwt.getClaim("Seed")).thenReturn(seedClaim);
        when(seedClaim.asString()).thenReturn("7");

        seedClaimValidator.validate(jwt);
    }

    @Test
    void shouldThrowInvalidTokenException_WhenSeedIsNotPrime() {
        when(jwt.getClaim("Seed")).thenReturn(seedClaim);
        when(seedClaim.asString()).thenReturn("8");

        assertThatThrownBy(() -> seedClaimValidator.validate(jwt))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Seed claim is not a prime number");
    }

    @Test
    void shouldThrowInvalidTokenException_WhenSeedIsNull() {
        when(jwt.getClaim("Seed")).thenReturn(seedClaim);
        when(seedClaim.asString()).thenReturn(null);

        assertThatThrownBy(() -> seedClaimValidator.validate(jwt))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Seed claim is null");
    }

    @Test
    void shouldThrowInvalidTokenException_WhenSeedIsNotANumber() {
        when(jwt.getClaim("Seed")).thenReturn(seedClaim);
        when(seedClaim.asString()).thenReturn("NotANumber");

        assertThatThrownBy(() -> seedClaimValidator.validate(jwt))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Seed claim is not a valid number");
    }
}

