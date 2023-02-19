/*
 * Autor: Saúl García Medina con base en el código de Bezkoder
 * Referencia: https://www.bezkoder.com/spring-boot-jwt-authentication/
 * Esta clase es para crear objetos los cuales tengan la información del JWT que se envía como respuesta
 * al usuario cuando inicia sesión. Al crear un objeto de esta clase se crea el token, el ID del usuario y el nombre
 * de usuario, esta información se envía como respuesta al cliente.
 * */

package com.sgarciam.pruebatecnica.payload.response;

public class JwtResponse {
    private String token;
    private String type = "Bearer";
    private String id;
    private String username;
    public JwtResponse(String accessToken, String id, String username) {
        this.token = accessToken;
        this.id = id;
        this.username = username;
    }

    public String getAccessToken() {
        return token;
    }

    public void setAccessToken(String accessToken) {
        this.token = accessToken;
    }

    public String getTokenType() {
        return type;
    }

    public void setTokenType(String tokenType) {
        this.type = tokenType;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }
}
