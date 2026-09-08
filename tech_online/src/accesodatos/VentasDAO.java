package accesodatos;

import java.sql.*;

import dto.Ventas;

public class VentasDAO {

    private static final String SQL_INSERT = """
            INSERT INTO ventas (producto_id, cantidad)
            VALUES (?, ?)
            """;

    private VentasDAO() {
    }

    public static void insertarVenta(Ventas venta)
            throws SQLException {

        try (
            Connection con = ConexionDB.getConnection();
            PreparedStatement ps =
                    con.prepareStatement(SQL_INSERT)
        ) {

            ps.setInt(1, venta.getProductoId());
            ps.setInt(2, venta.getCantidad());

            ps.executeUpdate();
        }
    }
}