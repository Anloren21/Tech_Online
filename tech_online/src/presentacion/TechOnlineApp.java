package presentacion;

import java.net.InetSocketAddress;
import java.nio.file.*;
import java.nio.charset.*;


import com.sun.net.httpserver.*;

import api.*;

public class TechOnlineApp {

    public static void main(String[] args)
            throws Exception {

        HttpServer servidor =
                HttpServer.create(
                        new InetSocketAddress(8080),
                        0
                );

        // GET /api/productos
        servidor.createContext(
                "/api/productos",
                new ProductosHandler()
        );

        // PUT /api/productos/{id}/reponer
        servidor.createContext(
                "/api/productos/",
                new ReponerProductoHandler()
        );

        // POST /api/ventas
        servidor.createContext(
                "/api/ventas",
                new VentasHandler()
        );

        // WEB
        servidor.createContext("/", exchange -> {

            String ruta = exchange.getRequestURI().getPath();

            if (ruta.equals("/")) {
                ruta = "/index.html";
            }

            Path base = Path.of("src", "webapp");
            Path archivo = base.resolve(ruta.substring(1)).normalize();

            if (!archivo.startsWith(base)
                    || !Files.exists(archivo)
                    || Files.isDirectory(archivo)) {

                exchange.sendResponseHeaders(404, -1);
                exchange.close();
                return;
            }

            String tipo = Files.probeContentType(archivo);

            if (tipo == null) {
                if (ruta.endsWith(".css")) {
                    tipo = "text/css";
                } else if (ruta.endsWith(".js")) {
                    tipo = "application/javascript";
                } else {
                    tipo = "text/plain";
                }
            }

            byte[] datos = Files.readAllBytes(archivo);

            exchange.getResponseHeaders().set(
                    "Content-Type",
                    tipo + "; charset=UTF-8"
            );

            exchange.sendResponseHeaders(200, datos.length);
            exchange.getResponseBody().write(datos);
            exchange.close();
        });
        
        servidor.setExecutor(null);

        servidor.start();

        System.out.println(
                """
                      TECH ONLINE - API REST
                ========================================
                Servidor iniciado en http://localhost:8080

                GET  /api/productos
                POST /api/ventas
                PUT  /api/productos/{id}/reponer
                """
        );
    }
}