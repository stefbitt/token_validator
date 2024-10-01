package com.itau.auth.token_validator.unit.controller;

import com.itau.auth.token_validator.controller.TokenController;
import com.itau.auth.token_validator.exception.InvalidTokenException;
import com.itau.auth.token_validator.service.TokenValidationService;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
@WebMvcTest(TokenController.class)
public class TokenControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private TokenValidationService tokenValidationService;

    @Test
    void shouldReturnTrue_WhenTokenIsValid() throws Exception {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiQWRtaW4iLCJTZWVkIjoiNzg0MSIsIk5hbWUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05sIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";

        when(tokenValidationService.validate(jwt)).thenReturn(true);

        mockMvc.perform(get("/api/v1/token/validate?jwt=" + jwt)
                        .content(jwt)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(true));
    }

    @Test
    void shouldReturnFalse_WhenTokenIsInvalid() throws Exception {
        String jwt = "eyJhbGciOiJIUzI1NiJ9.eyJSb2xlIjoiRXh0ZXJuYWwiLCJTZWVkIjoiODgwMzciLCJOYW1lIjoiTTRyaWEgT2xpdmlhIn0.6YD73XWZYQSSMDf6H0i3-kylz1-TY_Yt6h1cV2Ku-Qs";

        when(tokenValidationService.validate(jwt)).thenReturn(false);

        mockMvc.perform(get("/api/v1/token/validate?jwt=" + jwt)
                        .content(jwt)
                        .contentType("application/json"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").value(false));
    }

    @Test
    void shouldReturn400_WhenTokenIsMalformed() throws Exception {
        String jwt = "eyJhbGciOiJzI1NiJ9.dfsdfsfryJSr2xrIjoiQWRtaW4iLCJTZrkIjoiNzg0MSIsIk5hbrUiOiJUb25pbmhvIEFyYXVqbyJ9.QY05fsdfsIjtrcJnP533kQNk8QXcaleJ1Q01jWY_ZzIZuAg";

        when(tokenValidationService.validate(jwt)).thenThrow(new InvalidTokenException("Malformed token"));

        mockMvc.perform(get("/api/v1/token/validate?jwt=" + jwt)
                        .content(jwt)
                        .contentType("application/json"))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$").value(false));
    }
}


