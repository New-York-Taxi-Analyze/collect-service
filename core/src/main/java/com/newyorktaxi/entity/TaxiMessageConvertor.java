package com.newyorktaxi.entity;

import com.google.gson.Gson;
import com.newyorktaxi.avro.model.TaxiMessage;
import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;

@Converter(autoApply = true)
public class TaxiMessageConvertor implements AttributeConverter<TaxiMessage, String> {

    private final Gson GSON = new Gson();

    @Override
    public String convertToDatabaseColumn(TaxiMessage attribute) {
        return GSON.toJson(attribute);
    }

    @Override
    public TaxiMessage convertToEntityAttribute(String dbData) {
        return GSON.fromJson(dbData, TaxiMessage.class);
    }
}
