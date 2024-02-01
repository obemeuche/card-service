package com.mintyn.cardservice.response.dto;

import lombok.Builder;
import lombok.Data;

@Data
@Builder
public class Payload {

    private String scheme;

    private String type;

    private String bank;

}
