package accesodatos;

import java.sql.*;
import java.util.*;

import dto.Producto;

public class ProductoDAO {

    private static final String SQL_SELECT_ALL = """
            SELECT p.id, p.nombre, p.precio, p.stock
            FROM productos p
            ORDER BY p.id
            """;

    private static final String SQL_SELECT_BY_ID = """
            SELECT p.id, p.nombre, p.precio, p.stock
            FROM productos p
            WHERE p.id = ?
            """;

    private static final String SQL_REPONER =
            "{CALL pr_reabastecer_producto(?, ?)}";

    private ProductoDAO() {
    }

    public static List<Producto> obtenerTodos()
            throws SQLException {

        List<Producto> productos = new ArrayList<>();

        try (
            Connection con = ConexionDB.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_SELECT_ALL);
            ResultSet rs = ps.executeQuery()
        ) {

            while (rs.next()) {

                Producto producto = new Producto(
                        rs.getInt("id"),
                        rs.getString("nombre"),
                        rs.getDouble("precio"),
                        rs.getInt("stock")
                );

                productos.add(producto);
            }
        }

        return productos;
    }

    public static Producto buscarPorId(int id)
            throws SQLException {

        try (
            Connection con = ConexionDB.getConnection();
            PreparedStatement ps = con.prepareStatement(SQL_SELECT_BY_ID)
        ) {

            ps.setInt(1, id);

            try (ResultSet rs = ps.executeQuery()) {

                if (rs.next()) {

                    return new Producto(
                            rs.getInt("id"),
                            rs.getString("nombre"),
                            rs.getDouble("precio"),
                            rs.getInt("stock")
                    );
                }
            }
        }

        return null;
    }

    public static void reponerProducto(int id,int cantidad) throws SQLException {

        try (
            Connection con = ConexionDB.getConnection();
            CallableStatement cs = con.prepareCall(SQL_REPONER)
        ) {

            cs.setInt(1, id);
            cs.setInt(2, cantidad);

            cs.execute();
        }
    }
}