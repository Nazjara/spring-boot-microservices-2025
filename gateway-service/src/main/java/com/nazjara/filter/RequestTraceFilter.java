package com.nazjara.filter;

import lombok.extern.slf4j.Slf4j;
import org.springframework.cloud.gateway.filter.GatewayFilterChain;
import org.springframework.cloud.gateway.filter.GlobalFilter;
import org.springframework.stereotype.Component;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

@Slf4j
@Component
public class RequestTraceFilter implements GlobalFilter {

  @Override
  public Mono<Void> filter(ServerWebExchange exchange, GatewayFilterChain chain) {
    var requestHeaders = exchange.getRequest().getHeaders();
    if (FilterUtils.isCorrelationIdPresent(requestHeaders)) {
      log.info("bank-correlation-id found in RequestTraceFilter : {}",
          FilterUtils.getCorrelationId(requestHeaders));
    } else {
      var correlationID = FilterUtils.generateCorrelationId();
      exchange = FilterUtils.setCorrelationId(exchange, correlationID);
      log.info("bank-correlation-id generated in RequestTraceFilter : {}", correlationID);
    }
    return chain.filter(exchange);
  }

}
