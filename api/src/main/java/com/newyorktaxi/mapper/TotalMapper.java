package com.newyorktaxi.mapper;

import com.newyorktaxi.model.DatePeriod;
import org.mapstruct.Mapper;

@Mapper(componentModel = "spring")
public interface TotalMapper {

    DatePeriod toDatePeriod(Integer year, Integer month, Integer day);
}
