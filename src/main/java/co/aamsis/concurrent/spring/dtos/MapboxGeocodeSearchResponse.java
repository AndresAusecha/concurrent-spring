package co.aamsis.concurrent.spring.dtos;

import java.util.List;

public class MapboxGeocodeSearchResponse {
    List<MapBoxFeature> features;

    public class MapBoxGeometry {
        List<Double> coordinates;

        public List<Double> getCoordinates() {
            return coordinates;
        }
    }

    public class MapBoxFeature {
        MapBoxGeometry geometry;

        public MapBoxGeometry getGeometry() {
            return geometry;
        }
    }

    public List<MapBoxFeature> getFeatures() {
        return features;
    }
}

