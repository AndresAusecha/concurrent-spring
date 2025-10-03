package co.aamsis.concurrent.spring.config;

import org.springframework.boot.restclient.RestTemplateBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.ClientHttpRequestInterceptor;
import org.springframework.http.client.ClientHttpResponse;
import org.springframework.web.client.ResponseErrorHandler;
import org.springframework.web.client.RestTemplate;
import java.io.IOException;
import java.time.Duration;
import java.util.ArrayList;
import java.util.List;

@Configuration
public class ClientsConfig {

    @Bean
    public RestTemplate restTemplate(RestTemplateBuilder builder) {
        // Add custom interceptors
        List<ClientHttpRequestInterceptor> interceptors = new ArrayList<>();
        interceptors.add(loggingInterceptor());

        return builder
                .connectTimeout(Duration.ofSeconds(5))   // connection timeout
                .readTimeout(Duration.ofSeconds(10))     // read timeout
                .additionalInterceptors(interceptors)
                .errorHandler(customErrorHandler())
                .messageConverters(new org.springframework.http.converter.json.JacksonJsonHttpMessageConverter())
                .build();
    }

    private ClientHttpRequestInterceptor loggingInterceptor() {
        return (request, body, execution) -> {
            System.out.println("➡️ Request: " + request.getMethod() + " " + request.getURI());
            ClientHttpResponse response = execution.execute(request, body);
            System.out.println("⬅️ Response: " + response.getStatusCode());
            return response;
        };
    }

    private ResponseErrorHandler customErrorHandler() {
        return new ResponseErrorHandler() {
            @Override
            public boolean hasError(ClientHttpResponse response) throws IOException {
                return response.getStatusCode().isError();
            }
        };
    }
}