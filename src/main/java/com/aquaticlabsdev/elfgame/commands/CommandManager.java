package com.aquaticlabsdev.elfgame.commands;


import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.util.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.command.TabCompleter;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

/**
 * @Author: extremesnow
 * On: 10/30/2021
 * At: 18:35
 */
public class CommandManager implements CommandExecutor, TabCompleter {

    private final ElfPlugin plugin;
    private final BaseCommand baseCommand;
    private Map<String, Subcommand> subcommands;

    public CommandManager(ElfPlugin plugin, BaseCommand baseCommand, Map<String, Subcommand> subcommands) {
        this.plugin = plugin;
        this.baseCommand = baseCommand;
        this.subcommands = subcommands;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String label, String[] args) {

        Subcommand subcommand;


        if (args.length == 0) {
            if (baseCommand.getPermission() == null) {
                return baseCommand.onCommand(sender, cmd, args);
            } else {
                if (!Permission.hasPermission(sender, baseCommand.getPermission())) {
                    //todo sender.sendMessage(plugin.getFileUtil().getMessageFile().getNoPermission());
                    return true;
                }

                return baseCommand.onCommand(sender, cmd);
            }
        }

        if (subcommands.isEmpty()) {
            return baseCommand.onCommand(sender, cmd, args);
        }

        subcommand = subcommands.get(args[0]);
        if (subcommand == null) {
            subcommand = subcommands.get("help");
        }

        if (subcommand.getPermission() != null && !Permission.hasPermission(sender, subcommand.getPermission())) {
            //todo sender.sendMessage(plugin.getFileUtil().getMessageFile().getNoPermission());
            return true;
        }
        return subcommand.onCommand(sender, cmd, args);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String label, String[] args) {

        Subcommand subcommand;

        if (subcommands.isEmpty()) {
            return baseCommand.onTabComplete(sender, cmd, args);
        }

        if (args.length == 1) {
            List<String> keySet = new ArrayList<>(subcommands.keySet());
            List<String> tabList = new ArrayList<>();
            for (String key : keySet) {
                if (subcommands.get(key).getPermission() == null) {
                    tabList.add(key);
                    continue;
                }
                if (Permission.hasPermission(sender, subcommands.get(key).getPermission()))
                    tabList.add(key);

            }

            List<String> newList = new ArrayList<>();

            StringUtil.copyPartialMatches(args[0], tabList, newList);
            //sort the list
            Collections.sort(newList);
            return tabList;
        } else {
            subcommand = subcommands.get(args[0]);
            if (subcommand == null) {
                try {
                    subcommand = subcommands.get("help");
                } catch (NullPointerException e) {
                    return new ArrayList<>();
                }
            }
        }

        return subcommand.onTabComplete(sender, cmd, args);
    }


}
