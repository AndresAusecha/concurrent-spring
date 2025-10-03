package co.aamsis.concurrent.spring.dtos;

import java.util.List;

public record MapboxGeocodeSearchResponse(
        List<MapBoxFeature> features
) {
    public record MapBoxFeature(
            MapBoxGeometry geometry
    ) {
        public record MapBoxGeometry(
                List<Double> coordinates
        ) {
        }
    }
}

