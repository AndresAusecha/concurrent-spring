package co.aamsis.concurrent.spring.dtos;

public record OpenMeteoResponse(
    double latitude,
    double longitude,
    double generationtime_ms,
    int utc_offset_seconds,
    String timezone,
    String timezone_abbreviation,
    double elevation
) {
}
