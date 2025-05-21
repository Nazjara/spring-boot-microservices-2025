package com.nazjara.filter;

import java.util.UUID;
import lombok.experimental.UtilityClass;
import org.springframework.http.HttpHeaders;
import org.springframework.web.server.ServerWebExchange;

@UtilityClass
public class FilterUtils {

  public static final String CORRELATION_ID = "bank-correlation-id";

  public String getCorrelationId(HttpHeaders requestHeaders) {
    if (requestHeaders.get(CORRELATION_ID) != null) {
      return requestHeaders.get(CORRELATION_ID).stream().findFirst().get();
    } else {
      return null;
    }
  }

  public ServerWebExchange setCorrelationId(ServerWebExchange exchange, String correlationId) {
    return exchange.mutate()
        .request(exchange.getRequest().mutate().header(CORRELATION_ID, correlationId).build())
        .build();
  }

  public boolean isCorrelationIdPresent(HttpHeaders requestHeaders) {
    return getCorrelationId(requestHeaders) != null;
  }

  public String generateCorrelationId() {
    return UUID.randomUUID().toString();
  }
}
