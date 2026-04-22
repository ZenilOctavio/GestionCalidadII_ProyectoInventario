package mx.unison.infrastructure.persistence;

import mx.unison.core.domain.services.PasswordHasher;

import java.sql.*;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.List;

public class DatabaseSeeder {
    private final Database db;
    private final PasswordHasher passwordHasher;

    public DatabaseSeeder(Database db, PasswordHasher passwordHasher) throws SQLException{
        this.db = db;
        this.passwordHasher = passwordHasher;

    }

    public void seed() {
        try {
            // Insertar usuarios base si no existen
            insertDefaultUser("ADMIN", "admin23", "ADMIN");
            insertDefaultUser("PRODUCTOS", "productos19", "PRODUCTOS");
            insertDefaultUser("ALMACENES", "almacenes11", "ALMACENES");

            // Establecer fecha de creación para productos/almacenes existentes si están vacíos
            setDefaultFechaCreacionIfEmpty("productos");
            setDefaultFechaCreacionIfEmpty("almacenes");
            System.out.println("🌱 Datos iniciales insertados con éxito.");
        } catch (SQLException e) {
            System.err.println("❌ Error durante el seeding: " + e.getMessage());
        }
    }

    private void insertDefaultUser(String nombre, String passPlain, String rol) throws SQLException {
        String check = "SELECT nombre FROM usuarios WHERE nombre=?";
        try (PreparedStatement ps = db.conn.prepareStatement(check)) {
            ps.setString(1, nombre);
            ResultSet rs = ps.executeQuery();
            if (!rs.next()) {
                String ins = "INSERT INTO usuarios(nombre, contrasena, rol) VALUES(?, ?, ?)";
                try (PreparedStatement ps2 = db.conn.prepareStatement(ins)) {
                    ps2.setString(1, nombre);
                    ps2.setString(2, passwordHasher.hash(passPlain));
                    ps2.setString(3, rol);
                    ps2.executeUpdate();
                }
            }
        }
    }
    private void setDefaultFechaCreacionIfEmpty(String table) throws SQLException {
        String checkSql = String.format("SELECT id, fecha_hora_creacion FROM %s", table);
        try (PreparedStatement ps = db.conn.prepareStatement(checkSql)) {
            ResultSet rs = ps.executeQuery();
            List<Integer> ids = new ArrayList<>();
            while (rs.next()) {
                String f = rs.getString("fecha_hora_creacion");
                if (f == null || f.isEmpty()) ids.add(rs.getInt("id"));
            }
            String upd = String.format("UPDATE %s SET fecha_hora_creacion=? WHERE id=?", table);
            for (Integer id : ids) {
                try (PreparedStatement pu = db.conn.prepareStatement(upd)) {
                    pu.setString(1, LocalDateTime.now().format(DateTimeFormatter.ISO_LOCAL_DATE_TIME));
                    pu.setInt(2, id);
                    pu.executeUpdate();
                }
            }
        }
    }

}