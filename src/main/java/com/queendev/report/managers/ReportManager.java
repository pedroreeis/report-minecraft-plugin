package com.queendev.report.managers;

import com.queendev.report.models.ReportModel;
import org.bukkit.Bukkit;
import org.bukkit.OfflinePlayer;

import java.util.HashMap;

public class ReportManager {

    public HashMap<String, ReportModel> reports;

    public ReportManager() {
        reports = new HashMap<>();
    }

    public ReportModel getReport(String playerName) {
        return reports.get(playerName.toLowerCase());
    }
}
