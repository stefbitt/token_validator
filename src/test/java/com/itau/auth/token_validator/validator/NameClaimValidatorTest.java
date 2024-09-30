package com.itau.auth.token_validator.validator;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class NameClaimValidatorTest {

    @InjectMocks
    private NameClaimValidator nameClaimValidator;

    @Mock
    private DecodedJWT jwt;

    @Mock
    private Claim nameClaim;

    @Test
    void shouldValidateNameSuccessfully_WhenNameIsValid() {
        when(jwt.getClaim("Name")).thenReturn(nameClaim);
        when(nameClaim.asString()).thenReturn("John Doe");

        nameClaimValidator.validate(jwt);
    }

    @Test
    void shouldThrowInvalidTokenException_WhenNameContainsNumbers() {
        when(jwt.getClaim("Name")).thenReturn(nameClaim);
        when(nameClaim.asString()).thenReturn("John123");

        assertThatThrownBy(() -> nameClaimValidator.validate(jwt))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Name claim contains numbers or is null");
    }

    @Test
    void shouldThrowInvalidTokenException_WhenNameIsNull() {
        when(jwt.getClaim("Name")).thenReturn(nameClaim);
        when(nameClaim.asString()).thenReturn(null);

        assertThatThrownBy(() -> nameClaimValidator.validate(jwt))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Name claim contains numbers or is null");
    }
}
