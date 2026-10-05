package dao;

import conexion.ConexionDB;
import modelo.Entrega;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class EntregaDAO {

    // CREATE
    public boolean create(Entrega entrega) {

        String sql = """
                INSERT INTO entregas
                (pedido_id, repartidor_id, fecha_hora)
                VALUES (?, ?, ?)
                """;

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, entrega.getPedidoId());
            ps.setInt(2, entrega.getRepartidorId());

            ps.setTimestamp(
                    3,
                    Timestamp.valueOf(entrega.getFechaHora())
            );

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {
            System.out.println("Error al crear entrega: "
                    + e.getMessage());
            return false;
        }
    }

    // READ
    public List<Entrega> readAll() {

        List<Entrega> lista = new ArrayList<>();

        String sql = """
                SELECT id, pedido_id, repartidor_id, fecha_hora
                FROM entregas
                ORDER BY id
                """;

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Entrega entrega = new Entrega();

                entrega.setId(rs.getInt("id"));
                entrega.setPedidoId(
                        rs.getInt("pedido_id")
                );
                entrega.setRepartidorId(
                        rs.getInt("repartidor_id")
                );

                Timestamp timestamp =
                        rs.getTimestamp("fecha_hora");

                if (timestamp != null) {
                    entrega.setFechaHora(
                            timestamp.toLocalDateTime()
                    );
                }

                lista.add(entrega);
            }

        } catch (SQLException e) {
            System.out.println("Error al listar entregas: "
                    + e.getMessage());
        }

        return lista;
    }

    // UPDATE
    public boolean update(Entrega entrega) {

        String sql = """
                UPDATE entregas
                SET pedido_id = ?,
                    repartidor_id = ?,
                    fecha_hora = ?
                WHERE id = ?
                """;

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, entrega.getPedidoId());
            ps.setInt(2, entrega.getRepartidorId());

            ps.setTimestamp(
                    3,
                    Timestamp.valueOf(entrega.getFechaHora())
            );

            ps.setInt(4, entrega.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al actualizar entrega: "
                    + e.getMessage());
            return false;
        }
    }

    // DELETE
    public boolean delete(int id) {

        String sql = "DELETE FROM entregas WHERE id = ?";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {
            System.out.println("Error al eliminar entrega: "
                    + e.getMessage());
            return false;
        }
    }
}