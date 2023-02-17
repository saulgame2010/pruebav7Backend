package com.sgarciam.pruebatecnica.models;

import java.util.Map;

public class JsonObject {
    private Map<String, Object> properties;

    public JsonObject() {
    }

    public JsonObject(Map<String, Object> properties) {
        this.properties = properties;
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public void setProperties(Map<String, Object> properties) {
        this.properties = properties;
    }
}
