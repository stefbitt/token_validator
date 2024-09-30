package com.itau.auth.token_validator.validator;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
public class RoleClaimValidatorTest {

    @Mock
    private DecodedJWT decodedJWT;

    @Mock
    private Claim claim;

    @InjectMocks
    private RoleClaimValidator roleClaimValidator;

    @Test
    void shouldValidateRoleSuccessfully_WhenRoleIsValid() {

        when(decodedJWT.getClaim("Role")).thenReturn(claim);
        when(claim.asString()).thenReturn("Admin");

        roleClaimValidator.validate(decodedJWT);
    }

    @Test
    void shouldThrowInvalidTokenException_WhenRoleIsInvalid() {

        when(decodedJWT.getClaim("Role")).thenReturn(claim);
        when(claim.asString()).thenReturn("InvalidRole");

        assertThatThrownBy(() -> roleClaimValidator.validate(decodedJWT))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Role claim is invalid or null");
    }

    @Test
    void shouldThrowInvalidTokenException_WhenRoleIsNull() {

        when(decodedJWT.getClaim("Role")).thenReturn(claim);
        when(claim.asString()).thenReturn(null);

        assertThatThrownBy(() -> roleClaimValidator.validate(decodedJWT))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Role claim is invalid or null");
    }
}
