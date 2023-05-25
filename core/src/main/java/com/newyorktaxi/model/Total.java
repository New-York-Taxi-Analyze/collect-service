package com.newyorktaxi.model;


import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class Total {

    Double total;
    String date;
}
