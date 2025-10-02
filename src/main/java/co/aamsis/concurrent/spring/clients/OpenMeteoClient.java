package co.aamsis.concurrent.spring.clients;

import co.aamsis.concurrent.spring.dtos.OpenMeteoResponse;
import com.google.gson.Gson;
import okhttp3.OkHttpClient;
import okhttp3.Request;
import okhttp3.Response;
import okhttp3.ResponseBody;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;


@Component
public class OpenMeteoClient {

    private final RestTemplate restTemplate;

    public OpenMeteoClient(RestTemplate restTemplate) {
        this.restTemplate = restTemplate;
    }

    public OpenMeteoResponse fetchForecastForLocation(double latitude, double longitude) {
        String basePath = "https://api.open-meteo.com/v1/forecast";

        String url = UriComponentsBuilder
                .fromUri(URI.create(basePath))
                .queryParam("latitude", latitude)
                .queryParam("longitude", longitude)
                .toUriString();

        try {
            return restTemplate.getForObject(url, OpenMeteoResponse.class);
        } catch (Exception e) {
            System.out.println("Error fetching forecast: " + e.getMessage());
            return new OpenMeteoResponse();
        }
    }
}
