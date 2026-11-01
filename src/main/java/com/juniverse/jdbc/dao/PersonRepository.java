package com.juniverse.jdbc.dao;

import com.juniverse.jdbc.dto.Person;
import java.util.List;
import java.util.Optional;

/**
 * Contrato de acceso a datos de personas. El menú depende de esta interfaz, no de JDBC, así que se puede probar con
 * una implementación en memoria.
 */
public interface PersonRepository {

    /**
     * Guarda varias personas.
     * @param list personas a guardar
     * @return cantidad de filas insertadas
     */
    int save(List<Person> list);

    /**
     * Guarda una persona y le asigna el identificador generado.
     * @param person persona nueva
     * @return {@code true} si se guardó
     */
    boolean save(Person person);

    /**
     * Lista todas las personas.
     * @return personas registradas
     */
    List<Person> findAll();

    /**
     * Busca una persona por su identificador.
     * @param id identificador
     * @return la persona, si existe
     */
    Optional<Person> findById(int id);

    /**
     * Actualiza nombre y correo de una persona.
     * @param person persona con los datos nuevos
     * @return {@code true} si se actualizó
     */
    boolean update(Person person);

    /**
     * Elimina una persona.
     * @param id identificador
     * @return {@code true} si se eliminó
     */
    boolean deleteById(int id);
}
