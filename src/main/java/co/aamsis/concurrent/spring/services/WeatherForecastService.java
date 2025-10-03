package co.aamsis.concurrent.spring.services;

import co.aamsis.concurrent.spring.clients.MapboxClient;
import co.aamsis.concurrent.spring.clients.OpenMeteoClient;
import co.aamsis.concurrent.spring.dtos.ForecastBody;
import co.aamsis.concurrent.spring.dtos.MapboxGeocodeSearchResponse;
import co.aamsis.concurrent.spring.dtos.OpenMeteoResponse;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.concurrent.CompletableFuture;
import java.util.concurrent.ExecutorService;

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
        //use ExecutorService to create parallel requests to the APIs using virtual threads
        //all the requests are triggered at the same time
        List<CompletableFuture<MapboxGeocodeSearchResponse>> geocodeFutures;
        try {
            geocodeFutures = forecastBody.addresses().stream().map((address) ->
                    CompletableFuture
                            .supplyAsync(
                                    () -> this
                                            .mapboxClient
                                            .getGeocodeForLocation(
                                                    this.sanitizeAddress(address.address()) + address.postalCode()),
                                    executorService
                            ).exceptionally(ex -> {
                                System.out.println("Something failed");
                                throw new RuntimeException(ex.getMessage());
                            })
            ).toList();
        } catch (Exception e) {
            System.out.print(e.getMessage());
            return new ArrayList<>();
        }

        // wait for all the futures to complete the invocation
        CompletableFuture.allOf(geocodeFutures.toArray(CompletableFuture[]::new)).join();

        //same pattern here with the second bundle of requests
        var locationForecastFutures = geocodeFutures.stream().map((future) -> {
            MapboxGeocodeSearchResponse result = future.join();
            var geometry = result.features().getFirst().geometry().coordinates();

            return CompletableFuture.supplyAsync(
                    () -> openMeteoClient.fetchForecastForLocation(geometry.getLast(), geometry.getFirst()),
                    executorService
            ).exceptionally(ex -> {
                System.out.println("Something failed");
                System.out.print(ex.getMessage());

                return new OpenMeteoResponse(-1, -1, 0, 0, "", "", 0);
            });
        }).toList();

        CompletableFuture.allOf(locationForecastFutures.toArray(CompletableFuture[]::new)).join();

        return locationForecastFutures.stream().map(CompletableFuture::join).toList();
    }
}
