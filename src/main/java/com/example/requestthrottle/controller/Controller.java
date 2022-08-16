package com.example.requestthrottle.controller;

import com.example.requestthrottle.annotation.Throttle;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class Controller {

    @Throttle
    @PostMapping
    public ResponseEntity<String> request() {
        return ResponseEntity.ok("");
    }
}
