package accesodatos;

import java.sql.*;

public class ConexionDB {
	public static final String JDBC_URL = "jdbc:mysql://localhost:3306/inventario_hardware";
	public static final String JDBC_USER = "tech_admin";
	public static final String JDBC_PASS = "admin";
	
	
	public static Connection getConnection() throws SQLException {

        return DriverManager.getConnection(
                JDBC_URL,
                JDBC_USER,
                JDBC_PASS
        );
    }
}
