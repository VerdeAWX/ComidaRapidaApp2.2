package dao;

import conexion.ConexionDB;
import modelo.Pedido;

import java.sql.*;
import java.util.ArrayList;
import java.util.List;

public class PedidoDAO {

    // CREATE
    public boolean create(Pedido pedido) {

        String sql = """
                INSERT INTO pedidos
                (direccion, tipo, estado)
                VALUES (?, ?, ?)
                """;

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql)) {

            ps.setString(1, pedido.getDireccion());
            ps.setString(2, pedido.getTipo());
            ps.setString(3, pedido.getEstado());

            ps.executeUpdate();

            return true;

        } catch (SQLException e) {

            System.out.println("Error al crear pedido: "
                    + e.getMessage());

            return false;
        }
    }

    // READ ALL
    public List<Pedido> readAll() {

        List<Pedido> lista = new ArrayList<>();

        String sql = """
                SELECT id, direccion, tipo, estado
                FROM pedidos
                ORDER BY id
                """;

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement ps = conexion.prepareStatement(sql);
             ResultSet rs = ps.executeQuery()) {

            while (rs.next()) {

                Pedido pedido = new Pedido();

                pedido.setId(rs.getInt("id"));
                pedido.setDireccion(
                        rs.getString("direccion")
                );
                pedido.setTipo(
                        rs.getString("tipo")
                );
                pedido.setEstado(
                        rs.getString("estado")
                );

                lista.add(pedido);
            }

        } catch (SQLException e) {

            System.out.println("Error al listar pedidos: "
                    + e.getMessage());
        }

        return lista;
    }

    // FILTRAR
    public List<Pedido> filtrar(String estado, String tipo) {

        List<Pedido> lista = new ArrayList<>();

        StringBuilder sql = new StringBuilder("""
                SELECT id, direccion, tipo, estado
                FROM pedidos
                WHERE 1 = 1
                """);

        List<String> parametros = new ArrayList<>();

        if (estado != null && !estado.equals("TODOS")) {

            sql.append(" AND estado = ?");
            parametros.add(estado);
        }

        if (tipo != null && !tipo.equals("TODOS")) {

            sql.append(" AND tipo = ?");
            parametros.add(tipo);
        }

        sql.append(" ORDER BY id");

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement ps =
                     conexion.prepareStatement(sql.toString())) {

            for (int i = 0; i < parametros.size(); i++) {

                ps.setString(i + 1, parametros.get(i));
            }

            try (ResultSet rs = ps.executeQuery()) {

                while (rs.next()) {

                    Pedido pedido = new Pedido();

                    pedido.setId(
                            rs.getInt("id")
                    );

                    pedido.setDireccion(
                            rs.getString("direccion")
                    );

                    pedido.setTipo(
                            rs.getString("tipo")
                    );

                    pedido.setEstado(
                            rs.getString("estado")
                    );

                    lista.add(pedido);
                }
            }

        } catch (SQLException e) {

            System.out.println(
                    "Error al filtrar pedidos: "
                            + e.getMessage()
            );
        }

        return lista;
    }

    // UPDATE
    public boolean update(Pedido pedido) {

        String sql = """
                UPDATE pedidos
                SET direccion = ?,
                    tipo = ?,
                    estado = ?
                WHERE id = ?
                """;

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setString(1, pedido.getDireccion());
            ps.setString(2, pedido.getTipo());
            ps.setString(3, pedido.getEstado());
            ps.setInt(4, pedido.getId());

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al actualizar pedido: "
                            + e.getMessage()
            );

            return false;
        }
    }

    // DELETE
    public boolean delete(int id) {

        String sql =
                "DELETE FROM pedidos WHERE id = ?";

        try (Connection conexion = ConexionDB.conectar();
             PreparedStatement ps =
                     conexion.prepareStatement(sql)) {

            ps.setInt(1, id);

            return ps.executeUpdate() > 0;

        } catch (SQLException e) {

            System.out.println(
                    "Error al eliminar pedido: "
                            + e.getMessage()
            );

            return false;
        }
    }
}