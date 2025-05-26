package com.nazjara;

import java.time.Duration;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.filter.ratelimit.KeyResolver;
import org.springframework.cloud.gateway.filter.ratelimit.RedisRateLimiter;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;
import org.springframework.http.HttpMethod;
import reactor.core.publisher.Mono;

@SpringBootApplication
public class GatewayServiceApplication {

  public static void main(String[] args) {
    SpringApplication.run(GatewayServiceApplication.class, args);
  }

  @Bean
  public RouteLocator routeLocator(RouteLocatorBuilder routeLocatorBuilder) {
    return routeLocatorBuilder.routes()
        .route("accounts", path -> path
            .path("/bank/accounts/**")
            .filters(filter -> filter.rewritePath("/bank/accounts/(?<segment>.*)", "/${segment}")
                .circuitBreaker(config ->
                    config.setName("accounts").setFallbackUri("forward:/fallback"))
                .retry(retryConfig ->
                    retryConfig.setRetries(3).setMethods(HttpMethod.GET)
                        .setExceptions(RuntimeException.class)
                        .setBackoff(Duration.ofMillis(100), Duration.ofMillis(1000), 2, false)))
            .uri("lb://ACCOUNTS")) // uppercase service name from Eureka
        .route("cards", path -> path
            .path("/bank/cards/**")
            .filters(filter -> filter.rewritePath("/bank/cards/(?<segment>.*)", "/${segment}")
                .circuitBreaker(config ->
                    config.setName("cards").setFallbackUri("forward:/fallback")))
            .uri("lb://CARDS")) // uppercase service name from Eureka
        .route("loans", path -> path
            .path("/bank/loans/**")
            .filters(filter -> filter.rewritePath("/bank/loans/(?<segment>.*)", "/${segment}")
                .circuitBreaker(config ->
                    config.setName("loans").setFallbackUri("forward:/fallback"))
                .requestRateLimiter(config ->
                    config.setRateLimiter(redisRateLimiter()).setKeyResolver(keyResolver())))
            .uri("lb://LOANS")) // uppercase service name from Eureka
        .build();
  }

  @Bean
  public RedisRateLimiter redisRateLimiter() {
    return new RedisRateLimiter(1, 1, 1);
  }

  @Bean
  public KeyResolver keyResolver() {
    return exchange ->
        Mono.justOrEmpty(exchange.getRequest().getHeaders().getFirst("user"))
            .defaultIfEmpty("anonymous");
  }
}
