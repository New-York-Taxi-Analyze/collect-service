package com.newyorktaxi.mapper;

import com.newyorktaxi.usecase.params.TripInfoParams;
import com.newyorktaxi.model.TripInfoRequest;
import org.mapstruct.Mapper;

@Mapper
public interface TripInfoParamsMapper {

    TripInfoParams toTripInfoParams(TripInfoRequest messageRequest);
}
