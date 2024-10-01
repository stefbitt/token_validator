package com.itau.auth.token_validator.controller;

import com.itau.auth.token_validator.exception.InvalidTokenException;
import com.itau.auth.token_validator.service.TokenValidationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/v1/token")
public class TokenController {

    @Autowired
    private TokenValidationService tokenValidationService;

    @PostMapping("/validate")
    public ResponseEntity<Boolean> validateToken(@RequestBody String jwt) {
        try {
            boolean isValid = tokenValidationService.validate(jwt);
            return new ResponseEntity<>(isValid, HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(false, HttpStatus.BAD_REQUEST);
        }
    }
}

