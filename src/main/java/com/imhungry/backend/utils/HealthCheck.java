package com.imhungry.backend.utils;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

/**
 * Created by calebthomas on 3/7/19.
 */
@RestController
@RequestMapping("/health")
public class HealthCheck {

    @GetMapping
    public String getHealthCheck() {
        return "healthy";
    }
}
