package com.nazjara.function;

import com.nazjara.service.IAccountsService;
import java.util.function.Consumer;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
@RequiredArgsConstructor
public class AccountsFunctions {

  private final IAccountsService accountsService;

  @Bean
  public Consumer<Long> updateCommunication() {
    return accountNumber -> {
      log.info("Updating communication for account number: {}", accountNumber);
      accountsService.updateCommunicationSwitch(accountNumber);
    };
  }
}
