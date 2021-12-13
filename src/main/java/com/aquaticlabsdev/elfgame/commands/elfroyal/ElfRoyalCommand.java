package com.aquaticlabsdev.elfgame.commands.elfroyal;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.commands.BaseCommand;
import com.aquaticlabsdev.elfgame.util.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;

import java.util.List;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 19:37
 */
public class ElfRoyalCommand extends BaseCommand {

    private ElfPlugin plugin;

    public ElfRoyalCommand(ElfPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Permission getPermission() {
        return null;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd) {

        sender.sendMessage(" Nice ");
        //todo

        return true;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
        return onCommand(sender, cmd);
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }

}
