package com.lightsout.model.utils;

import jakarta.persistence.AttributeConverter;
import jakarta.persistence.Converter;
import java.util.List;        // <-- you need this

import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;

@Converter
public class GridConverter implements AttributeConverter<int[][], String> {

    private final ObjectMapper objectMapper = new ObjectMapper();

    @Override
    public String convertToDatabaseColumn(int[][] attribute) {
        try {
            return objectMapper.writeValueAsString(attribute);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public int[][] convertToEntityAttribute(String dbData) {
        try {
            return objectMapper.readValue(dbData, new TypeReference<int[][]>() {});
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}
