package com.queendev.report.repository.providers;

import com.queendev.report.Report;
import com.queendev.report.repository.Database;
import org.bukkit.Bukkit;

import java.sql.*;

public class MySQL implements Database {

    private Connection connection;

    @Override
    public Connection getConnection() {
        return connection;
    }

    @Override
    public void open() {
        String host = Report.getPlugin().config.getString("MySQL.host");
        String user = Report.getPlugin().config.getString("MySQL.usuario");
        String password = Report.getPlugin().config.getString("MySQL.senha");
        String database = Report.getPlugin().config.getString("MySQL.database");
        String url = "jdbc:mysql://" + host + "/" + database + "?autoReconnect=true";

        try {
            connection = DriverManager.getConnection(url, user, password);
            createTable();
            Bukkit.getConsoleSender().sendMessage("§a[ReportPlugin] §aConexão com MySQL estabelecida com sucesso.");
        } catch (SQLException ex) {
            Bukkit.getConsoleSender().sendMessage("§c[ReportPlugin] §cHouve um erro ao conectar-se com o MySQL!");
            ex.printStackTrace();
            Bukkit.getPluginManager().disablePlugin(Report.getPlugin());
        }
    }

    @Override
    public void close() {
        try {
            connection.close();
        } catch (SQLException ex) {
            ex.printStackTrace();
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