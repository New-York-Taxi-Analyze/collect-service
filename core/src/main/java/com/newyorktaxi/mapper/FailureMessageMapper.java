package com.newyorktaxi.mapper;

import com.newyorktaxi.avro.model.TaxiMessage;
import com.newyorktaxi.entity.FailureMessage;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;

import java.util.UUID;

@Mapper
public interface FailureMessageMapper {

    @Mapping(target = "status", constant = "RETRY")
    FailureMessage toFailureMessage(String topic, UUID key, TaxiMessage message);
}
