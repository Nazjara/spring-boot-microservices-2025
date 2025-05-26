package com.nazjara.controller;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Mono;

@RestController
public class FallbackController {

  @GetMapping("/fallback")
  public Mono<String> contactSupport() {
    return Mono.just("An error occurred. Please contact support.");
  }
}
