package com.queendev.report.commands;

import com.queendev.report.Report;
import com.queendev.report.managers.ConfigManager;
import com.queendev.report.models.ReportModel;
import com.queendev.report.utils.ItemBuilder;
import com.queendev.report.utils.Scroller;
import org.bukkit.Material;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemFlag;
import org.bukkit.inventory.ItemStack;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.Locale;
import java.util.stream.Collectors;

public class ReportsCommand implements CommandExecutor {
    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {
        if(!(sender instanceof Player)) {
            sender.sendMessage("§cComando apenas para jogadores.");
            return false;
        }

        Player p = (Player)sender;

        if(cmd.getName().equalsIgnoreCase("reports")) {
            if(p.hasPermission("report.admin")) {
                ConfigManager menuCondig = Report.getPlugin().config;
                HashMap<String, ReportModel> reports = Report.getPlugin().manager.reports;
                ArrayList<ItemStack> items = new ArrayList<>();

                for (ReportModel report : reports.values()) {
                    String amountReport = String.valueOf(report.getAmountReport());
                    String vizualized = report.isVisualized() ? "§aSim" : "§cNão";
                    SimpleDateFormat dateFormattter = new SimpleDateFormat("dd/MM/yyyy", Locale.US);
                    String horario = dateFormattter.format(report.getDate());
                    ItemStack it = new ItemBuilder(new ItemStack(Material.valueOf(menuCondig.getString("Menu.report_item.material")), 1, menuCondig.getShort("Menu.report_item.data")))
                            .setName(report.getPlayerName())
                            .setLore(menuCondig.getStringList("Menu.report_item.lore").stream().map(line -> line.replace("{player}", report.getPlayerName()).replace("{reportAmount}", amountReport).replace("{isVizualized}", vizualized).replace("{date}", horario).replace("&", "§")).collect(Collectors.toList()))
                            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                            .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                            .toItemStack();
                    items.add(it);
                }
                int size = menuCondig.getInt("Menu.slots");

                ItemStack nothingItem = null;
                if (items.isEmpty()) {
                    nothingItem = new ItemBuilder(new ItemStack(Material.valueOf(menuCondig.getString("Menu.nothing_item.material")), 1, menuCondig.getShort("Menu.nothing_item.data")))
                            .setName(menuCondig.getString("Menu.nothing_item.name").replace("&", "§"))
                            .setLore(menuCondig.getStringList("Menu.nothing_item.lore").stream().map(line -> line.replace("&", "§")).collect(Collectors.toList()))
                            .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                            .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                            .toItemStack();
                }

                ItemStack nextPageItem = new ItemBuilder(new ItemStack(Material.valueOf(menuCondig.getString("Menu.next_page_item.material")), 1, menuCondig.getShort("Menu.next_page_item.data")))
                        .setName(menuCondig.getString("Menu.next_page_item.name").replace("&", "§"))
                        .setLore(menuCondig.getStringList("Menu.next_page_item.lore").stream().map(line -> line.replace("&", "§")).collect(Collectors.toList()))
                        .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                        .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .toItemStack();

                ItemStack previousPageItem = new ItemBuilder(new ItemStack(Material.valueOf(menuCondig.getString("Menu.previous_page_item.material")), 1, menuCondig.getShort("Menu.previous_page_item.data")))
                        .setName(menuCondig.getString("Menu.previous_page_item.name").replace("&", "§"))
                        .setLore(menuCondig.getStringList("Menu.previous_page_item.lore").stream().map(line -> line.replace("&", "§")).collect(Collectors.toList()))
                        .addItemFlag(ItemFlag.HIDE_ENCHANTS)
                        .addItemFlag(ItemFlag.HIDE_ATTRIBUTES)
                        .toItemStack();

                Scroller scroller = Scroller.builder()
                        .withName(menuCondig.getString("Menu.name").replace("&", "§"))
                        .withItems(items.isEmpty() ? Collections.singletonList(nothingItem) : items)
                        .withSize(size)
                        .withAllowedSlots(items.isEmpty() ? Collections.singletonList(menuCondig.getInt("Menu.nothing_item.slot")) : menuCondig.getIntegerList("Menu.allowed_slots"))
                        .withNextPageSlot(nothingItem != null ? 0 : menuCondig.getInt("Menu.next_page_slot"))
                        .withPreviousPageSlot(nothingItem != null ? 1 : menuCondig.getInt("Menu.previous_page_slot"))
                        .withNextPageItem(nextPageItem)
                        .withPreviousPageItem(previousPageItem)
                        .build();
                scroller.open(p);
            }
            }else {
                p.sendMessage("§cVocê não tem permissão para fazer isso.");
            }
        return true;
    }
}
