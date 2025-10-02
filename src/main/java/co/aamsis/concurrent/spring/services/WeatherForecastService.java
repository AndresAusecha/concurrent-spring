package co.aamsis.concurrent.spring.services;

import co.aamsis.concurrent.spring.dtos.ForecastBody;
import co.aamsis.concurrent.spring.dtos.OpenMeteoResponse;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class WeatherForecastService {
    public WeatherForecastService() {

    }

    public List<OpenMeteoResponse> forecastForAddress(ForecastBody forecastBody) {

        return null;
    }
}
