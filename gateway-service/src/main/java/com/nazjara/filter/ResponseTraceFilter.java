package com.nazjara.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import reactor.core.publisher.Mono;

@Configuration
@Slf4j
public class ResponseTraceFilter {

  @Bean
  public GlobalFilter postGlobalFilter() {
    return (exchange, chain) ->
        chain.filter(exchange).then(Mono.fromRunnable(() -> {
          var requestHeaders = exchange.getRequest().getHeaders();
          var correlationId = FilterUtils.getCorrelationId(requestHeaders);
          log.info("Updated the correlation id to the outbound headers: {}", correlationId);
          exchange.getResponse().getHeaders().add(FilterUtils.CORRELATION_ID, correlationId);
        }));
  }
}
