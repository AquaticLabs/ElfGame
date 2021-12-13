package com.aquaticlabsdev.elfgame.game.types.peppermint;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import com.aquaticlabsdev.elfgame.game.ElfTimer;
import com.aquaticlabsdev.elfgame.game.GameType;
import com.aquaticlabsdev.elfgame.game.types.peppermint.other.BombTag;
import com.aquaticlabsdev.elfgame.game.types.peppermint.other.PeppermintMap;
import com.aquaticlabsdev.elfroyal.game.ElfGame;
import com.aquaticlabsdev.elfroyal.game.GameState;
import com.aquaticlabsdev.elfroyal.loc.LocationType;
import com.aquaticlabsdev.elfroyal.timer.GameTimer;
import com.aquaticlabsdev.elfroyal.timer.ObjectTimer;
import com.aquaticlabsdev.elfroyal.timer.TimeTickType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.entity.Player;

import java.io.BufferedReader;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Random;
import java.util.UUID;

/**
 * @Author: extremesnow
 * On: 12/12/2021
 * At: 19:48
 */
public class PeppermintGame extends ElfGame {

    private final ElfPlugin plugin;
    private GameTimer preGameTimer;
    private GameTimer postGameTimer;
    private final GameType type = GameType.BOMB_TAG;
    @Setter
    private PeppermintMap map;

    @Getter
    private BombTag bombTag;

    private List<UUID> alivePlayers = new ArrayList<>();;



    public PeppermintGame(ElfPlugin plugin, String id) {
        super(id);
        map = new PeppermintMap(plugin, id);
        this.plugin = plugin;
    }


    @Override
    public void activate() {
        setState(GameState.ACTIVATED);
        map.load();
        for (Map.Entry<UUID, PlayerData> entry : plugin.getGameHandler().getAvailablePlayersToPlay().entrySet()) {
            Player p = Bukkit.getPlayer(entry.getKey());

            if (p == null || !p.isOnline())  {
                return;
            }
            getPlayersToPlay().putIfAbsent(entry.getKey(), p);
        }

        bombTag = new BombTag(plugin,this, 25);
        if (map == null) {
            System.out.println("Cant Start game. Picked Map Failed to load, or doesn't exist..");
            return;
        }
        teleportPlayers();
        startPregameCountdown();
    }


    @Override
    public void startPregameCountdown() {
        setState(GameState.PREGAME);
        preGameTimer = new ElfTimer(plugin, this::start, 10, TimeTickType.DOWN);
        preGameTimer.start();
        System.out.println("Game: " + getGameID() + " starting in " + preGameTimer.getTime() + " seconds");
        alivePlayers.addAll(getPlayersToPlay().keySet());
    }

    @Override
    public void start() {
        System.out.println("Game: " + getGameID() + " started");
        setState(GameState.INGAME);
        pickRandomPlayerToBeTagged();

    }

    @Override
    public void stop() {
        if (preGameTimer != null)
        preGameTimer.stop();
    }

    @Override
    public void finish() {
        setState(GameState.POSTGAME);
        postGameTimer = new ElfTimer(plugin, () -> {
            plugin.getGameHandler().teleportToLobbyLocation(getPlayersToPlay());
        }, 10, TimeTickType.DOWN);
        postGameTimer.start();

    }

    private void teleportPlayers() {
        int i = 0;
        for (Map.Entry<UUID, Player> entry : getPlayersToPlay().entrySet()) {
            Player player = entry.getValue();
            if (map.getLocationList().get(LocationType.PLAYER_SPAWN).get(i) == null) i = 0;
            player.teleport(map.getPlayerSpawns().get(i));
            i++;
        }
    }


    private void pickRandomPlayerToBeTagged() {
        int i = new Random().nextInt(getPlayersToPlay().size());
        bombTag.tagPlayer((UUID)getPlayersToPlay().keySet().toArray()[i]);

    }

    public void killPlayer(Player player) {
        player.sendMessage("You Have Died");
        alivePlayers.remove(player.getUniqueId());
        player.teleport(map.getSpectatorSpawn());

    }

}
