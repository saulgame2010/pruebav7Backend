/*
 * Autor: Saúl García Medina
 * Esta interfaz extiende de MongoRepository para poder interactuar con la base de datos.
 * Aquí se describen los métodos a utilizar para esta interacción, en este caso, es para hacer las querys necesarias
 * para la colección de los usuarios.
 * */

package com.sgarciam.pruebatecnica.repository;

import com.sgarciam.pruebatecnica.models.User;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.Optional;

public interface UserRepository extends MongoRepository<User, String> {
    Optional<User> findByUsername(String username);
    Boolean existsByUsername(String username);
}
