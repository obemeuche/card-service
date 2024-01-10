package com.mintyn.cardservice.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintyn.cardservice.apiCall.WebClientCall;
import com.mintyn.cardservice.entity.CardInfo;
import com.mintyn.cardservice.exceptions.ConnectionException;
import com.mintyn.cardservice.exceptions.DatabaseException;
import com.mintyn.cardservice.repository.CardInfoRepository;
import com.mintyn.cardservice.response.BinListResponse;
import com.mintyn.cardservice.response.CardInfoResponse;
import com.mintyn.cardservice.response.dto.Payload;
import com.mintyn.cardservice.service.CardInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.Map;

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
        CardInfo cardDetails;
        try
        {
            cardDetails = cardInfoRepository.findByCardNumber(cardNumber);
        }catch (Exception e)
        {
            throw new DatabaseException("DATABASE UNAVAILABLE!");
        }
        CardInfoResponse cardInfoResponse = new CardInfoResponse();

        if (cardDetails != null)
        {
            Payload payload = new Payload();
            payload.setBank(cardDetails.getBank());
            payload.setType(cardDetails.getType());
            payload.setScheme(cardDetails.getScheme());

            cardInfoResponse.setSuccess(true);
            cardInfoResponse.setPayload(payload);

        } else
        {
            Map<String, Object> response;
            try
            {
                response = webClientCall.cardServiceApiCall(cardNumber);
            }catch (Exception e)
            {
                throw new ConnectionException("SERVICE UNAVAILABLE!");
            }
            log.info("Api Call Response: " + response);

            //maps response to binList response body
            BinListResponse binListResponse = objectMapper.convertValue(response, BinListResponse.class);

            if (binListResponse != null)
            {
                cardInfoResponse.setSuccess(true);
                cardInfoResponse.getPayload().setScheme(binListResponse.getScheme());
                cardInfoResponse.getPayload().setType(binListResponse.getType());
                cardInfoResponse.getPayload().setBank(binListResponse.getBank().getName());

                CardInfo cardInfo = new CardInfo();
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
