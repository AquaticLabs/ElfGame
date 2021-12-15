package com.aquaticlabsdev.elfgame.commands.elfroyal;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.commands.Subcommand;
import com.aquaticlabsdev.elfgame.game.GameType;
import com.aquaticlabsdev.elfgame.util.Permission;
import com.aquaticlabsdev.elfgame.util.file.MessageFile;
import com.aquaticlabsdev.elfroyal.game.GameState;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.List;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 20:54
 */
public class ElfRoyalStopGameSubcommand implements Subcommand {

    private final ElfPlugin plugin;
    public ElfRoyalStopGameSubcommand(ElfPlugin plugin) {
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
            sender.sendMessage("Usage /er start <GameType>");
            return true;
        }
        final Player player = (Player) sender;
        if (plugin.getGameHandler().getActiveGame() == null) {
            player.sendMessage("Game not Running.");
            return true;
        }
        MessageFile messageFile = plugin.getFileUtil().getMessageFile();
        if (plugin.getGameHandler().getActiveGame().type().equalsIgnoreCase(args[1].toUpperCase())) {
            plugin.getGameHandler().getActiveGame().stop();
        }
        GameType type = GameType.valueOf(plugin.getGameHandler().getActiveGame().type());

        player.sendMessage("You Stopped the current game. Type: " + type.name());
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String[] args) {
        if (args.length == 2) {
            List<String> tabList = new ArrayList<>();
            if (plugin.getGameHandler().getActiveGame() != null) {
                tabList.add(plugin.getGameHandler().getActiveGame().type());
            }
            return tabList;
        }

        return null;
    }
}
