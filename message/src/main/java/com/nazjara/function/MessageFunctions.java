package com.nazjara.function;

import com.nazjara.dto.AccountsMessageDto;
import java.util.function.Function;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
@Slf4j
public class MessageFunctions {

  @Bean
  public Function<AccountsMessageDto, AccountsMessageDto> email() {
    return accountsMessageDto -> {
      log.info("Email function called with: {}", accountsMessageDto);
      return accountsMessageDto;
    };
  }

  @Bean
  public Function<AccountsMessageDto, Long> sms() {
    return accountsMessageDto -> {
      log.info("Sms function called with: {}", accountsMessageDto);
      return accountsMessageDto.accountNumber();
    };
  }
}
