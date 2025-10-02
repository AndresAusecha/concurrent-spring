package co.aamsis.concurrent.spring.dtos;

import java.util.List;

class MapBoxGeometry {
    List<Double> coordinates;
}

class MapBoxFeature {
    MapBoxGeometry geometry;
}


public class MapboxGeocodeSearchResponse {
    List<MapBoxFeature> features;
}

