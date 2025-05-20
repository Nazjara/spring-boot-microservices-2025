package com.nazjara;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.cloud.gateway.route.RouteLocator;
import org.springframework.cloud.gateway.route.builder.RouteLocatorBuilder;
import org.springframework.context.annotation.Bean;

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
            .filters(filter -> filter.rewritePath("/bank/accounts/(?<segment>.*)", "/${segment}"))
            .uri("lb://ACCOUNTS")) // uppercase service name from Eureka
        .route("cards", path -> path
            .path("/bank/cards/**")
            .filters(filter -> filter.rewritePath("/bank/cards/(?<segment>.*)", "/${segment}"))
            .uri("lb://CARDS")) // uppercase service name from Eureka
        .route("loans", path -> path
            .path("/bank/loans/**")
            .filters(filter -> filter.rewritePath("/bank/loans/(?<segment>.*)", "/${segment}"))
            .uri("lb://LOANS")) // uppercase service name from Eureka
        .build();
  }
}
