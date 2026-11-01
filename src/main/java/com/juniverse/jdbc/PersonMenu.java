package com.juniverse.jdbc;

import com.juniverse.jdbc.dao.PersonRepository;
import com.juniverse.jdbc.dto.Person;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

import java.util.logging.Logger;
/**
 * Menú de consola con el CRUD completo (crear, listar, buscar, actualizar y eliminar) sobre una base de datos H2.
 *
 * @author ajuar
 */
public class PersonMenu {
    private final Logger LOGGER = AppLogger.get(PersonMenu.class);


    private final Scanner scanner;
    private final PersonRepository personDao;

    /**
     * Crea el menú.
     * @param personDao repositorio de personas
     * @param scanner origen de la entrada del usuario
     */
    public PersonMenu(PersonRepository personDao, Scanner scanner) {
        this.personDao = personDao;
        this.scanner = scanner;
    }

    /** Repite el menú hasta que se elija salir. */
    public void run() {
        int option;
        try (scanner) {
            do {
                option = showMenu();
                switch (option) {
                    case 1 -> {
                        save();
                    }
                    case 2 -> {
                        findAll();
                    }
                    case 3 -> {
                        findById();
                    }
                    case 4 -> {
                        update();
                    }
                    case 5 -> {
                        delete();
                    }
                    case 0 ->
                        LOGGER.info("Adios");
                    default ->
                        LOGGER.info("Opcion invalida");
                }
            } while (option != 0);
        }
    }

    /**
     * Muestra el menú y lee la opción.
     * @return opción elegida (-1 si no era un número)
     */
    private int showMenu() {
        LOGGER.info("\n=== CRUD DE PERSONAS (H2 Database) ===");
        LOGGER.info("1. Crear persona");
        LOGGER.info("2. Listar todas las personas");
        LOGGER.info("3. Buscar personas por ID");
        LOGGER.info("4. Actualizar persona");
        LOGGER.info("5. Eliminar persona");
        LOGGER.info("0. Salir");
        LOGGER.info("Seleccione una opcion: ");
        return readInt();
    }

    /**
     * Lee un entero de la entrada estándar sin que un texto no numérico cierre el programa.
     *
     * @return el número escrito, o -1 si no es un entero válido
     */
    private int readInt() {
        String line = scanner.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException ex) {
            LOGGER.info("Debe escribir un numero entero");
            return -1;
        }
    }

    /** Pide nombre y correo y crea la persona. */
    private void save() {
        LOGGER.info("\n--- Crear Persona ---");
        LOGGER.info("Nombre: ");
        String name = scanner.nextLine();

        LOGGER.info("Email: ");
        String email = scanner.nextLine();

        Person person = new Person(name, email);

        if (personDao.save(person)) {
            LOGGER.info("Persona creada exitosamente con ID: " + person.getId());
        } else {
            LOGGER.info("Error al crear la persona");
        }
    }

    /** Pide un ID y permite cambiar nombre y correo (Enter conserva el valor actual). */
    private void update() {
        LOGGER.info("\n--- Actualizar Persona ---");
        LOGGER.info("ID de la persona: ");
        int id = readInt();

        Optional<Person> optionalPerson = personDao.findById(id);

        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            LOGGER.info("Persona actual: " + person);

            LOGGER.info("Nuevo nombre (actual: " + person.getName() + "): ");
            String name = scanner.nextLine();
            if (!name.trim().isEmpty()) {
                person.setName(name);
            }

            LOGGER.info("Nuevo email (actual: " + person.getEmail() + "): ");
            String email = scanner.nextLine();
            if (!email.trim().isEmpty()) {
                person.setEmail(email);
            }

            if (personDao.update(person)) {
                LOGGER.info("Persona actualizada exitosamente");
            } else {
                LOGGER.info("Error al actualizar la persona");
            }
        } else {
            LOGGER.info("Persona no encontrada");
        }
    }

    /** Busca y muestra una persona por su ID. */
    private void findById() {
        LOGGER.info("\n--- Buscar Persona ---");
        LOGGER.info("ID de la persona: ");
        int id = readInt();

        Optional<Person> person = personDao.findById(id);

        if (person.isPresent()) {
            LOGGER.info("Persona encontrada: " + person.get());
        } else {
            LOGGER.info("Persona no encontrada");
        }
    }

    /** Lista todas las personas. */
    private void findAll() {
        LOGGER.info("\n--- Lista de Personas ---");
        List<Person> list = personDao.findAll();

        if (list.isEmpty()) {
            LOGGER.info("No hay personas registradas");
        } else {
            for (Person person : list) {
                LOGGER.info(String.valueOf(person));
            }
        }
    }

    /** Elimina una persona tras pedir confirmación. */
    private void delete() {
        LOGGER.info("\n--- Eliminar Persona ---");
        LOGGER.info("ID de la persona: ");
        int id = readInt();

        Optional<Person> person = personDao.findById(id);

        if (person.isPresent()) {
            LOGGER.info("Persona a eliminar: " + person.get());
            LOGGER.info("Esta seguro? (s/n): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("s")) {
                if (personDao.deleteById(id)) {
                    LOGGER.info("Persona eliminada exitosamente");
                } else {
                    LOGGER.info("Error al eliminar persona");
                }
            } else {
                LOGGER.info("Operacion cancelada");
            }
        } else {
            LOGGER.info("Persona no encontrada");
        }
    }
}
