package com.nazjara.service;

import com.nazjara.constant.CardsConstants;
import com.nazjara.dto.CardsDto;
import com.nazjara.entity.Cards;
import com.nazjara.exception.CardAlreadyExistsException;
import com.nazjara.exception.ResourceNotFoundException;
import com.nazjara.mapper.CardsMapper;
import com.nazjara.repository.CardsRepository;
import java.util.Random;
import lombok.AllArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@AllArgsConstructor
public class CardsServiceImpl implements ICardsService {

  private CardsRepository cardsRepository;

  /**
   * @param mobileNumber - Mobile Number of the Customer
   */
  @Override
  public void createCard(String mobileNumber) {
    var optionalCards = cardsRepository.findByMobileNumber(mobileNumber);
    if (optionalCards.isPresent()) {
      throw new CardAlreadyExistsException(
          "Card already registered with given mobileNumber " + mobileNumber);
    }
    cardsRepository.save(createNewCard(mobileNumber));
  }

  /**
   * @param mobileNumber - Mobile Number of the Customer
   * @return the new card details
   */
  private Cards createNewCard(String mobileNumber) {
    var newCard = new Cards();
    var randomCardNumber = 100000000000L + new Random().nextInt(900000000);
    newCard.setCardNumber(Long.toString(randomCardNumber));
    newCard.setMobileNumber(mobileNumber);
    newCard.setCardType(CardsConstants.CREDIT_CARD);
    newCard.setTotalLimit(CardsConstants.NEW_CARD_LIMIT);
    newCard.setAmountUsed(0);
    newCard.setAvailableAmount(CardsConstants.NEW_CARD_LIMIT);
    return newCard;
  }

  /**
   * @param mobileNumber - Input mobile Number
   * @return Card Details based on a given mobileNumber
   */
  @Override
  public CardsDto fetchCard(String mobileNumber) {
    var cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
        () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
    );
    return CardsMapper.mapToCardsDto(cards, new CardsDto());
  }

  /**
   * @param cardsDto - CardsDto Object
   * @return boolean indicating if the update of card details is successful or not
   */
  @Override
  public boolean updateCard(CardsDto cardsDto) {
    var cards = cardsRepository.findByCardNumber(cardsDto.getCardNumber()).orElseThrow(
        () -> new ResourceNotFoundException("Card", "CardNumber", cardsDto.getCardNumber()));
    CardsMapper.mapToCards(cardsDto, cards);
    cardsRepository.save(cards);
    return true;
  }

  /**
   * @param mobileNumber - Input MobileNumber
   * @return boolean indicating if the delete of card details is successful or not
   */
  @Override
  public boolean deleteCard(String mobileNumber) {
    var cards = cardsRepository.findByMobileNumber(mobileNumber).orElseThrow(
        () -> new ResourceNotFoundException("Card", "mobileNumber", mobileNumber)
    );
    cardsRepository.deleteById(cards.getCardId());
    return true;
  }
}
