package com.newyorktaxi.mapper;

import com.newyorktaxi.model.TripInfo;
import com.newyorktaxi.model.TripInfoRequest;
import org.mapstruct.Mapper;

@Mapper
public interface TripInfoMapper {

    TripInfo toTripInfo(TripInfoRequest messageRequest);
}
