package com.juniverse.jdbc;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.juniverse.jdbc.dto.Person;
import java.util.Scanner;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

/** Pruebas del menú de consola con entrada simulada y un repositorio en memoria. */
class PersonMenuTest {

    private InMemoryPersonRepository repository;

    @BeforeEach
    void setUp() {
        repository = new InMemoryPersonRepository();
    }

    private void run(String... lines) {
        new PersonMenu(repository, new Scanner(String.join("\n", lines) + "\n")).run();
    }

    @Test
    void creaUnaPersona() {
        run("1", "Ana", "ana@mail.com", "0");
        assertEquals(1, repository.findAll().size());
        Person ana = repository.findById(1).orElseThrow();
        assertEquals("Ana", ana.getName());
        assertEquals("ana@mail.com", ana.getEmail());
    }

    @Test
    void actualizaSoloLosCamposEscritos() {
        repository.save(new Person("Luis", "luis@mail.com"));
        run("4", "1", "", "nuevo@mail.com", "0");
        Person luis = repository.findById(1).orElseThrow();
        assertEquals("Luis", luis.getName());
        assertEquals("nuevo@mail.com", luis.getEmail());
    }

    @Test
    void eliminaSoloSiSeConfirma() {
        repository.save(new Person("Eva", "eva@mail.com"));
        run("5", "1", "n", "0");
        assertTrue(repository.findById(1).isPresent());
        run("5", "1", "s", "0");
        assertTrue(repository.findById(1).isEmpty());
    }

    @Test
    void unaOpcionNoNumericaNoCierraElPrograma() {
        run("abc", "99", "1", "Zoe", "zoe@mail.com", "0");
        assertEquals(1, repository.findAll().size());
    }

    @Test
    void buscarYListarNoModificanLosDatos() {
        repository.save(new Person("Pedro", "pedro@mail.com"));
        run("2", "3", "1", "0");
        assertEquals(1, repository.findAll().size());
    }
}
