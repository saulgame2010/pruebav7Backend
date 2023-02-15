package com.sgarciam.pruebatecnica.models;

import jakarta.validation.constraints.NotBlank;
import org.hibernate.annotations.DynamicInsert;
import org.hibernate.annotations.DynamicUpdate;
import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.HashMap;
import java.util.Map;

@Document(collection = "documents")
@DynamicInsert
@DynamicUpdate
public class DocumentoJson {
    @Id
    private String id;

    //La anotación NotBlank hará que nuestro item no sea nulo
    @NotBlank
    private String item;

    //Usaremos un Map para que las propiedades del JSON sean dinámicas
    private Map<String, Object> properties = new HashMap<>();

    public DocumentoJson() {}

    public DocumentoJson(String item) {
        this.item = item;
    }

    public void setProperties(String key, Object value) {
        this.properties.put(key, value);
    }

    public Map<String, Object> getProperties() {
        return properties;
    }

    public String getItem() {
        return item;
    }

    public void setItem(String item) {
        this.item = item;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }
}
