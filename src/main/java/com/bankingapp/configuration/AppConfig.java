package com.bankingapp.configuration;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties
@JsonIgnoreProperties
public class AppConfig {

    private Double criticalAmount;

    public Double getCriticalAmount() {
        return criticalAmount;
    }

    public void setCriticalAmount(Double criticalAmount) {
        this.criticalAmount = criticalAmount;
    }
}
