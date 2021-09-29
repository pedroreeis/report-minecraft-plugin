package com.queendev.report.repository.providers;

import com.queendev.report.Report;
import com.queendev.report.repository.Database;
import org.bukkit.Bukkit;

import java.io.File;
import java.sql.*;

public class SQLite implements Database {

    private Connection connection;

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void open() {
        File file = new File(Report.getPlugin().getDataFolder(), "database.db");
        String url = "jdbc:sqlite:" + file;

        try {
            Class.forName("org.sqlite.JDBC");
            connection = DriverManager.getConnection(url);
            createTable();
            Bukkit.getConsoleSender().sendMessage("§a[ReportPlugin] §aConexão com SQLite estabelecida com sucesso.");
        } catch (Exception ex) {
            Bukkit.getConsoleSender().sendMessage("§c[ReportPlugin] §cHouve um erro ao conectar-se com o SQLite!");
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(Report.getPlugin());
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException e) {
            e.printStackTrace();
        }
    }

    @Override
    public void createTable() {
        try {
            PreparedStatement stm = this.connection.prepareStatement(
                    "create table if not exists reportplugin(`player` TEXT NOT NULL, " +
                            "`amountReport` DOUBLE NOT NULL, " +
                            "`date` DATE NOT NULL, " +
                            "`isVisualized` BOOLEAN NOT NULL DEFAULT FALSE)");
            stm.executeUpdate();
        } catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}