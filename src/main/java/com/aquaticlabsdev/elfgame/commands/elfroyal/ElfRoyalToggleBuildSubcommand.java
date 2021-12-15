package com.aquaticlabsdev.elfgame.commands.elfroyal;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.commands.Subcommand;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import com.aquaticlabsdev.elfgame.util.Permission;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.List;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 20:54
 */
public class ElfRoyalToggleBuildSubcommand implements Subcommand {

    private final ElfPlugin plugin;
    public ElfRoyalToggleBuildSubcommand(ElfPlugin plugin) {
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
            sender.sendMessage("Usage /er build");
            return true;
        }
        final Player player = (Player) sender;
        PlayerData playerData = plugin.getPlayerData(player);

        playerData.setCanBuild(!playerData.isCanBuild());
        player.sendMessage("Building has been set to: " + playerData.isCanBuild());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String[] args) {
        return null;
    }
}
