package com.omelchenkoaleks.core.database;

import org.sqlite.SQLiteException;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.logging.Level;
import java.util.logging.Logger;

public class SQLiteConnection {

    private static Connection connection;

    public static Connection getConnection() {

        try {

            Class.forName("org.sqlite.JDBC").newInstance();

            String url = "jdbc:sqlite:D:\\DBsqlite_arhiv\\money.db";

            if (connection == null) {
                connection = DriverManager.getConnection(url);
            }
            connection.createStatement().execute("PRAGMA foreign_keys = ON");
            return connection;


        } catch (SQLiteException | ClassNotFoundException | InstantiationException | IllegalAccessException ex) {

            Logger.getLogger(org.sqlite.SQLiteConnection.class.getName()).log(Level.SEVERE, null, ex);

        } catch (SQLException e) {
            e.printStackTrace();
        }

        return null;

    }
}
