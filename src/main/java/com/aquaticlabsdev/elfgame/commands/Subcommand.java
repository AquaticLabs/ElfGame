package com.aquaticlabsdev.elfgame.commands;

import com.aquaticlabsdev.elfgame.util.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @Author: extremesnow
 * On: 10/30/2021
 * At: 18:38
 */
public interface Subcommand {

    Permission getPermission();

    boolean onCommand(CommandSender sender, Command cmd, String[] args);

    List<String> onTabComplete(CommandSender sender, Command cmd, String[] args);


}
