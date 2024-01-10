package com.mintyn.cardservice.response;

import com.mintyn.cardservice.response.dto.Payload;
import lombok.Data;

@Data
public class CardInfoResponse {

    private boolean success;

    private Payload payload;
}
