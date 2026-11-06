package com.juniverse.jdbc;

import com.juniverse.jdbc.dao.PersonDao;
import com.juniverse.jdbc.util.DatabaseUtil;
import java.util.Scanner;

/**
 * Punto de entrada: prepara la base de datos H2 y muestra el menú de consola del CRUD de personas.
 *
 * @author Java Universe
 */
public class Main {

    private Main() {
    }

    /**
     * Crea la tabla y abre el menú.
     * @param args no se usan
     */
    public static void main(String[] args) {
        DatabaseUtil.createTable();
        new PersonMenu(new PersonDao(), new Scanner(System.in)).run();
    }
}
