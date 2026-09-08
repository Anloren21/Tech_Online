package presentacion;

import static bibliotecas.Consola.*;

import java.sql.*;
import java.util.*;

import accesodatos.*;
import dto.*;
import logicanegocio.*;

public class ConsolaUsuario {
	
	private static final Scanner teclado = new Scanner(System.in);
	private static final int SALIR = 0;
	
	private static final String FORMATO_CABECERAS = "%-4s %-25s %12s %12s%n";
	private static final String FORMATO_LINEA = "%-4d %-25s %10.2f € %12d%n";

	public static void main(String[] args) {
		int opcion;
		
		do {
			mostrarMenu();
			opcion = pedirOpcion();
			procesarOpcion(opcion);
		} while (opcion != SALIR);
	}

	private static void mostrarMenu() {
		pl("""
				
    			TECH ONLINE - PRUEBAS
    		================================
    		1. Probar conexión a MySQL
    		
		    2. Mostrar todos los productos
		    
		    3. Buscar producto por ID
		    
		    4. Reponer stock de producto
		    
		    5. Realizar Venta
		    
		    0. Salir 
		    
		    """);
	}
	
	private static int pedirOpcion() {
		return pedirInt("\nDime la opción");
	}

	private static void procesarOpcion(int opcion) {
		switch(opcion) {
             case 1->probarConexion();
             case 2->mostrarProductos();
             case 3->buscarProducto();
             case 4->reponerProducto();
             case 5->realizarVenta();
             case SALIR-> pl("Gracias por usar esta aplicación");
             default ->pl("Opción no válida");
		}
    }

    private static void probarConexion() {
        try (Connection con = ConexionDB.getConnection()) {
            	System.out.println("\nConexión con MySQL correcta.");
            	
        } catch (SQLException e) {

            System.out.println("\nError de conexión:");

            System.out.println(e.getMessage());
        }
    }
    
    private static void mostrarCabeceras() {
		System.out.printf(FORMATO_CABECERAS, "ID", "Producto","Precio", "Stock Actual");
		System.out.printf(FORMATO_CABECERAS, "--", "--------","------", "------------");
	}
    
    private static void mostrarProducto(Producto producto) {
    	System.out.printf(FORMATO_LINEA,
                producto.getId(),
                producto.getNombre(),
                producto.getPrecio(),
                producto.getStock()
        );
    }

    private static void mostrarProductos() {

        try {

            List<Producto> productos = ProductoDAO.obtenerTodos();

            System.out.println("\n--- LISTADO DE PRODUCTOS ---" );
            
            
            if (productos.isEmpty()) {
                System.out.println("No existen productos.");
                return;
            }
            
            mostrarCabeceras();
            
            for (Producto producto : productos) {

            	mostrarProducto(producto);
            }

        } catch (SQLException e) {

            System.out.println("\nError al consultar productos:");

            System.out.println(e.getMessage());
        }
    }

    private static void buscarProducto() {

        try {

            System.out.print("\nIntroduce el ID del producto: ");
            
            int id = Integer.parseInt(teclado.nextLine());

            Producto producto = TechOnlineNegocio.buscarProducto(id);

            if (producto == null) {

                System.out.println("Producto no encontrado.");

            } else {

                System.out.println("\nProducto encontrado:");
                
                mostrarCabeceras();
                mostrarProducto(producto);
            }

        } catch (NumberFormatException e) {

            System.out.println("El ID debe ser un número.");

        } catch (SQLException e) {

            System.out.println("Error al buscar el producto:");

            System.out.println(e.getMessage());
        }
    }

    private static void reponerProducto() {

        try {

            System.out.print("\nIntroduce el ID del producto: ");

            int id = Integer.parseInt(teclado.nextLine());

            Producto producto = TechOnlineNegocio.buscarProducto(id);

            if (producto == null) {

                System.out.println("Producto no encontrado.");
                return;
            }

            System.out.println("\nProducto seleccionado:");

			mostrarCabeceras();
			mostrarProducto(producto);
			
            System.out.print("\nCantidad a reponer: ");

            int cantidad = Integer.parseInt(teclado.nextLine());

            TechOnlineNegocio.reponerProducto(id, cantidad);

            Producto actualizado = TechOnlineNegocio.buscarProducto(id);

            System.out.println( "\nReposición realizada correctamente." );

            System.out.println( "\nProducto actualizado:" );

            mostrarCabeceras();
            mostrarProducto(actualizado);

        } catch (NumberFormatException e) {

            System.out.println("Debes introducir valores numéricos.");

        } catch (SQLException e) {

            System.out.println("\nError al realizar la reposición:");

            System.out.println(e.getMessage());
        }
    }
    private static void realizarVenta() {

        try {

            System.out.print( "\nIntroduce el ID del producto: " );

            int productoId = Integer.parseInt(  teclado.nextLine());

            Producto producto = TechOnlineNegocio.buscarProducto(productoId);

            if (producto == null) {

                System.out.println("Producto no encontrado.");
                return;
            }

            System.out.println("\nProducto seleccionado:");

            mostrarCabeceras();
            mostrarProducto(producto);
            
            System.out.print("\nCantidad a vender: ");

            int cantidad = Integer.parseInt(teclado.nextLine());

            TechOnlineNegocio.realizarVenta(productoId,cantidad);

            Producto actualizado = TechOnlineNegocio.buscarProducto(productoId);

            System.out.println("\nVenta realizada correctamente.");

            System.out.println("\nProducto después de la venta:");

            mostrarCabeceras();
            mostrarProducto(actualizado);

        } catch (NumberFormatException e) {

            System.out.println("\nDebes introducir valores numéricos.");

        } catch (SQLException e) {

            System.out.println("\nNo se pudo realizar la venta.");

            System.out.println("Mensaje de MySQL: "+ e.getMessage());
        }
    }
}