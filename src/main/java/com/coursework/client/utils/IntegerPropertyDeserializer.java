package com.coursework.client.utils;

import com.fasterxml.jackson.core.JsonParser;
import com.fasterxml.jackson.databind.DeserializationContext;
import com.fasterxml.jackson.databind.JsonDeserializer;
import javafx.beans.property.IntegerProperty;
import javafx.beans.property.SimpleIntegerProperty;

import java.io.IOException;

public class IntegerPropertyDeserializer extends JsonDeserializer<IntegerProperty> {
    @Override
    public IntegerProperty deserialize(JsonParser p, DeserializationContext ctxt) throws IOException {
        return new SimpleIntegerProperty(p.getIntValue());
    }
}
