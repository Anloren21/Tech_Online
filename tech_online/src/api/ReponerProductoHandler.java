package api;

import java.io.*;
import java.nio.charset.*;
import java.sql.*;
import java.util.regex.*;

import com.sun.net.httpserver.*;

import dto.Producto;
import logicanegocio.TechOnlineNegocio;

public class ReponerProductoHandler implements HttpHandler {

    @Override
    public void handle(HttpExchange exchange)
            throws IOException {

        if (!exchange.getRequestMethod()
                .equalsIgnoreCase("PUT")) {

            enviarRespuesta(
                    exchange,
                    405,
                    "{\"error\":\"Método no permitido\"}"
            );

            return;
        }

        try {

            int productoId =
                    obtenerIdDeRuta(
                            exchange.getRequestURI().getPath()
                    );

            Producto producto =
                    TechOnlineNegocio.buscarProducto(
                            productoId
                    );

            if (producto == null) {

                enviarRespuesta(
                        exchange,
                        404,
                        "{\"error\":\"Producto no encontrado\"}"
                );

                return;
            }

            String cuerpo = new String(
                    exchange.getRequestBody().readAllBytes(),
                    StandardCharsets.UTF_8
            );

            int cantidad =
                    obtenerEnteroJson(
                            cuerpo,
                            "cantidad"
                    );

            TechOnlineNegocio.reponerProducto(
                    productoId,
                    cantidad
            );

            Producto actualizado =
                    TechOnlineNegocio.buscarProducto(
                            productoId
                    );

            String json =
                    "{"
                    + "\"mensaje\":\"Reposición realizada correctamente\","
                    + "\"producto\":{"
                    + "\"id\":" + actualizado.getId() + ","
                    + "\"nombre\":\""
                    + escaparJson(actualizado.getNombre())
                    + "\","
                    + "\"precio\":"
                    + actualizado.getPrecio()
                    + ","
                    + "\"stock\":"
                    + actualizado.getStock()
                    + "}"
                    + "}";

            enviarRespuesta( exchange, 200, json );

        } catch (IllegalArgumentException e) {

            enviarRespuesta( exchange, 400,
                    "{\"error\":\"" + escaparJson(e.getMessage()) + "\"}");

        } catch (SQLException e) {

            if ("45000".equals(e.getSQLState())) {

                enviarRespuesta(exchange, 400,
                        "{\"error\":\""+ escaparJson(e.getMessage()) + "\"}");

            } else {

                enviarRespuesta(exchange, 500,
                        "{\"error\":\"Error interno de base de datos\"}"
                );
            }
        }
    }

    private int obtenerIdDeRuta(String ruta) {

        Pattern patron = Pattern.compile( "^/api/productos/(\\d+)/reponer/?$");

        Matcher matcher = patron.matcher(ruta);

        if (!matcher.matches()) {

            throw new IllegalArgumentException("Ruta de reposición incorrecta");
        }

        return Integer.parseInt(matcher.group(1));
    }

    private int obtenerEnteroJson(String json, String campo) {

        Pattern patron = Pattern.compile("\"" + campo+ "\"\\s*:\\s*(-?\\d+)");

        Matcher matcher =patron.matcher(json);

        if (!matcher.find()) {

            throw new IllegalArgumentException("Falta el campo " + campo);
        }

        return Integer.parseInt(matcher.group(1));
    }

    private String escaparJson(String texto) {

        if (texto == null) {
            return "";
        }

        return texto.replace("\\", "\\\\").replace("\"", "\\\"");
    }

    private void enviarRespuesta(
            HttpExchange exchange,
            int codigo,
            String respuesta)
            throws IOException {

        byte[] datos = respuesta.getBytes( StandardCharsets.UTF_8 );

        exchange.getResponseHeaders().set(
                "Content-Type",
                "application/json; charset=UTF-8"
        );

        exchange.sendResponseHeaders(codigo,datos.length);

        try (OutputStream os = exchange.getResponseBody()) {

            os.write(datos);
        }
    }
}
