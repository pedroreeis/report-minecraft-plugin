package com.queendev.report.process;

import com.queendev.report.Report;
import com.queendev.report.managers.ReportManager;
import com.queendev.report.models.ReportModel;
import com.queendev.report.repository.Database;

import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

public class ReportProcess {

    private static Database database = Report.getPlugin().database;
    private static ReportManager manager = Report.getPlugin().manager;

    public static void loadReports() {
        try {
            PreparedStatement stm = database.getConnection().prepareStatement(
                    "select * from reportplugin"
            );

            ResultSet rs = stm.executeQuery();
            while (rs.next()) {
                String playerName = rs.getString("player");
                double amountReport = rs.getDouble("amountReport");
                Date date = rs.getDate("date");
                boolean isVisualized = rs.getBoolean("isVisualized");

                manager.loadReport(playerName.toLowerCase(), amountReport, date, isVisualized);
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }

    private static boolean containsAccountSQL(String playerName) {
        try {
            PreparedStatement stm = database.getConnection().prepareStatement(
                    "select * from reportplugin where player = ?"
            );
            stm.setString(1, playerName);
            return stm.executeQuery().next();
        }catch (SQLException ex) {
            ex.printStackTrace();
            return false;
        }
    }

    public static void saveReports() {
        PreparedStatement stm;
        try {
            for (ReportModel report : manager.reports.values()) {
                if(containsAccountSQL(report.getPlayerName())) {
                    stm = database.getConnection().prepareStatement(
                            "update reportplugin set amountReport = ?,date = ?,isVisualized = ? where player = ?"
                    );

                    stm.setDouble(1, report.getAmountReport());
                    stm.setDate(2, report.getDate());
                    stm.setBoolean(3, report.isVisualized());
                    stm.setString(4, report.getPlayerName());

                    stm.executeUpdate();
                }else {
                    stm = database.getConnection().prepareStatement(
                            "insert into reportplugin(player, amountReport, date, isVisualized) VALUES(?,?,?,?)"
                    );

                    stm.setDouble(2, report.getAmountReport());
                    stm.setDate(3, report.getDate());
                    stm.setBoolean(4, report.isVisualized());
                    stm.setString(1, report.getPlayerName());

                    stm.executeUpdate();
                }
            }
        }catch (SQLException ex) {
            ex.printStackTrace();
        }
    }
}
