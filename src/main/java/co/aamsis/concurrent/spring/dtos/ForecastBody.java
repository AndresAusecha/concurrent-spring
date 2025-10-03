package co.aamsis.concurrent.spring.dtos;

import java.util.List;

public record ForecastBody(
        List<ForecastAddress> addresses
) {
    public record ForecastAddress(String address, String postalCode) {
    }
}