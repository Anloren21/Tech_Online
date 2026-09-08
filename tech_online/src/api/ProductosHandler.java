package api;

import java.io.*;
import java.nio.charset.*;
import java.sql.*;
import java.util.*;

import com.sun.net.httpserver.*;

import dto.Producto;
import logicanegocio.TechOnlineNegocio;

public class ProductosHandler implements HttpHandler {

    public void handle(HttpExchange exchange)throws IOException {

        if (!exchange.getRequestMethod().equalsIgnoreCase("GET")) {

            enviarRespuesta(exchange, 405,
                    "{\"error\":\"Método no permitido\"}"
            );

            return;
        }

        try {

            List<Producto> productos =TechOnlineNegocio.obtenerProductos();

            String json = convertirProductosAJson(productos);

            enviarRespuesta( exchange, 200, json );

        } catch (SQLException e) {

            enviarRespuesta( exchange, 500,
                    "{\"error\":\"Error al consultar los productos\"}"
            );
        }
    }

    private String convertirProductosAJson(List<Producto> productos) {

        StringBuilder json = new StringBuilder();

        json.append("[");

        for (int i = 0; i < productos.size(); i++) {

            Producto producto = productos.get(i);

            json.append("{");

            json.append("\"id\":").append(producto.getId()).append(",");

            json.append("\"nombre\":\"").append(escaparJson(producto.getNombre())).append("\",");

            json.append("\"precio\":").append(producto.getPrecio()).append(",");

            json.append("\"stock\":").append(producto.getStock());

            json.append("}");

            if (i < productos.size() - 1) {
                json.append(",");
            }
        }

        json.append("]");

        return json.toString();
    }

    private String escaparJson(String texto) {

        if (texto == null) {
            return "";
        }

        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private void enviarRespuesta(HttpExchange exchange,int codigo,String respuesta)throws IOException {

        byte[] datos =respuesta.getBytes(StandardCharsets.UTF_8);

        exchange.getResponseHeaders().set("Content-Type","application/json; charset=UTF-8");

        exchange.sendResponseHeaders(codigo, datos.length);

        try (OutputStream os =exchange.getResponseBody()) {

            os.write(datos);
        }
    }
}