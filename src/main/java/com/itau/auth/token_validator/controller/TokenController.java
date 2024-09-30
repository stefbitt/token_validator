package com.itau.auth.token_validator.controller;

import com.itau.auth.token_validator.exception.InvalidTokenException;
import com.itau.auth.token_validator.model.TokenValidationResponse;
import com.itau.auth.token_validator.service.TokenValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1/token")
public class TokenController {
    @Autowired
    private TokenValidationService tokenValidationService;

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestBody String jwt) {
        try {
            boolean isValid = tokenValidationService.validate(jwt);
            return ResponseEntity.ok(isValid);
        } catch (InvalidTokenException e) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(false);
        }
    }
    }

