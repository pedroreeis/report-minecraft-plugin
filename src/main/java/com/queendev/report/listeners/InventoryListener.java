package com.queendev.report.listeners;

import com.queendev.report.Report;
import com.queendev.report.models.ReportModel;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.HashMap;

public class InventoryListener implements Listener {

    @EventHandler
    void onInteract(InventoryClickEvent e) {
        Player p = (Player) e.getWhoClicked();
        if (e.getView().getTitle().equalsIgnoreCase(Report.getPlugin().config.getString("Menu.name").replace("&", "§"))) {

            ItemMeta itemReport = e.getCurrentItem().getItemMeta();
            HashMap<String, ReportModel> reports = Report.getPlugin().manager.reports;
            String playerName = itemReport.getDisplayName();

            if(reports.containsKey(playerName)) {
                if (e.getCurrentItem().getItemMeta().getDisplayName().contains(playerName)) {
                    Player playerTarget = Bukkit.getOfflinePlayer(playerName).getPlayer();

                    if(e.isLeftClick()) {
                        if(playerTarget == null) {
                            p.sendMessage("§c* Este jogador não existe ou está offline.");
                            return;
                        }

                        if(p.hasPermission("report.admin")) {
                            p.teleport(playerTarget);
                            ReportModel report = reports.get(playerTarget.getName().toLowerCase());
                            report.setVisualized(true);
                            p.sendMessage("§aVocê foi teleportado para o jogador reportado: §f" + playerTarget.getName());
                        }
                    }

                    if(e.isRightClick()) {
                        if(p.hasPermission("report.admin")) {
                            reports.remove(playerTarget.getName().toLowerCase());
                            e.getView().close();
                            p.sendMessage("§cVocê excluiu o reporte sobre o jogador: §f" + playerTarget.getName());
                        }
                    }

                }
            }
        }
    }
}
