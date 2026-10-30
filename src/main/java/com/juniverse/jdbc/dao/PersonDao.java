package com.juniverse.jdbc.dao;

import com.juniverse.jdbc.dto.Person;
import com.juniverse.jdbc.util.DatabaseUtil;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.SQLException;
import java.util.List;
import static java.lang.System.out;
import java.sql.ResultSet;
import java.sql.Statement;
import java.util.ArrayList;
import java.util.Optional;

/**
 * Acceso a datos de la tabla Persona con JDBC y consultas parametrizadas (PreparedStatement),
 * que evitan la inyección SQL.
 *
 * @author ajuar
 */
public class PersonDao {

    /**
     * Inserta varias personas con el ID que ya traen.
     * @param list personas a guardar
     * @return cantidad de filas insertadas
     */
    public int save(List<Person> list) {
        int cont = 0;
        if (!list.isEmpty()) {
            out.println("Guardando los registros");
            String query = "INSERT INTO Persona (ID, NAME, EMAIL) VALUES(?,?,?)";
            try (Connection conn = DatabaseUtil.getConnection(); PreparedStatement pstmt = conn.prepareStatement(query)) {
                for (Person person : list) {
                    pstmt.setInt(1, person.getId());
                    pstmt.setString(2, person.getName());
                    pstmt.setString(3, person.getEmail());
                    cont += pstmt.executeUpdate();
                }
            } catch (SQLException ex) {
                ex.printStackTrace(System.err);
            }
        }
        return cont;
    }

    /**
     * Devuelve todas las personas.
     * @return lista (vacía si no hay o si ocurre un error)
     */
    public List<Person> findAll() {
        String sql = "SELECT * FROM Persona";
        List<Person> list = new ArrayList<>();
        out.println("Ejecutando consulta");
        try (Connection conn = DatabaseUtil.getConnection(); 
                Statement pstmt = conn.createStatement(); 
                ResultSet rs = pstmt.executeQuery(sql)) {
            while (rs.next()) {
                list.add(new Person(rs.getInt("ID"),
                        rs.getString("NAME"),
                        rs.getString("EMAIL")));
            }
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
        }
        return list;
    }

    /**
     * Busca una persona por su ID.
     * @param id identificador
     * @return la persona, o vacío si no existe
     */
    public Optional<Person> findById(int id) {
        String sql = "SELECT ID, NAME, EMAIL FROM Persona WHERE ID = ?";

        try (Connection conn = DatabaseUtil.getConnection(); 
                PreparedStatement pstmt = conn.prepareStatement(sql)) {

            pstmt.setInt(1, id);

            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return Optional.of(new Person(
                            rs.getInt("ID"),
                            rs.getString("NAME"),
                            rs.getString("EMAIL"))
                    );
                }
            }

        } catch (SQLException e) {
            System.err.println("Error al obtener la persona: " + e.getMessage());

        }
        return Optional.empty();
    }

    /**
     * Elimina una persona.
     * @param id identificador
     * @return {@code true} si se eliminó
     */
    public boolean deleteById(int id) {
        String sql = "DELETE FROM Persona WHERE ID = ?";
        try (Connection conn = DatabaseUtil.getConnection(); 
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setInt(1, id);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;

        } catch (SQLException e) {
            System.err.println("Error al eliminar la persona: " + e.getMessage());
            return false;
        }
    }

    /**
     * Actualiza nombre y correo de una persona.
     * @param person persona con su ID y los nuevos datos
     * @return {@code true} si se actualizó
     */
    public boolean update(Person person) {
        String sql = "UPDATE Persona SET NAME = ?, EMAIL = ? WHERE ID = ?";

        try (Connection conn = DatabaseUtil.getConnection(); 
                PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, person.getName());
            pstmt.setString(2, person.getEmail());
            pstmt.setInt(3, person.getId());
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            System.err.println("Error al actualizar la persona: " + e.getMessage());
            return false;
        }
    }

    /**
     * Inserta una persona y asigna a {@code person} el ID generado por la base de datos.
     * @param person persona a guardar
     * @return {@code true} si se insertó (falla, por ejemplo, si el correo ya existe)
     */
    public boolean save(Person person) {
        out.println("Guardando los registros");
        String query = "INSERT INTO Persona(NAME, EMAIL) VALUES(?,?)";
        try (Connection conn = DatabaseUtil.getConnection(); 
                PreparedStatement pstmt = conn.prepareStatement(query, new String[]{"ID"})) {

            pstmt.setString(1, person.getName());
            pstmt.setString(2, person.getEmail());
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected > 0) {
                try (ResultSet generatedKeys = pstmt.getGeneratedKeys()) {
                    if (generatedKeys.next()) {
                        person.setId(generatedKeys.getInt(1));
                    } else {
                        System.out.println("Warning: No se pudo obtener el ID generado");
                    }
                }
            }
            return rowsAffected > 0;
        } catch (SQLException ex) {
            ex.printStackTrace(System.err);
            return false;
        }
    }

}
