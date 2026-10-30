package com.juniverse.jdbc.util;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;

/**
 * Conexión a la base de datos H2 (archivo {@code ~/crud_db}) y creación de la tabla con datos de ejemplo.
 *
 * @author ajuar
 */
public final class DatabaseUtil {

    private static final String URL = "jdbc:h2:~/crud_db;DB_CLOSE_DELAY=-1;DB_CLOSE_ON_EXIT=FALSE";
    private static final String USERNAME = "sa";
    private static final String PASSWORD = "";

    /** Clase de utilidades: no se instancia. */
    private DatabaseUtil() {

    }

    /**
     * Abre una conexión a la base H2.
     * @return conexión nueva (hay que cerrarla)
     * @throws SQLException si no se puede conectar
     */
    public static Connection getConnection() throws SQLException {
        try {
            Class.forName("org.h2.Driver");
            return DriverManager.getConnection(URL, USERNAME, PASSWORD);
        } catch (ClassNotFoundException ex) {
            throw new SQLException("The driver does not exist");
        }
    }

    /**
     * Cierra una conexión sin lanzar excepciones.
     * @param connection conexión (puede ser null)
     */
    public static void closeConnection(Connection connection) {
        if (connection != null) {
            try {
                connection.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }

    /** Crea la tabla Persona si no existe e inserta datos de ejemplo la primera vez. */
    public static void createTable() {
        String sql = """
            CREATE TABLE IF NOT EXISTS Persona (
                id INT AUTO_INCREMENT PRIMARY KEY,
                name VARCHAR(100) NOT NULL,
                email VARCHAR(100) NOT NULL UNIQUE,
                creation_date TIMESTAMP DEFAULT CURRENT_TIMESTAMP
            )
            """;

        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            stmt.execute(sql);
            System.out.println("Base de datos H2 inicializada correctamente");

            insertDataExamples();

        } catch (SQLException e) {
            System.err.println("Error al crear tabla: " + e.getMessage());
        }
    }

    /** Inserta cinco personas de ejemplo si la tabla está vacía. */
    private static void insertDataExamples() {
        try (Connection conn = getConnection(); Statement stmt = conn.createStatement()) {

            ResultSet rs = stmt.executeQuery("SELECT COUNT(*) FROM Persona");
            rs.next();
            int count = rs.getInt(1);

            if (count == 0) {
                String insertSQL = """
                    INSERT INTO Persona (name, email) VALUES 
                    ('Juan Perez', 'juan@email.com'),
                    ('Maria Garcia', 'maria@email.com'),
                    ('Carlos Lopez', 'carlos@email.com'),
                    ('Ana Martinez', 'ana@email.com'),
                    ('Luis Rodriguez', 'luis@email.com')
                    """;

                stmt.executeUpdate(insertSQL);
                System.out.println("Datos de ejemplo insertados correctamente");
            }

        } catch (SQLException e) {
            System.out.println("Nota: No se pudieron insertar datos de ejemplo");
        }
    }
}
