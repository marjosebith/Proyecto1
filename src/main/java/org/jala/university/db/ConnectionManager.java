package org.jala.university.db;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

public class ConnectionManager {
    private static final String URL = "jdbc:sqlite:db.sqlite3";

    public static Connection getConnection() throws SQLException {
        return DriverManager.getConnection(URL);
    }
}
