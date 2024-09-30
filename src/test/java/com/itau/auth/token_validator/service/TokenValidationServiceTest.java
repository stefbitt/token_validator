package com.itau.auth.token_validator.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class TokenValidationServiceTest {

    @InjectMocks
    private TokenValidationService tokenValidationService;

    @Mock
    private ClaimsValidationService claimsValidationService;

    @Mock
    private DecodedJWT jwt;

    @Test
    void shouldValidateTokenSuccessfully() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";

        doNothing().when(claimsValidationService).validateClaims(any());

        boolean isValid = tokenValidationService.validate(token);

        assertThat(isValid).isTrue();
        verify(claimsValidationService).validateClaims(any(DecodedJWT.class));
    }

    @Test
    void shouldThrowExceptionWhenClaimsValidationFails() {
        String token = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiODgwMzciLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.6YD73XWZYQSSMDf6H0i3-kylz1-TY_Yt6h1cV2Ku-Qs";

        doThrow(new InvalidTokenException("Claims validation failed"))
                .when(claimsValidationService).validateClaims(any(DecodedJWT.class));

        assertThatThrownBy(() -> tokenValidationService.validate(token))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Claims validation failed");

        verify(claimsValidationService).validateClaims(any(DecodedJWT.class));
    }

    @Test
    void shouldThrowExceptionWhenTokenIsMalformed() {
        String malformedToken = "malformed.jwt.token";

        assertThatThrownBy(() -> tokenValidationService.validate(malformedToken))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Invalid JWT");
    }
}

