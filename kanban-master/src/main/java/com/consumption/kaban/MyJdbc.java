package com.consumption.kaban;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;

    public class MyJdbc {
        public static Connection getConnection() throws SQLException {
            String url = "jdbc:mysql://localhost:3306/kanban";
            String user = "root";
            String password = "2103";
            return DriverManager.getConnection(url, user, password);
        }
    }


