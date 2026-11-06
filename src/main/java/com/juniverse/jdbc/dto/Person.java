package com.juniverse.jdbc.dto;

/**
 * Persona con identificador, nombre y correo electrónico.
 *
 * @author Java Universe
 */
public class Person {

    private int id;
    private String name;
    private String email;

    /** Crea una persona vacía. */
    public Person() {
    }

    /**
     * Crea una persona nueva (aún sin ID).
     * @param name nombre
     * @param email correo electrónico
     */
    public Person(String name, String email) {
        this.name = name;
        this.email = email;
    }

    /**
     * Crea una persona con ID.
     * @param id identificador
     * @param name nombre
     * @param email correo electrónico
     */
    public Person(int id, String name, String email) {
        this.id = id;
        this.name = name;
        this.email = email;
    }

    /** Identificador. */
    public int getId() {
        return id;
    }

    /**
     * Asigna el identificador.
     * @param id identificador
     */
    public void setId(int id) {
        this.id = id;
    }

    /** Nombre. */
    public String getName() {
        return name;
    }

    /**
     * Asigna el nombre.
     * @param name nombre
     */
    public void setName(String name) {
        this.name = name;
    }

    /** Correo electrónico. */
    public String getEmail() {
        return email;
    }

    /**
     * Asigna el correo electrónico.
     * @param email correo
     */
    public void setEmail(String email) {
        this.email = email;
    }

    /** Representación en texto de la persona. */
    @Override
    public String toString() {
        return "Persona{" + "id=" + id + ", name=" + name + ", email=" + email + '}';
    }

}
