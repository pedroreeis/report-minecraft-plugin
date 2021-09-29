package com.queendev.report.managers;

import com.queendev.report.models.ReportModel;

import java.util.Date;
import java.util.HashMap;

public class ReportManager {

    public HashMap<String, ReportModel> reports;

    public ReportManager() {
        reports = new HashMap<>();
    }

    public ReportModel getReport(String playerName) {
        return reports.get(playerName.toLowerCase());
    }

    public ReportModel addReport(String player) {
        Date date = new Date();
        String playerName = player.toLowerCase();

        ReportModel report = new ReportModel(playerName, 1, date, false);
        reports.put(playerName, report);
        return report;
    }
}
