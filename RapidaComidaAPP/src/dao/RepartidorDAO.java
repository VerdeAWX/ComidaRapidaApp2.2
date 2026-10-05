package dao;

import conexion.ConexionDB;
import modelo.Repartidor;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class RepartidorDAO {

    // CREATE
    public boolean create(Repartidor repartidor) {

        String sql = "INSERT INTO repartidores (nombre) VALUES (?)";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, repartidor.getNombre());

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("Error al crear repartidor: "
                    + e.getMessage());
            return false;
        }
    }

    // READ
    public List<Repartidor> readAll() {

        List<Repartidor> lista = new ArrayList<>();

        String sql = "SELECT id, nombre FROM repartidores ORDER BY id";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Repartidor repartidor = new Repartidor();

                repartidor.setId(rs.getInt("id"));
                repartidor.setNombre(rs.getString("nombre"));

                lista.add(repartidor);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar repartidores: "
                    + e.getMessage());
        }

        return lista;
    }

    // UPDATE
    public boolean update(Repartidor repartidor) {

        String sql = """
                UPDATE repartidores
                SET nombre = ?
                WHERE id = ?
                """;

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, repartidor.getNombre());
            ps.setInt(2, repartidor.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar repartidor: "
                    + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean delete(int id) {

        String sql = "DELETE FROM repartidores WHERE id = ?";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar repartidor: "
                    + e.getMessage());
            return false;
        }
    }
}