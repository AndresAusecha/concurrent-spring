package co.aamsis.concurrent.spring.services;

import co.aamsis.concurrent.spring.clients.MapboxClient;
import co.aamsis.concurrent.spring.clients.OpenMeteoClient;
import co.aamsis.concurrent.spring.dtos.ForecastBody;
import co.aamsis.concurrent.spring.dtos.MapboxGeocodeSearchResponse;
import co.aamsis.concurrent.spring.dtos.OpenMeteoResponse;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;
import java.util.function.Supplier;
import java.util.stream.Collectors;

@Service
public class WeatherForecastService {
    private final MapboxClient mapboxClient;
    private final OpenMeteoClient openMeteoClient;
    private final ExecutorService executorService;

    public WeatherForecastService(
            MapboxClient mapboxClient,
            OpenMeteoClient openMeteoClient,
            ExecutorService executorService
    ) {
        this.mapboxClient = mapboxClient;
        this.openMeteoClient = openMeteoClient;
        this.executorService = executorService;
    }

    private String sanitizeAddress(String address) {
        return address.replace(" ", "+");
    }

    public List<OpenMeteoResponse> forecastForAddress(ForecastBody forecastBody) {
        List<CompletableFuture<MapboxGeocodeSearchResponse>> geocodeFutures
                = forecastBody.addresses.stream().map((address) ->
                CompletableFuture
                        .supplyAsync(
                                () -> this
                                .mapboxClient
                                .getGeocodeForLocation(
                                        this.sanitizeAddress(address.getAddress())+address.getPostalCode()),
                                executorService
                        )
        ).toList();

        CompletableFuture.allOf(geocodeFutures.toArray(CompletableFuture[]::new)).join();

        var locationForecastFutures = geocodeFutures.stream().map((future) -> {
            MapboxGeocodeSearchResponse result = future.join();
            var geometry = result.getFeatures().getFirst().getGeometry().getCoordinates();

            return CompletableFuture.supplyAsync(
                    () -> openMeteoClient.fetchForecastForLocation(geometry.getLast(), geometry.getFirst()),
                    executorService
            ).exceptionally(ex -> {
                System.out.println("Something failed");
                System.out.print(ex.getMessage());

                return new OpenMeteoResponse();
            });
        }).toList();

        CompletableFuture.allOf(locationForecastFutures.toArray(CompletableFuture[]::new)).join();

        return locationForecastFutures.stream().map(CompletableFuture::join).toList();
    }
}
