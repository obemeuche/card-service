package com.mintyn.cardservice.service.serviceImpl;

import com.mintyn.cardservice.response.CardStatsResponse;
import com.mintyn.cardservice.service.CardStatsService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
@Slf4j
public class CardStatsServiceImpl implements CardStatsService {

    @Override
    public CardStatsResponse getCardStats(int start, int limit) {
        return null;
    }
}
