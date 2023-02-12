package com.newyorktaxi.usecase.impl;

import com.newyorktaxi.model.DatePeriod;
import com.newyorktaxi.model.TotalResponse;
import com.newyorktaxi.usecase.FunctionalUseCase;
import org.springframework.stereotype.Component;

import java.security.InvalidParameterException;
import java.text.ParseException;
import java.text.SimpleDateFormat;

@Component
public class GetTotalUseCase implements FunctionalUseCase<DatePeriod, TotalResponse> {

    @Override
    public TotalResponse execute(DatePeriod totalRequest) {
        validateDate(totalRequest.getYear(), totalRequest.getMonth(), totalRequest.getDay());

        return TotalResponse.builder()
                .total(201.)
                .date(totalRequest.getYear() + "/" + totalRequest.getMonth() + "/" + totalRequest.getDay())
                .build();
    }

    public void validateDate(Integer year, Integer month, Integer day) {
        if (month == null && day != null) {
            throw new InvalidParameterException("month value is empty");
        }

        if (year != null && month != null  && day != null) {
            final SimpleDateFormat sdf = new SimpleDateFormat("yyyy/MM/dd");
            sdf.setLenient(false);
            try {
                sdf.parse(year + "/" + month + "/" + day);
            } catch (ParseException e) {
                throw new InvalidParameterException("date is invalid");
            }
        }
    }
}
