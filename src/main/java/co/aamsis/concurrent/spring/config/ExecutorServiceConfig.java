package co.aamsis.concurrent.spring.config;

import org.springframework.boot.autoconfigure.condition.ConditionalOnThreading;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

import static org.springframework.boot.thread.Threading.VIRTUAL;

@Configuration
public class ExecutorServiceConfig {

    @Bean
    @ConditionalOnThreading(VIRTUAL)
    public ExecutorService virtualThreadExecutor(){
        return Executors.newVirtualThreadPerTaskExecutor();
    }
}
