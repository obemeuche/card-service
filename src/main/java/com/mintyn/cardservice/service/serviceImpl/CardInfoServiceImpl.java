package com.mintyn.cardservice.service.serviceImpl;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.mintyn.cardservice.apiCall.WebClientCall;
import com.mintyn.cardservice.response.BinListResponse;
import com.mintyn.cardservice.response.CardInfoResponse;
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


    @Override
    public CardInfoResponse verifyCard(String cardNumber) {

        Map<String, Object> response = webClientCall.cardServiceApiCall(cardNumber);

        BinListResponse binListResponse = objectMapper.convertValue(response, BinListResponse.class);

        CardInfoResponse cardInfoResponse = new CardInfoResponse();
        if (binListResponse != null)
        {
            cardInfoResponse.setSuccess(true);
            cardInfoResponse.getPayload().setScheme(binListResponse.getScheme());
            cardInfoResponse.getPayload().setType(binListResponse.getType());
            cardInfoResponse.getPayload().setBank(binListResponse.getBank().getName());

            return cardInfoResponse;
        }
        cardInfoResponse.setSuccess(false);
        cardInfoResponse.getPayload().setScheme("N/A");
        cardInfoResponse.getPayload().setType("N/A");
        cardInfoResponse.getPayload().setBank("N/A");

        return cardInfoResponse;
    }
}
