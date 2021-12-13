package com.aquaticlabsdev.elfgame.commands.elfroyal;

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
public class ElfRoyalWandSubcommand implements Subcommand {

    public ElfRoyalWandSubcommand() {
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
        if (args.length > 1) {
            sender.sendMessage("Usage /er wand");
            return true;
        }
        final Player player = (Player) sender;
        player.getInventory().addItem(Utils.wandItem());



        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
