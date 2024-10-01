package com.itau.auth.token_validator.unit.service;

import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import com.itau.auth.token_validator.service.ClaimsValidationService;
import com.itau.auth.token_validator.validator.ClaimValidator;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.lang.reflect.Field;
import java.util.Arrays;
import java.util.List;

import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.Mockito.*;
//@SpringBootTest
public class ClaimsValidationServiceTest {

    @InjectMocks
    private ClaimsValidationService claimsValidationService;

    @MockBean
    private DecodedJWT jwt;

    @MockBean
    private ClaimValidator nameClaimValidator;

    @MockBean
    private ClaimValidator roleClaimValidator;

    @MockBean
    private ClaimValidator seedClaimValidator;

    @BeforeEach
    void setUp() throws Exception {
        List<ClaimValidator> validators = Arrays.asList(nameClaimValidator, roleClaimValidator, seedClaimValidator);

        Field validatorsField = ClaimsValidationService.class.getDeclaredField("validators");
        validatorsField.setAccessible(true);
        validatorsField.set(claimsValidationService, validators);
    }

//    @Test
//    void shouldValidateAllClaimsSuccessfully() {
//        doNothing().when(nameClaimValidator).validate(jwt);
//        doNothing().when(roleClaimValidator).validate(jwt);
//        doNothing().when(seedClaimValidator).validate(jwt);
//
//        claimsValidationService.validateClaims(jwt);
//
//        verify(nameClaimValidator).validate(jwt);
//        verify(roleClaimValidator).validate(jwt);
//        verify(seedClaimValidator).validate(jwt);
//    }
//
//    @Test
//    void shouldThrowExceptionWhenAValidatorFails() {
//        doThrow(new InvalidTokenException("Name validation failed"))
//                .when(nameClaimValidator).validate(jwt);
//
//        //claimsValidationService = new ClaimsValidationService(List.of(nameClaimValidator, roleClaimValidator, seedClaimValidator));
//
//        assertThatThrownBy(() -> claimsValidationService.validateClaims(jwt))
//                .isInstanceOf(InvalidTokenException.class)
//                .hasMessageContaining("Name validation failed");
//
//        verify(nameClaimValidator).validate(jwt);
//        verify(roleClaimValidator, never()).validate(jwt);
//        verify(seedClaimValidator, never()).validate(jwt);
//    }
}
