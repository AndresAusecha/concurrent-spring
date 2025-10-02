package co.aamsis.concurrent.spring.controllers;

import co.aamsis.concurrent.spring.dtos.ForecastBody;
import co.aamsis.concurrent.spring.dtos.OpenMeteoResponse;
import co.aamsis.concurrent.spring.services.WeatherForecastService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("api/v1/weather")
public class WeatherController {
    private final WeatherForecastService service;

    public WeatherController(WeatherForecastService service) {
        this.service = service;
    }

    @PostMapping("/forecast")
    ResponseEntity<List<OpenMeteoResponse>> forecastForAddress(
            @RequestBody ForecastBody forecastBody
    ) {
        return ResponseEntity.ok(this.service.forecastForAddress(forecastBody));
    }
}
