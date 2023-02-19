/*
 * Autor: Bezkoder
 * Referencia: https://www.bezkoder.com/spring-boot-jwt-authentication/
 * Esta clase nos sirve para poder tener más información del usuario en caso de ser necesario.
 * Por ejemplo, si quisiéramos obtener su ID o si en este caso quisiéramos obtener su email (si es que tuviéramos que
 * guardarlo) podríamos obtenerlo mediante esta clase
 * */

package com.sgarciam.pruebatecnica.security.services;

import com.sgarciam.pruebatecnica.models.User;
import com.sgarciam.pruebatecnica.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UsernameNotFoundException;

public class UserDetailsServiceImpl implements UserDetailsService {
    @SuppressWarnings("SpringJavaInjectionPointsAutowiringInspection")
    @Autowired
    UserRepository userRepository;

    @Override
    @Transactional
    public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new UsernameNotFoundException("No se encontró al usuario con el nombre: " + username));
        return UserDetailsImpl.build(user);
    }
}
