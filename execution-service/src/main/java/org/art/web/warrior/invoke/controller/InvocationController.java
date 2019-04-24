package org.art.web.warrior.invoke.controller;

import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static org.art.web.warrior.invoke.ServiceCommonConstants.INVOCATION_SERVICE_OK_MESSAGE;

@RestController
@RequestMapping("/execute")
public class InvocationController {

    @PostMapping
    public ResponseEntity executeClientCode() {
        return null;
    }

    @GetMapping("/ping")
    public String ping() {
        return INVOCATION_SERVICE_OK_MESSAGE;
    }
}
