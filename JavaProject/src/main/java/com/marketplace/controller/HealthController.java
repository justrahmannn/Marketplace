package com.marketplace.controller;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class HealthController {

    @RequestMapping(value = "/health", method = RequestMethod.HEAD)
    public ResponseEntity<Void> healthCheck() {
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @RequestMapping(value = "/health", method = RequestMethod.GET)
    public ResponseEntity<String> healthCheckGet() {
        return ResponseEntity.ok("OK");
    }
}
