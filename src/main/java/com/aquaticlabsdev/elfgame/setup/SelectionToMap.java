package com.aquaticlabsdev.elfgame.setup;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.GameType;
import com.aquaticlabsdev.elfgame.game.types.peppermint.other.PeppermintMap;
import com.aquaticlabsdev.elfgame.util.TemporaryBlankMap;
import com.aquaticlabsdev.elfgame.util.Utils;
import com.aquaticlabsdev.elfroyal.game.GameMap;
import com.aquaticlabsdev.elfroyal.loc.Selection;
import org.bukkit.Location;
import org.bukkit.block.Sign;

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
            case SPLEEF:
                return new TemporaryBlankMap();
            default:
                return new TemporaryBlankMap("default");
        }


    }


    private static PeppermintMap bombTagMapFromSelection(ElfPlugin plugin, Selection selection, String mapName) {

        PeppermintMap map = new PeppermintMap(plugin, mapName);

        for (int x = (int) selection.getX1(); x < (int) selection.getX2(); x++) {
            for (int y = (int) selection.getY1(); y < (int) selection.getY2(); y++) {
                for (int z = (int) selection.getZ1(); z < (int) selection.getZ2(); z++) {
                    if (selection.getWorld().getBlockAt(x, y, z).getState() instanceof Sign) {
                        Sign sign = (Sign) selection.getWorld().getBlockAt(x, y, z).getState();
                        org.bukkit.block.data.type.Sign signX = (org.bukkit.block.data.type.Sign) sign.getBlockData();
                        System.out.println(signX.getRotation().name());

                        if (sign.getLine(0).equalsIgnoreCase("spectator_spawn")) {
                            Location location = sign.getLocation();
                            location.setYaw(Utils.faceToYaw(signX.getRotation()));
                            map.setSpectatorSpawn(location);
                            System.out.println("added spectatorspawn");
                        }

                        if (sign.getLine(0).equalsIgnoreCase("player_spawn")) {
                            Location location = sign.getLocation();
                            location.setYaw(Utils.faceToYaw(signX.getRotation()));
                            map.addPlayerSpawn(location);
                            System.out.println("added playerspawn");
                        }


                    }
                }
            }
        }


        return map;
    }

}
