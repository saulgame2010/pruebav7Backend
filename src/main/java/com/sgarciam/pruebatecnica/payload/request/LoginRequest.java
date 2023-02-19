/*
* Autor: Saúl García Medina con base en el código de Bezkoder
* Referencia: https://www.bezkoder.com/spring-boot-jwt-authentication/
* Esta clase nos sirve como auxiliar en la petición de inicio de sesión, puntualmente, se utiliza en el controlador
* AuthController.java para el inicio de sesión del usuario. Ayudando a manejar los datos de inicio de sesión.
* */

package com.sgarciam.pruebatecnica.payload.request;

import jakarta.validation.constraints.NotBlank;

public class LoginRequest {
    @NotBlank
    private String username;

    @NotBlank
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
