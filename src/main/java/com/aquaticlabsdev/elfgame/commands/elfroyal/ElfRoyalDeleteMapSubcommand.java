package com.aquaticlabsdev.elfgame.commands.elfroyal;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.commands.Subcommand;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import com.aquaticlabsdev.elfgame.game.GameType;
import com.aquaticlabsdev.elfgame.game.types.bombtag.other.BombTagMap;
import com.aquaticlabsdev.elfgame.setup.SelectionToMap;
import com.aquaticlabsdev.elfgame.util.Permission;
import com.aquaticlabsdev.elfgame.util.file.MapFile;
import com.aquaticlabsdev.elfroyal.game.GameMap;
import com.aquaticlabsdev.elfroyal.loc.Selection;
import org.bukkit.command.Command;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;
import org.bukkit.util.StringUtil;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 20:54
 */
public class ElfRoyalDeleteMapSubcommand implements Subcommand {

    private final ElfPlugin plugin;

    public ElfRoyalDeleteMapSubcommand(ElfPlugin plugin) {
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
        if (args.length < 3) {
            sender.sendMessage("Usage /er deleteMap <Game Type> <Map Name>");
            return true;
        }
        Player player = (Player) sender;
        String gameTypeStr = args[1].toUpperCase();
        GameType type = GameType.valueOf(gameTypeStr);

        String mapName = args[2];

        MapFile mapFile = plugin.getFileUtil().getMapFile();
        if (mapFile.getMapConfig().get("Maps." + type.name() + "." + mapName) == null) {
            player.sendMessage("This map doesn't exist.");
            return true;
        }

        mapFile.getMapConfig().set("Maps." + type.name()  + "." + mapName, null);
        mapFile.save();
        player.sendMessage("Map Deleted");

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
