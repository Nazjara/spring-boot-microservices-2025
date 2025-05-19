package com.nazjara.dto;

import lombok.Data;

@Data
public class CustomerDetailsDto {

  private String name;
  private String email;
  private String mobileNumber;
  private AccountsDto accountsDto;
  private CardsDto cardsDto;
  private LoansDto loansDto;
}
