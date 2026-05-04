package ar.com.petmanager.persistence;

import java.io.IOException;
import java.io.InputStream;
import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Properties;

/**
 * Singleton que gestiona la conexión JDBC a MySQL.
 * Lee credenciales desde database.properties en el classpath.
 */
public class DBConnector {
    private static DBConnector instance;
    private final String url;
    private final String user;
    private final String password;
    private final String driver;

    private DBConnector() throws PersistenceException {
        Properties props = new Properties();
        try (InputStream input = getClass().getClassLoader().getResourceAsStream("database.properties")) {
            if (input==null) {
                throw new PersistenceException("No se encontró database.properties en classpath");
            }
            props.load(input);
        } catch (IOException e) {
            throw new PersistenceException("Error al leer database.properties", e);
        }

        this.driver = props.getProperty("db.driver");
        this.url = props.getProperty("db.url");
        this.user = props.getProperty("db.user");
        this.password = props.getProperty("db.password");

        try {
            Class.forName(driver);
        } catch (ClassNotFoundException e) {
            throw new PersistenceException("Driver MySQL no encontrado: " + driver, e);
        }
    }

    public static synchronized DBConnector getInstance() throws PersistenceException {
        if (instance==null) {
            instance = new DBConnector();
        }
        return instance;
    }

    public Connection getConnection() throws PersistenceException {
        try {
            return DriverManager.getConnection(url, user, password);
        } catch (SQLException e) {
            throw new PersistenceException("Error al conectar con base de datos: " + e.getMessage(), e);
        }
    }

    public void closeConnection(Connection conn) {
        if (conn!=null) {
            try {
                conn.close();
            } catch (SQLException e) {
                System.err.println("Error al cerrar conexión: " + e.getMessage());
            }
        }
    }
}