package com.sgarciam.pruebatecnica.security.jwt;

import com.sgarciam.pruebatecnica.security.services.UserDetailsImpl;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Component;
import io.jsonwebtoken.*;

import java.util.Date;

@Component
public class JwtUtils {
    private static final Logger logger = LoggerFactory.getLogger(JwtUtils.class);

    @Value("${sgarciam.app.jwtSecret}")
    private String jwtSecret;

    @Value("${sgarciam.app.jwtExpirationMs}")
    private int jwtExpirationMs;

    public String generateJwtToken(Authentication authentication) {

        UserDetailsImpl userPrincipal = (UserDetailsImpl) authentication.getPrincipal();

        return Jwts.builder()
                .setSubject((userPrincipal.getUsername()))
                .setIssuedAt(new Date())
                .setExpiration(new Date((new Date()).getTime() + jwtExpirationMs))
                .signWith(SignatureAlgorithm.HS256, jwtSecret)
                .compact();
    }

    public String getUserNameFromJwtToken(String token) {
        return Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(token).getBody().getSubject();
    }

    public boolean validateJwtToken(String authToken) {
        try {
            Jwts.parser().setSigningKey(jwtSecret).parseClaimsJws(authToken);
            return true;
        } catch (SignatureException e) {
            logger.error("Firma no válida: {}", e.getMessage());
        } catch (MalformedJwtException e) {
            logger.error("Token no válido: {}", e.getMessage());
        } catch (ExpiredJwtException e) {
            logger.error("El token ha expirado: {}", e.getMessage());
        } catch (UnsupportedJwtException e) {
            logger.error("El token no está soportado: {}", e.getMessage());
        } catch (IllegalArgumentException e) {
            logger.error("La cadena de reclamos del token está vacía: {}", e.getMessage());
        }

        return false;
    }
}
