package com.aquaticlabsdev.elfgame.game;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import com.aquaticlabsdev.elfgame.util.file.ConfigFile;
import com.aquaticlabsdev.elfroyal.game.ElfGame;
import com.aquaticlabsdev.elfroyal.game.GameState;
import lombok.Getter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.World;
import org.bukkit.entity.Player;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 20:03
 */
@Getter
public class GameHandler {


    private ElfPlugin plugin;
    private Location mainLobbyLoc;
    private ElfGame activeGame;
    private Map<UUID, PlayerData> admins = new HashMap<>();
    private Map<UUID, PlayerData> availablePlayersToPlay = new HashMap<>();
    private Map<UUID, PlayerData> spectators = new HashMap<>();

    public GameHandler(ElfPlugin plugin) {
        this.plugin = plugin;
        ConfigFile configFile = plugin.getFileUtil().getConfigFile();

        String lobbySpawn = configFile.getMainLobbyLocation();
        if (lobbySpawn == null) {
            System.out.println("You must setup a Main Lobby Location");
            return;
        }

        String[] splitStr = lobbySpawn.trim().split(":");
        World world = Bukkit.getWorld(splitStr[0]);
        double x = Double.parseDouble(splitStr[1]);
        double y = Double.parseDouble(splitStr[2]);
        double z = Double.parseDouble(splitStr[3]);
        float yaw = Float.parseFloat(splitStr[4]);
        float pitch = Float.parseFloat(splitStr[5]);

        Location lobbyLoc = new Location(world, x, y, z);
        lobbyLoc.setYaw(yaw);
        lobbyLoc.setPitch(pitch);
        this.mainLobbyLoc = lobbyLoc;
    }


    public void activateGame(ElfGame elfGame) {
        if (activeGame != null) {
            if (activeGame.getState() != GameState.ENDED) {
                activeGame.stop();
            }
        }
        this.activeGame = elfGame;
        activeGame.activate();
    }

    public void cancelActiveGame() {
        if (activeGame != null) {
            if (activeGame.getState() != GameState.ENDED) {
                activeGame.stop();
            }
        }
    }

    public void teleportToLobbyLocation(Map<UUID, Player> map) {

        for (Player player : map.values()) {
            player.teleport(mainLobbyLoc);
        }
    }


    public void addToGamePlayers(PlayerData data) {
        admins.remove(data.getUuid());
        spectators.remove(data.getUuid());
        availablePlayersToPlay.putIfAbsent(data.getUuid(), data);
    }

    public void addToAdminPlayers(PlayerData data) {
        availablePlayersToPlay.remove(data.getUuid());
        spectators.remove(data.getUuid());
        admins.putIfAbsent(data.getUuid(), data);
    }

    public void addToSpectators(PlayerData data) {
        admins.remove(data.getUuid());
        availablePlayersToPlay.remove(data.getUuid());
        spectators.putIfAbsent(data.getUuid(), data);
    }


}
