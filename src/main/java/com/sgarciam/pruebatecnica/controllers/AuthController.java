/*
* Autor: Saúl García Medina con base en el código de Bezkoder
* Referencia: https://www.bezkoder.com/spring-boot-jwt-authentication/
* Este es el controlador encargado de manejar el inicio de sesión y el registro de usuarios haciendo uso
* de JWT (JSON Web Tokens) para manejar las sesiones del lado del cliente.
* */

package com.sgarciam.pruebatecnica.controllers;

import com.sgarciam.pruebatecnica.models.User;
import com.sgarciam.pruebatecnica.payload.request.LoginRequest;
import com.sgarciam.pruebatecnica.payload.request.SignupRequest;
import com.sgarciam.pruebatecnica.payload.response.JwtResponse;
import com.sgarciam.pruebatecnica.payload.response.MessageResponse;
import com.sgarciam.pruebatecnica.repository.UserRepository;
import com.sgarciam.pruebatecnica.security.jwt.JwtUtils;
import com.sgarciam.pruebatecnica.security.services.UserDetailsImpl;
import jakarta.validation.*;
import org.springframework.beans.factory.annotation.*;
import org.springframework.http.ResponseEntity;
import org.springframework.security.authentication.*;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.*;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

@CrossOrigin(origins = "*", maxAge = 3600)
@RestController
@RequestMapping("/api/auth")
public class AuthController {
    @Autowired
    AuthenticationManager authenticationManager;

    @Autowired
    UserRepository userRepository;

    @Autowired
    PasswordEncoder encoder;

    @Autowired
    JwtUtils jwtUtils;

    @PostMapping("/signin")
    public ResponseEntity<?> authenticateUser(@Valid @RequestBody LoginRequest loginRequest) {

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(loginRequest.getUsername(), loginRequest.getPassword()));

        SecurityContextHolder.getContext().setAuthentication(authentication);
        String jwt = jwtUtils.generateJwtToken(authentication);

        UserDetailsImpl userDetails = (UserDetailsImpl) authentication.getPrincipal();

        return ResponseEntity.ok(new JwtResponse(jwt,
                userDetails.getId(),
                userDetails.getUsername()));
    }

    @PostMapping("/signup")
    public ResponseEntity<?> registerUser(@Valid @RequestBody SignupRequest signUpRequest) {
        if (userRepository.existsByUsername(signUpRequest.getUsername())) {
            return ResponseEntity
                    .badRequest()
                    .body(new MessageResponse("Error: El usuario ya existe"));
        }

        // Crea un nuevo usuario
        User user = new User(signUpRequest.getUsername(),
                encoder.encode(signUpRequest.getPassword()));

        userRepository.save(user);

        return ResponseEntity.ok(new MessageResponse("Usuario registrado exitosamente"));
    }
}
