package com.WebUI.WebUI.controller;

import com.WebUI.WebUI.services.SystemMetricsService;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.Map;

@RestController
@RequestMapping("/api/metrics")
@CrossOrigin(origins = "*")
public class MetricsController {

    private final SystemMetricsService systemMetricsService;

    public MetricsController(SystemMetricsService systemMetricsService) {
        this.systemMetricsService = systemMetricsService;
    }

    @GetMapping
    public Map<String, Object> getMetrics() {
        return systemMetricsService.fetchSystemMetrics();
    }
}
