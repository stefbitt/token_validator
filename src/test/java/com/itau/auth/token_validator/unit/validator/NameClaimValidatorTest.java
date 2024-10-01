package com.itau.auth.token_validator.unit.validator;

import com.auth0.jwt.interfaces.Claim;
import com.auth0.jwt.interfaces.DecodedJWT;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import com.itau.auth.token_validator.validator.NameClaimValidator;
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

    @Test
    void shouldThrowInvalidTokenException_WhenNameIsMore256Characters() {
        when(jwt.getClaim("Name")).thenReturn(nameClaim);
        when(nameClaim.asString()).thenReturn("Lorem ipsum dolor sit amet. Ea mollitia voluptate id saepe magni est " +
                "quos eveniet ut omnis rerum sit animi perspiciatis. Sit Quis libero aut dolor dolore ut quia " +
                "aspernatur a ullam ipsam sit reprehenderit fuga ut nemo ullam non dolore autem. Et accusamus " +
                "ducimus eum omnis provident ea nesciunt nostrum aut corrupti provident ad odit dolorem aut " +
                "veritatis vero id consequuntur cumque! Eum rerum cupiditate est enim quasi et ipsam ipsa est " +
                "iste voluptatum. Et rerum quia eos sint autem non temporibus reprehenderit et illo impedit et " +
                "fugiat dolorum. Est nulla galisum est voluptas delectus At veniam consequatur non quisquam " +
                "ipsum qui aspernatur enim. Et sunt sunt molestiae eveniet ut consectetur repellendus ut " +
                "ratione minima ea quam odio sed dolorum molestiae. Eum perspiciatis voluptas ut excepturi " +
                "dolorem aut ullam neque vel inventore delectus id quaerat dolore est laborum sint.");

        assertThatThrownBy(() -> nameClaimValidator.validate(jwt))
                .isInstanceOf(InvalidTokenException.class)
                .hasMessageContaining("Name claim contains more than 256 characters");
    }
}
