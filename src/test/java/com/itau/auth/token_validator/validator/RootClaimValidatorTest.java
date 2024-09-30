package com.itau.auth.token_validator.validator;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Map;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RootClaimValidatorTest {

    @InjectMocks
    private RootClaimValidator rootClaimValidator;

    @Mock
    private DecodedJWT jwt;

    @Mock
    private Map<String, Claim> claims;

    @BeforeEach
    void setUp() {
        when(jwt.getClaims()).thenReturn(claims);
    }

    @Test
    void shouldValidateSuccessfully_WhenJWTContainsExactlyThreeClaims() {
        when(claims.size()).thenReturn(3);

        rootClaimValidator.validate(jwt);
    }

    @Test
    void shouldThrowInvalidTokenException_WhenJWTContainsMoreThanThreeClaims() {
        when(claims.size()).thenReturn(4);

        assertThatThrownBy(() -> rootClaimValidator.validate(jwt))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("JWT must contain exactly 3 Claims");
    }

    @Test
    void shouldThrowInvalidTokenException_WhenJWTContainsLessThanThreeClaims() {
        when(claims.size()).thenReturn(2);

        assertThatThrownBy(() -> rootClaimValidator.validate(jwt))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("JWT must contain exactly 3 Claims");
    }
}
