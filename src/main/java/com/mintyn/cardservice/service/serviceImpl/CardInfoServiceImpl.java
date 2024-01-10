package com.mintyn.cardservice.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintyn.cardservice.apiCall.WebClientCall;
import com.mintyn.cardservice.entity.CardInfo;
import com.mintyn.cardservice.exceptions.DatabaseException;
import com.mintyn.cardservice.repository.CardInfoRepository;
import com.mintyn.cardservice.response.BinListResponse;
import com.mintyn.cardservice.response.CardInfoResponse;
import com.mintyn.cardservice.service.CardInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;
import java.util.Optional;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardInfoServiceImpl implements CardInfoService {

    private final WebClientCall webClientCall;

    private final ObjectMapper objectMapper;

    private final CardInfoRepository cardInfoRepository;


    @Override
    public CardInfoResponse verifyCard(String cardNumber)
    {
        //check if cardNumber exists in DB
        Optional<CardInfo> cardDetails;
        try
        {
            cardDetails = cardInfoRepository.findByCardNumber(cardNumber);
        }catch (Exception e)
        {
            throw new DatabaseException("UNABLE TO CONNECT TO THE CARD INFO DATABASE!");
        }


        CardInfo cardInfo = new CardInfo();

        CardInfoResponse cardInfoResponse = new CardInfoResponse();

        if (cardDetails.isPresent())
        {
            cardInfoResponse.setSuccess(true);
            cardInfoResponse.getPayload().setScheme(cardInfo.getScheme());
            cardInfoResponse.getPayload().setType(cardInfo.getType());
            cardInfoResponse.getPayload().setBank(cardInfo.getBank());

        } else
        {
            Map<String, Object> response = webClientCall.cardServiceApiCall(cardNumber);
            log.info("Api Call Response: " + response);

            BinListResponse binListResponse = objectMapper.convertValue(response, BinListResponse.class);

            if (binListResponse != null) {
                cardInfoResponse.setSuccess(true);
                cardInfoResponse.getPayload().setScheme(binListResponse.getScheme());
                cardInfoResponse.getPayload().setType(binListResponse.getType());
                cardInfoResponse.getPayload().setBank(binListResponse.getBank().getName());

                cardInfo.setCardNumber(cardNumber);
                cardInfo.setBank(binListResponse.getBank().getName());
                cardInfo.setScheme(binListResponse.getScheme());
                cardInfo.setType(binListResponse.getType());

                //saves to DB
                cardInfoRepository.save(cardInfo);
            }
            cardInfoResponse.setSuccess(false);
            cardInfoResponse.getPayload().setScheme("N/A");
            cardInfoResponse.getPayload().setType("N/A");
            cardInfoResponse.getPayload().setBank("N/A");

            return cardInfoResponse;
        }
        return cardInfoResponse;
    }
}
