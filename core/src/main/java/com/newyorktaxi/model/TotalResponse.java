package com.newyorktaxi.model;

import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class TotalResponse {

    Double total;
    String date;
}
