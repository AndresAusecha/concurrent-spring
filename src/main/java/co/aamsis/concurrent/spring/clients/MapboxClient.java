package co.aamsis.concurrent.spring.clients;

import co.aamsis.concurrent.spring.dtos.MapboxGeocodeSearchResponse;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.client.RestTemplate;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;

@Component
public class MapboxClient {

    private final RestTemplate restTemplate;
    private final String baseUrl;
    private final String token;

    public MapboxClient(
            RestTemplate restTemplate,
            @Value("${apis.mapbox.baseUrl}") String baseUrl,
            @Value("${apis.mapbox.apiToken}") String token
    ) {
        this.restTemplate = restTemplate;
        this.baseUrl = baseUrl;
        this.token = token;
    }

    public MapboxGeocodeSearchResponse getGeocodeForLocation(String location) {
        String url = UriComponentsBuilder
                .fromUri(URI.create(baseUrl + "/search/geocode/v6/forward"))
                .queryParam("q", location)
                .queryParam("access_token", token)
                .toUriString();

        return restTemplate.getForObject(url, MapboxGeocodeSearchResponse.class);
    }
}
