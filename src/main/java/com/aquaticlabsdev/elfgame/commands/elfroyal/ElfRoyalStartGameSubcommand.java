package com.aquaticlabsdev.elfgame.commands.elfroyal;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.commands.Subcommand;
import com.aquaticlabsdev.elfgame.game.GameType;
import com.aquaticlabsdev.elfgame.util.Permission;
import com.aquaticlabsdev.elfgame.util.file.MessageFile;
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
public class ElfRoyalStartGameSubcommand implements Subcommand {

    private final ElfPlugin plugin;
    public ElfRoyalStartGameSubcommand(ElfPlugin plugin) {
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
        MessageFile messageFile = plugin.getFileUtil().getMessageFile();
        if (plugin.getGameHandler().getActiveGame().getGameID().equalsIgnoreCase(args[1])) {
            plugin.getGameHandler().getActiveGame().startPregameCountdown();

        }
        player.sendMessage(messageFile.getBombTagStartNaturally().replace("%prefix%", messageFile.getBombTagPrefix()));
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String[] args) {
        if (args.length == 2) {
            List<String> tabList = new ArrayList<>();
            tabList.add(plugin.getGameHandler().getActiveGame().getGameID());
            return tabList;
        }

        return null;
    }
}
