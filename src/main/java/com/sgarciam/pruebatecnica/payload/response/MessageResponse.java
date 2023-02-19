/*
* Autor: Saúl García Medina
* Esta clase es para crear objetos con un mensaje específico para cada respuesta enviada al cliente por los distintos
* controladores.
* */

package com.sgarciam.pruebatecnica.payload.response;

public class MessageResponse {
    private String message;

    public MessageResponse(String message) {
        this.message = message;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }
}
