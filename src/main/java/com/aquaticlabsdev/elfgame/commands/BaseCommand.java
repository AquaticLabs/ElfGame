package com.aquaticlabsdev.elfgame.commands;

import com.aquaticlabsdev.elfgame.util.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

public abstract class BaseCommand {

    public BaseCommand() {}

    public abstract Permission getPermission();

    public abstract boolean onCommand(CommandSender sender, Command cmd);

    public abstract boolean onCommand(CommandSender sender, Command cmd, String[] args);

    public abstract List<String> onTabComplete(CommandSender sender, Command cmd, String[] args);

}
