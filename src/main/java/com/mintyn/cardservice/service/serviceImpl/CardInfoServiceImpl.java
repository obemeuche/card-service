package com.mintyn.cardservice.service.serviceImpl;

import com.mintyn.cardservice.repository.CardInfoRepository;
import com.mintyn.cardservice.response.CardInfoResponse;
import com.mintyn.cardservice.service.CardInfoService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardInfoServiceImpl implements CardInfoService {

    private final CardInfoRepository cardInfoRepository;

    @Override
    public CardInfoResponse verifyCard(String cardNumber) {
        return null;
    }
}
