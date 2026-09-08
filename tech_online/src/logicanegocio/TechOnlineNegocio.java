package logicanegocio;

import java.sql.*;
import java.util.*;

import accesodatos.*;
import dto.*;

public class TechOnlineNegocio {

    private TechOnlineNegocio() {
    }

    public static List<Producto> obtenerProductos() throws SQLException {

        return ProductoDAO.obtenerTodos();
    }

    public static Producto buscarProducto(int id)throws SQLException {

        return ProductoDAO.buscarPorId(id);
    }

    public static void realizarVenta(int productoId, int cantidad) throws SQLException {

        Ventas venta = new Ventas(productoId, cantidad);

        VentasDAO.insertarVenta(venta);
    }

    public static void reponerProducto(int productoId, int cantidad) throws SQLException {

        ProductoDAO.reponerProducto(productoId,cantidad);
    }
}