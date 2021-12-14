package com.aquaticlabsdev.elfgame.game.types.bombtag.other;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.game.GameType;
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

import java.util.ArrayList;
import java.util.Collections;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 20:32
 */
public class BombTagMap implements GameMap {


    @Getter
    private static final Map<String, BombTagMap> loadedMaps = new HashMap<>();


    private final ElfPlugin plugin;
    private final String mapName;
    private final GameType gameType = GameType.BOMB_TAG;
    private Selection selection;

    @Getter
    private List<Location> playerSpawns = new ArrayList<>();
    @Getter
    @Setter
    private Location spectatorSpawn;
    @Getter
    @Setter
    private Location lobbyLocation;


    public BombTagMap(ElfPlugin plugin, String mapName) {
        this.plugin = plugin;
        this.mapName = mapName;
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


            List<Location> spawnLocations = new ArrayList<>();
            for (String spawnLoc : section.getStringList(mapName + ".spawns")) {
                String[] splitStr = spawnLoc.trim().split(":");
                World world = Bukkit.getWorld(splitStr[0]);
                double x = Double.parseDouble(splitStr[1]);
                double y = Double.parseDouble(splitStr[2]);
                double z = Double.parseDouble(splitStr[3]);
                float yaw = Float.parseFloat(splitStr[4]);
                float pitch = Float.parseFloat(splitStr[5]);

                Location loc = new Location(world, x, y, z);
                loc.setYaw(yaw);
                loc.setPitch(pitch);
                spawnLocations.add(loc);
            }
            playerSpawns = spawnLocations;
        }

        loadedMaps.put(mapName, this);
    }


    @Override
    public void save() {
        MapFile maps = plugin.getFileUtil().getMapFile();
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
                            + ":" +  selection.getY1()
                            + ":" +  selection.getZ1());
            maps.getMapConfig().set("Maps." + gameType.name() + "." + mapName + ".selection.bound2",
                    selection.getX2()
                            + ":" +  selection.getY2()
                            + ":" +  selection.getZ2());

        }

        List<String> spawns = new ArrayList<>();
        for (Location spawn : playerSpawns) {
            spawns.add(spawn.getWorld().getName()
                    + ":" + spawn.getX()
                    + ":" + spawn.getY()
                    + ":" + spawn.getZ()
                    + ":" + spawn.getYaw()
                    + ":" + spawn.getPitch());
        }
        maps.getMapConfig().set("Maps." + gameType.name() + "." + mapName + ".spawns", spawns);


        maps.save();


    }

    public void addPlayerSpawn(Location location) {
        playerSpawns.add(location);
    }


    @Override
    public String getMapName() {
        return mapName;
    }

    @Override
    public Selection getMapBounds() {
        return selection;
    }

    public void delete() {
        unload();
    }

    public void unload() {
        loadedMaps.remove(this);
    }

    @Override
    public Map<LocationType, List<Location>> getLocationList() {
        Map<LocationType, List<Location>> map = new HashMap<>();

        map.put(LocationType.LOBBY, Collections.singletonList(lobbyLocation));
        map.put(LocationType.PLAYER_SPAWN, playerSpawns);
        map.put(LocationType.SPECTATOR, Collections.singletonList(spectatorSpawn));

        return map;
    }
}
