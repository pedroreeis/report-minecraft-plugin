package com.queendev.report.commands;

import com.queendev.report.Report;
import com.queendev.report.models.ReportModel;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.HashMap;

public class ReportCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cComando apenas para jogadores.");
            return false;
        }
        Player p = (Player)sender;

        if(args.length > 0) {
            Player playerTarget = Bukkit.getOfflinePlayer(args[0]).getPlayer();
            HashMap<String, ReportModel> reports = Report.getPlugin().manager.reports;

            if(playerTarget != null) {
                if(reports.containsKey(playerTarget.getName().toLowerCase())) {
                    ReportModel report = reports.get(playerTarget.getName().toLowerCase());
                    double nowAmount = report.getAmountReport();
                    double newAmount = nowAmount+1;

                    report.setAmountReport(newAmount);
                    p.sendMessage("§fVocê reportou o jogador: §a" + playerTarget.getName().toLowerCase() + " §fObrigado pela contribuição!");
                    return false;
                }

                Report.getPlugin().manager.addReport(playerTarget.getName().toLowerCase());
                p.sendMessage("§fVocê reportou o jogador: §a" + playerTarget.getName().toLowerCase() + " §fObrigado pela contribuição!");
            }else {
                p.sendMessage("§c* Este jogador não existe.");
                return false;
            }
        }else {
            p.sendMessage("§c* Use /report <player>");
            return false;
        }
        return true;
    }
}
