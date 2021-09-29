package com.queendev.report;

import com.queendev.report.commands.ReportCommand;
import com.queendev.report.commands.ReportsCommand;
import com.queendev.report.listeners.InventoryListener;
import com.queendev.report.managers.ConfigManager;
import com.queendev.report.managers.ReportManager;
import com.queendev.report.process.ReportProcess;
import com.queendev.report.repository.Database;
import com.queendev.report.repository.providers.MySQL;
import com.queendev.report.repository.providers.SQLite;
import org.bukkit.Bukkit;
import org.bukkit.plugin.java.JavaPlugin;

public class Report extends JavaPlugin {

    private static Report instance;
    public ConfigManager config;
    public Database database;
    public ReportManager manager;

    public static Report getPlugin() {
        return instance;
    }

    @Override
    public void onEnable() {
        instance = this;

        manager = new ReportManager();
        config = new ConfigManager(null, "config.yml", false);
        database = config.getBoolean("MySQL.ativar") ? new MySQL() : new SQLite();
        database.open();

        getCommand("reports").setExecutor(new ReportsCommand());
        getCommand("report").setExecutor(new ReportCommand());

        Bukkit.getPluginManager().registerEvents(new InventoryListener(), this);

        ReportProcess.loadReports();
    }

    @Override
    public void onDisable() {
        ReportProcess.saveReports();
        database.close();
    }
}
