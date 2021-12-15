package com.aquaticlabsdev.elfgame.setup;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.GameType;
import com.aquaticlabsdev.elfgame.game.types.battleroyale.other.BattleRoyaleMap;
import com.aquaticlabsdev.elfgame.game.types.bombtag.other.BombTagMap;
import com.aquaticlabsdev.elfgame.util.TemporaryBlankMap;
import com.aquaticlabsdev.elfgame.util.Utils;
import com.aquaticlabsdev.elfroyal.game.GameMap;
import com.aquaticlabsdev.elfroyal.loc.Selection;
import org.bukkit.Location;
import org.bukkit.block.Block;
import org.bukkit.block.Sign;
import org.bukkit.block.data.type.WallSign;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 23:46
 */
public class SelectionToMap {



    public static GameMap getMapFromSelection(ElfPlugin plugin, Selection selection, GameType gameType, String mapName) {

        switch (gameType) {
            case BOMB_TAG:
                return bombTagMapFromSelection(plugin, selection, mapName);
            case BATTLE_ROYALE:
                return brMapFromSelection(plugin, selection, mapName);
            default:
                return new TemporaryBlankMap("default");
        }


    }


    private static BombTagMap bombTagMapFromSelection(ElfPlugin plugin, Selection selection, String mapName) {

        BombTagMap map = new BombTagMap(plugin, mapName);

        for (int x = selection.getX1(); x < selection.getX2(); x++) {
            for (int y = selection.getY1(); y < selection.getY2(); y++) {
                for (int z = selection.getZ1(); z < selection.getZ2(); z++) {
                    if (selection.getWorld().getBlockAt(x, y, z).getBlockData() instanceof org.bukkit.block.data.type.Sign) {
                        Sign sign = (Sign) selection.getWorld().getBlockAt(x, y, z).getState();
                        org.bukkit.block.data.type.Sign signX = (org.bukkit.block.data.type.Sign) sign.getBlockData();
                       // System.out.println(signX.getRotation().name());

                        if (sign.getLine(0).equalsIgnoreCase("spectator_spawn")) {
                            Location location = sign.getLocation();
                            location.setYaw(Utils.faceToYaw(signX.getRotation()));
                            map.setSpectatorSpawn(location);
                        }

                        if (sign.getLine(0).equalsIgnoreCase("player_spawn")) {
                            Location location = sign.getLocation();
                            location.setYaw(Utils.faceToYaw(signX.getRotation()));
                            map.addPlayerSpawn(location);
                        }
                        if (sign.getLine(0).equalsIgnoreCase("lobby_spawn")) {
                            Location location = sign.getLocation();
                            location.setYaw(Utils.faceToYaw(signX.getRotation()));
                            map.setLobbyLocation(location);
                        }
                    }
                }
            }
        }
        map.setSelection(selection);
        return map;
    }


    private static BattleRoyaleMap brMapFromSelection(ElfPlugin plugin, Selection selection, String mapName) {

        BattleRoyaleMap map = new BattleRoyaleMap(plugin, mapName);

        for (int x = selection.getX1(); x < selection.getX2(); x++) {
            for (int y = selection.getY1(); y < selection.getY2(); y++) {
                for (int z = selection.getZ1(); z < selection.getZ2(); z++) {
                    if (selection.getWorld().getBlockAt(x, y, z).getBlockData() instanceof org.bukkit.block.data.type.Sign) {
                        Sign sign = (Sign) selection.getWorld().getBlockAt(x, y, z).getState();
                        org.bukkit.block.data.type.Sign signX = (org.bukkit.block.data.type.Sign) sign.getBlockData();

                        if (sign.getLine(0).equalsIgnoreCase("spectator_spawn")) {
                            Location location = sign.getLocation();
                            location.setYaw(Utils.faceToYaw(signX.getRotation()));
                            map.setSpectatorSpawn(location);
                        }

                        if (sign.getLine(0).equalsIgnoreCase("player_spawn")) {
                            Location location = sign.getLocation();
                            location.setYaw(Utils.faceToYaw(signX.getRotation()));
                            map.setPlayerSpawn(location);
                        }
                        if (sign.getLine(0).equalsIgnoreCase("lobby_spawn")) {
                            Location location = sign.getLocation();
                            location.setYaw(Utils.faceToYaw(signX.getRotation()));
                            map.setLobbyLocation(location);
                        }
                        if (sign.getLine(0).equalsIgnoreCase("ring_center")) {
                            Location location = sign.getLocation();
                            location.setYaw(Utils.faceToYaw(signX.getRotation()));
                            map.setRingCenterLocation(location);
                        }
                    }
                }
            }
        }
        map.setSelection(selection);
        return map;
    }

}
