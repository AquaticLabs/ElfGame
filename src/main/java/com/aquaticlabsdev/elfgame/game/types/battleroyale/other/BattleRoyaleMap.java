package com.aquaticlabsdev.elfgame.game.types.battleroyale.other;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.GameType;
import com.aquaticlabsdev.elfgame.game.types.bombtag.other.BombTagMap;
import com.aquaticlabsdev.elfgame.util.file.MapFile;
import com.aquaticlabsdev.elfroyal.game.GameMap;
import com.aquaticlabsdev.elfroyal.loc.LocationType;
import com.aquaticlabsdev.elfroyal.loc.Selection;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.configuration.ConfigurationSection;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: extremesnow
 * On: 12/13/2021
 * At: 21:01
 */
public class BattleRoyaleMap implements GameMap {


    @Getter
    private static final Map<String, BattleRoyaleMap> loadedMaps = new HashMap<>();

    private final ElfPlugin plugin;
    private final String mapName;
    private final GameType gameType = GameType.BATTLE_ROYALE;
    private Selection selection;

    @Getter
    @Setter
    private Location playerSpawn;
    @Getter
    @Setter
    private Location spectatorSpawn;
    @Getter
    @Setter
    private Location lobbyLocation;


    public BattleRoyaleMap(ElfPlugin plugin, String mapName) {
        this.plugin = plugin;
        this.mapName = mapName;
    }

    @Override
    public String getMapName() {
        return mapName;
    }

    @Override
    public void save() {
        MapFile maps = plugin.getFileUtil().getMapFile();


        if (playerSpawn != null) {

            maps.getMapConfig().set("Maps." + gameType.name() + "." + mapName + ".player-spawn",
                    playerSpawn.getWorld().getName()
                            + ":" + playerSpawn.getX()
                            + ":" + playerSpawn.getY()
                            + ":" + playerSpawn.getZ()
                            + ":" + playerSpawn.getYaw()
                            + ":" + playerSpawn.getPitch());
        }
        if (spectatorSpawn != null) {
            maps.getMapConfig().set("Maps." + gameType.name() + "." + mapName + ".spec-loc",
                    spectatorSpawn.getWorld().getName()
                            + ":" + spectatorSpawn.getX()
                            + ":" + spectatorSpawn.getY()
                            + ":" + spectatorSpawn.getZ()
                            + ":" + spectatorSpawn.getYaw()
                            + ":" + spectatorSpawn.getPitch());
        }
        if (lobbyLocation != null) {

            maps.getMapConfig().set("Maps." + gameType.name() + "." + mapName + ".lobby-loc",
                    lobbyLocation.getWorld().getName()
                            + ":" + lobbyLocation.getX()
                            + ":" + lobbyLocation.getY()
                            + ":" + lobbyLocation.getZ()
                            + ":" + lobbyLocation.getYaw()
                            + ":" + lobbyLocation.getPitch());
        }
        if (selection != null) {


            maps.getMapConfig().set("Maps." + gameType.name() + "." + mapName + ".selection.world", selection.getWorld().getName());

            maps.getMapConfig().set("Maps." + gameType.name() + "." + mapName + ".selection.bound1",
                    selection.getX1()
                            + ":" + selection.getY1()
                            + ":" + selection.getZ1());
            maps.getMapConfig().set("Maps." + gameType.name() + "." + mapName + ".selection.bound2",
                    selection.getX2()
                            + ":" + selection.getY2()
                            + ":" + selection.getZ2());

        }
        maps.save();

    }

    @Override
    public void load() {
        ConfigurationSection section = plugin.getFileUtil().getMapFile().getMapConfig().getConfigurationSection("Maps." + gameType.name());
        for (String mapName : section.getKeys(false)) {

            String selWorld = section.getString(mapName + ".selection.world");
            String bound1Str = section.getString(mapName + ".selection.bound1");
            String bound2Str = section.getString(mapName + ".selection.bound2");

            if (selWorld != null && bound1Str != null && bound2Str != null) {

                int x1 = Integer.parseInt(bound1Str.split(":")[0]);
                int y1 = Integer.parseInt(bound1Str.split(":")[1]);
                int z1 = Integer.parseInt(bound1Str.split(":")[2]);

                int x2 = Integer.parseInt(bound2Str.split(":")[0]);
                int y2 = Integer.parseInt(bound2Str.split(":")[1]);
                int z2 = Integer.parseInt(bound2Str.split(":")[2]);

                Location bound1 = new Location(Bukkit.getWorld(selWorld), x1, y1, z1);
                Location bound2 = new Location(Bukkit.getWorld(selWorld), x2, y2, z2);

                this.selection = new Selection(bound1, bound2);
            }

            String playerSpawnStr = section.getString(mapName + ".player-spawn");
            if (playerSpawnStr != null) {
                String[] splitStr = playerSpawnStr.trim().split(":");
                World world = Bukkit.getWorld(splitStr[0]);
                double x = Double.parseDouble(splitStr[1]);
                double y = Double.parseDouble(splitStr[2]);
                double z = Double.parseDouble(splitStr[3]);
                float yaw = Float.parseFloat(splitStr[4]);
                float pitch = Float.parseFloat(splitStr[5]);

                Location playerLoc = new Location(world, x, y, z);
                playerLoc.setYaw(yaw);
                playerLoc.setPitch(pitch);
                this.spectatorSpawn = playerLoc;
            }


            String lobbyLocStr = section.getString(mapName + ".lobby-loc");
            if (lobbyLocStr != null) {
                String[] splitStr = lobbyLocStr.trim().split(":");
                World world = Bukkit.getWorld(splitStr[0]);
                double x = Double.parseDouble(splitStr[1]);
                double y = Double.parseDouble(splitStr[2]);
                double z = Double.parseDouble(splitStr[3]);
                float yaw = Float.parseFloat(splitStr[4]);
                float pitch = Float.parseFloat(splitStr[5]);

                Location lobbyLoc = new Location(world, x, y, z);
                lobbyLoc.setYaw(yaw);
                lobbyLoc.setPitch(pitch);
                this.lobbyLocation = lobbyLoc;
            }


            String spectatorSpawnString = section.getString(mapName + ".spec-loc");
            if (spectatorSpawnString != null) {
                String[] splitStr = spectatorSpawnString.trim().split(":");
                World world = Bukkit.getWorld(splitStr[0]);
                double x = Double.parseDouble(splitStr[1]);
                double y = Double.parseDouble(splitStr[2]);
                double z = Double.parseDouble(splitStr[3]);
                float yaw = Float.parseFloat(splitStr[4]);
                float pitch = Float.parseFloat(splitStr[5]);

                Location spectatorLoc = new Location(world, x, y, z);
                spectatorLoc.setYaw(yaw);
                spectatorLoc.setPitch(pitch);
                this.spectatorSpawn = spectatorLoc;
            }
            loadedMaps.put(mapName, this);

        }

    }


    @Override
    public Selection getMapBounds() {
        return selection;
    }

    @Override
    public Map<LocationType, List<Location>> getLocationList() {
        return null;
    }
}