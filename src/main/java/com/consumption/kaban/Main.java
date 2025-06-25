package com.consumption.kaban;

import com.consumption.kaban.ui.ProjetoView;

import javax.swing.*;
import java.sql.SQLException;

public class Main {
    public static void main(String[] args) {
        SwingUtilities.invokeLater(() -> {
            try {
                new ProjetoView();
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        });
    }
}
