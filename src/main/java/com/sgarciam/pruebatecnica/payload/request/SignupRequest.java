/*
 * Autor: Saúl García Medina con base en el código de Bezkoder
 * Referencia: https://www.bezkoder.com/spring-boot-jwt-authentication/
 * Esta clase nos sirve como auxiliar en la petición de registro de usuario, puntualmente, se utiliza en el controlador
 * AuthController.java para registro del usuario. De esta manera se verifica si el usuario ya existe en la BD o si no
 * existe, en este caso, se registra.
 * */

package com.sgarciam.pruebatecnica.payload.request;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;

public class SignupRequest {
    @NotBlank
    @Size(min = 3, max = 20)
    private String username;

    @NotBlank
    @Size(min = 6, max = 40)
    private String password;

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
