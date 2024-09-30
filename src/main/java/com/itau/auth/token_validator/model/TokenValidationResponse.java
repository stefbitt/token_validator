package com.itau.auth.token_validator.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
@AllArgsConstructor
public class TokenValidationResponse {
        private Boolean valid;
        private String message;
}
