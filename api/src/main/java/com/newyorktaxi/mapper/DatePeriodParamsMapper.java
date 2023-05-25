package com.newyorktaxi.mapper;

import com.newyorktaxi.usecase.params.DatePeriodParams;
import org.mapstruct.Mapper;

@Mapper
public interface DatePeriodParamsMapper {

    DatePeriodParams toDatePeriodParams(Integer year, Integer month, Integer day);
}
