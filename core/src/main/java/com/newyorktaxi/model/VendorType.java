package com.newyorktaxi.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.experimental.FieldDefaults;

@Getter
@AllArgsConstructor
@FieldDefaults(level = lombok.AccessLevel.PRIVATE, makeFinal = true)
public enum VendorType {
    CMT(1),
    VFI(2);

    Integer value;
}
