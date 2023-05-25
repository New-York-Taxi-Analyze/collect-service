package com.newyorktaxi.mapper;

import com.newyorktaxi.model.Total;
import com.newyorktaxi.model.TotalResponse;
import org.mapstruct.Mapper;

@Mapper
public interface TotalResponseMapper {

    TotalResponse toTotalResponse(Total total);
}
