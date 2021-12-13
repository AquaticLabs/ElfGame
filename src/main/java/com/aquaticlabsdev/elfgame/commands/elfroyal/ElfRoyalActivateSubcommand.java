package com.aquaticlabsdev.elfgame.commands.elfroyal;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.commands.Subcommand;
import com.aquaticlabsdev.elfgame.game.GameFactory;
import com.aquaticlabsdev.elfgame.game.GameType;
import com.aquaticlabsdev.elfgame.util.Permission;
import com.aquaticlabsdev.elfgame.util.Utils;
import com.aquaticlabsdev.elfgame.util.file.MessageFile;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;
import java.util.Locale;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 20:54
 */
public class ElfRoyalActivateSubcommand implements Subcommand {

    private final ElfPlugin plugin;

    public ElfRoyalActivateSubcommand(ElfPlugin plugin) {
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
        if (args.length < 2) {
            sender.sendMessage("Usage /er activate <GameType>");
            return true;
        }
        final Player player = (Player) sender;
        String gameType = args[1].toUpperCase();
        String mapName = args[2];
        GameType type = GameType.valueOf(gameType);

        plugin.getGameHandler().activateGame(GameFactory.createGame(plugin, type, mapName));
        MessageFile messageFile = plugin.getFileUtil().getMessageFile();

        List<String> activatedMessage = messageFile.getGameActivatedMessage(plugin.getGameHandler().getActiveGame().type());
        for (String s : activatedMessage) {
            player.sendMessage(s.replace("%prefix%", messageFile.getBombTagPrefix()));
        }
        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String[] args) {
        if (args.length == 2) {
            List<GameType> keySet = new ArrayList<>(Arrays.asList(GameType.values()));
            List<String> tabList = new ArrayList<>();
            for (GameType key : keySet) {
                tabList.add(key.name().toLowerCase());
            }

            List<String> newList = new ArrayList<>();

            StringUtil.copyPartialMatches(args[1], tabList, newList);
            //sort the list
            Collections.sort(newList);
            return tabList;
        }
        if (args.length == 3) {
            List<String> tabList = new ArrayList<>();
            tabList.add("<Map-Name>");
            return tabList;
        }

        return new ArrayList<>();
    }
}
