package com.juniverse.jdbc;

import com.juniverse.jdbc.dao.PersonDao;
import com.juniverse.jdbc.dto.Person;
import com.juniverse.jdbc.util.DatabaseUtil;
import java.util.List;
import java.util.Optional;
import java.util.Scanner;

/**
 * Menú de consola con el CRUD completo (crear, listar, buscar, actualizar y eliminar) sobre una base de datos H2.
 *
 * @author ajuar
 */
public class Main {

    private static final Scanner scanner = new Scanner(System.in);
    private static final PersonDao personDao = new PersonDao();

    /**
     * Crea la tabla y repite el menú hasta que se elija salir.
     * @param args no se usan
     */
    public static void main(String[] args) {
        int option;
        try (scanner) {
            DatabaseUtil.createTable();
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
                        System.out.println("Adios");
                    default ->
                        System.out.println("Opcion invalida");
                }
            } while (option != 0);
        }
    }

    /**
     * Muestra el menú y lee la opción.
     * @return opción elegida (-1 si no era un número)
     */
    private static int showMenu() {
        System.out.println("\n=== CRUD DE PERSONAS (H2 Database) ===");
        System.out.println("1. Crear persona");
        System.out.println("2. Listar todas las personas");
        System.out.println("3. Buscar personas por ID");
        System.out.println("4. Actualizar persona");
        System.out.println("5. Eliminar persona");
        System.out.println("0. Salir");
        System.out.print("Seleccione una opcion: ");
        return readInt();
    }

    /**
     * Lee un entero de la entrada estándar sin que un texto no numérico cierre el programa.
     *
     * @return el número escrito, o -1 si no es un entero válido
     */
    private static int readInt() {
        String line = scanner.nextLine().trim();
        try {
            return Integer.parseInt(line);
        } catch (NumberFormatException ex) {
            System.out.println("Debe escribir un numero entero");
            return -1;
        }
    }

    /** Pide nombre y correo y crea la persona. */
    private static void save() {
        System.out.println("\n--- Crear Persona ---");
        System.out.print("Nombre: ");
        String nombre = scanner.nextLine();

        System.out.print("Email: ");
        String email = scanner.nextLine();

        Person person = new Person(nombre, email);

        if (personDao.save(person)) {
            System.out.println("Persona creada exitosamente con ID: " + person.getId());
        } else {
            System.out.println("Error al crear la persona");
        }
    }

    /** Pide un ID y permite cambiar nombre y correo (Enter conserva el valor actual). */
    private static void update() {
        System.out.println("\n--- Actualizar Persona ---");
        System.out.print("ID de la persona: ");
        int id = readInt();

        Optional<Person> optionalPerson = personDao.findById(id);

        if (optionalPerson.isPresent()) {
            Person person = optionalPerson.get();
            System.out.println("Persona actual: " + person);

            System.out.print("Nuevo nombre (actual: " + person.getName() + "): ");
            String nombre = scanner.nextLine();
            if (!nombre.trim().isEmpty()) {
                person.setName(nombre);
            }

            System.out.print("Nuevo email (actual: " + person.getEmail() + "): ");
            String email = scanner.nextLine();
            if (!email.trim().isEmpty()) {
                person.setEmail(email);
            }

            if (personDao.update(person)) {
                System.out.println("Persona actualizada exitosamente");
            } else {
                System.out.println("Error al actualizar la persona");
            }
        } else {
            System.out.println("Persona no encontrada");
        }
    }

    /** Busca y muestra una persona por su ID. */
    private static void findById() {
        System.out.println("\n--- Buscar Persona ---");
        System.out.print("ID de la persona: ");
        int id = readInt();

        Optional<Person> person = personDao.findById(id);

        if (person.isPresent()) {
            System.out.println("Persona encontrada: " + person.get());
        } else {
            System.out.println("Persona no encontrada");
        }
    }

    /** Lista todas las personas. */
    private static void findAll() {
        System.out.println("\n--- Lista de Personas ---");
        List<Person> list = personDao.findAll();

        if (list.isEmpty()) {
            System.out.println("No hay personas registradas");
        } else {
            for (Person person : list) {
                System.out.println(person);
            }
        }
    }

    /** Elimina una persona tras pedir confirmación. */
    private static void delete() {
        System.out.println("\n--- Eliminar Persona ---");
        System.out.print("ID de la persona: ");
        int id = readInt();

        Optional<Person> person = personDao.findById(id);

        if (person.isPresent()) {
            System.out.println("Persona a eliminar: " + person.get());
            System.out.print("Esta seguro? (s/n): ");
            String confirmation = scanner.nextLine();

            if (confirmation.equalsIgnoreCase("s")) {
                if (personDao.deleteById(id)) {
                    System.out.println("Persona eliminada exitosamente");
                } else {
                    System.out.println("Error al eliminar persona");
                }
            } else {
                System.out.println("Operacion cancelada");
            }
        } else {
            System.out.println("Persona no encontrada");
        }
    }
}
