package com.aquaticlabsdev.elfgame.commands.elfroyal;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.commands.Subcommand;
import com.aquaticlabsdev.elfgame.util.Permission;
import com.aquaticlabsdev.elfgame.util.Utils;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 20:54
 */
public class ElfRoyalRoleSubcommand implements Subcommand {

    private final ElfPlugin plugin;
    public ElfRoyalRoleSubcommand(ElfPlugin plugin) {
        this.plugin = plugin;
    }

    @Override
    public Permission getPermission() {
        return Permission.SETUP_MAP;
    }

    @Override
    public boolean onCommand(CommandSender sender, Command cmd, String[] args) {
        if (!(sender instanceof Player)) {
            sender.sendMessage("This command cannot be run from the console.");
            return true;
        }
        if (args.length < 1) {
            sender.sendMessage("Usage /er role");
            return true;
        }
        final Player player = (Player) sender;

        if (args[1].equalsIgnoreCase("player")) {
            plugin.getGameHandler().addToGamePlayers(plugin.getPlayerData(player));
            player.sendMessage("Added to players");
            return true;
        }
        if (args[1].equalsIgnoreCase("admin")) {
            plugin.getGameHandler().addToAdminPlayers(plugin.getPlayerData(player));
            player.sendMessage("Added to admins");
            return true;
        }
        if (args[1].equalsIgnoreCase("spec")) {
            plugin.getGameHandler().addToSpectators(plugin.getPlayerData(player));
            player.sendMessage("Added to spectators");
            return true;
        }



        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
