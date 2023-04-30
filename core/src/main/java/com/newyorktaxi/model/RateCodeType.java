package com.newyorktaxi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum RateCodeType {
    STANDARD_RATE(1),
    JFK(2),
    NEWARK(3),
    NASSAU_OR_WESTCHESTER(4),
    NEGOTIATED_FARE(5),
    GROUP_RATE(6);

    int value;
}
