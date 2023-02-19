/*
* Autor: Saúl García Medina
* Esta interfaz extiende de MongoRepository para poder interactuar con la base de datos.
* Aquí se describen los métodos a utilizar para esta interacción, en este caso, es para hacer las querys necesarias
* para la colección de los documentos JSON.
* */

package com.sgarciam.pruebatecnica.repository;

import com.sgarciam.pruebatecnica.models.DocumentoJson;
import org.springframework.data.mongodb.repository.MongoRepository;

import java.util.List;

public interface DocumentoJsonRepository extends MongoRepository<DocumentoJson, String> {
    //Este método nos ayudará a encontrar documentos por el nombre de su item pasándole una subcadena en el query
    List<DocumentoJson> findByItemContainingIgnoreCase(String query);
}
