package com.mintyn.cardservice.service;

import com.mintyn.cardservice.response.CardStatsResponse;

public interface CardStatsService {

    CardStatsResponse getCardStats(int start, int limit);
}
