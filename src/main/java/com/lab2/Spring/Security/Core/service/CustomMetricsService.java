package com.lab2.Spring.Security.Core.service;

import io.micrometer.core.instrument.MeterRegistry;
import org.springframework.stereotype.Service;

@Service
public class CustomMetricsService {

    private final MeterRegistry meterRegistry;

    public CustomMetricsService(MeterRegistry meterRegistry) {
        this.meterRegistry = meterRegistry;
        meterRegistry.counter("custom.method.calls").increment();
    }

    public void someMethod() {
        meterRegistry.counter("custom.method.calls").increment();
    }
}
