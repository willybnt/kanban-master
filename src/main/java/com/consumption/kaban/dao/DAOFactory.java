package com.consumption.kaban.dao;

import com.consumption.kaban.MyJdbc;

import java.sql.Connection;
import java.sql.SQLException;

public class DAOFactory {

    private final Connection conn;

    public DAOFactory() throws SQLException {
        this.conn = MyJdbc.getConnection();
    }

    public ProjetoDAO getProjetoDAO() {
        return new ProjetoDAO(conn);
    }

    public MetaDAO getMetaDAO() {
        return new MetaDAO(conn);
    }

    public TarefaDAO getTarefaDAO() {
        return new TarefaDAO(conn);
    }


    public Connection getConnection() {
        return conn;
    }
}
