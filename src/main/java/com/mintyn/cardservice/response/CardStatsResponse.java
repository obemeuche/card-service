package com.mintyn.cardservice.response;

import com.mintyn.cardservice.response.dto.StatsPayload;
import lombok.Data;

@Data
public class CardStatsResponse {

    private boolean success;

    private int start;

    private int limit;

    private int size;

   private StatsPayload payload;
}
