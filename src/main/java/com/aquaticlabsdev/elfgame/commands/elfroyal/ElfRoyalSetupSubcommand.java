package com.aquaticlabsdev.elfgame.commands.elfroyal;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.commands.Subcommand;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import com.aquaticlabsdev.elfgame.game.GameType;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.other.BattleRoyaleMap;
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
public class ElfRoyalSetupSubcommand implements Subcommand {

    private final ElfPlugin plugin;

    public ElfRoyalSetupSubcommand(ElfPlugin plugin) {
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
            sender.sendMessage("Usage /er setup <map-name> <game-type>");
            return true;
        }
        Player player = (Player) sender;
        PlayerData data = plugin.getPlayerData(player);

        if (data.getSelection() == null) {
            player.sendMessage("You need to have a valid selection.");
            return true;
        }

        Selection selection = data.getSelection();
        String mapName = args[1];
        String gameTypeStr = args[2].toUpperCase();
        GameType type = GameType.valueOf(gameTypeStr);

        MapFile mapFile = plugin.getFileUtil().getMapFile();
        if (mapFile.getMapConfig().get("Maps." + type.name() + "." + mapName) != null) {
            player.sendMessage("This map already exists.");
            return true;
        }

        GameMap map = SelectionToMap.getMapFromSelection(plugin, selection, type, mapName);
        map.save();
        player.sendMessage("Map Created");
        switch (type) {
            case BOMB_TAG: {
                BombTagMap pmap = (BombTagMap) map;
                String specSet = pmap.getSpectatorSpawn() != null ? "Set." : "Not Set";
                player.sendMessage("Spectator Spawn: " + specSet);

                String lobbySet = pmap.getLobbyLocation() != null ? "Set." : "Not Set";
                player.sendMessage("Lobby Spawn: " + lobbySet);

                int playerSpawns = pmap.getPlayerSpawns().size();
                player.sendMessage("Player Spawns: " + playerSpawns);
            }
            case BATTLE_ROYALE: {
                BattleRoyaleMap brMap = (BattleRoyaleMap) map;

                String playerSpawnSet = brMap.getPlayerSpawn() != null ? "Set." : "Not Set";
                player.sendMessage("Player Spawn: " + playerSpawnSet);

                String specSet = brMap.getSpectatorSpawn() != null ? "Set." : "Not Set";
                player.sendMessage("Spectator Spawn: " + specSet);

                String lobbySet = brMap.getLobbyLocation() != null ? "Set." : "Not Set";
                player.sendMessage("Lobby Spawn: " + lobbySet);
            }

        }


        return true;
    }

    @Override
    public List<String> onTabComplete(CommandSender sender, Command cmd, String[] args) {
        if (args.length == 3) {
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
        if (args.length == 2) {
            List<String> tabList = new ArrayList<>();
            tabList.add("<Map-Name>");
            return tabList;
        }

        return new ArrayList<>();
    }
}
