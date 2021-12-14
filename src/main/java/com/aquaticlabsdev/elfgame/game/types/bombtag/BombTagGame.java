package com.aquaticlabsdev.elfgame.game.types.bombtag;

import com.aquaticlabsdev.elfgame.ElfPlugin;
import com.aquaticlabsdev.elfgame.data.PlayerData;
import com.aquaticlabsdev.elfgame.game.ElfTimer;
import com.aquaticlabsdev.elfgame.game.GameType;
import com.aquaticlabsdev.elfgame.game.types.bombtag.other.BombTag;
import com.aquaticlabsdev.elfgame.game.types.bombtag.other.BombTagMap;
import com.aquaticlabsdev.elfgame.game.types.bombtag.other.BombTagPregameTimer;
import com.aquaticlabsdev.elfgame.util.file.MessageFile;
import com.aquaticlabsdev.elfroyal.game.ElfGame;
import com.aquaticlabsdev.elfroyal.game.GamePlacements;
import com.aquaticlabsdev.elfroyal.game.GameState;
import com.aquaticlabsdev.elfroyal.loc.LocationType;
import com.aquaticlabsdev.elfroyal.timer.GameTimer;
import com.aquaticlabsdev.elfroyal.timer.TimeTickType;
import lombok.Getter;
import lombok.Setter;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;

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
public class BombTagGame extends ElfGame {

    private final ElfPlugin plugin;
    private GameTimer preGameTimer;
    private GameTimer postGameTimer;
    private final GameType type = GameType.BOMB_TAG;
    @Getter
    private boolean activated;
    @Setter
    private BombTagMap map;
    @Getter
    private BombTag bombTag;
    @Getter
    private GamePlacements placements = new GamePlacements();

    private List<UUID> alivePlayers = new ArrayList<>();


    public BombTagGame(ElfPlugin plugin, String id) {
        super(id);
        map = new BombTagMap(plugin, id);
        this.plugin = plugin;
    }


    @Override
    public void activate() {
        setState(GameState.ACTIVATED);
        map.load();
        for (Map.Entry<UUID, PlayerData> entry : plugin.getGameHandler().getAvailablePlayersToPlay().entrySet()) {
            Player p = Bukkit.getPlayer(entry.getKey());

            if (p == null || !p.isOnline()) {
                return;
            }
            getPlayersToPlay().putIfAbsent(entry.getKey(), p);
        }

        bombTag = new BombTag(plugin, this, 25);
        if (map == null) {
            System.out.println("Cant Start game. Picked Map Failed to load, or doesn't exist..");
            return;
        }
        teleportPlayersToGameLobby();
    }


    @Override
    public void startPregameCountdown() {
        setState(GameState.PREGAME);
        preGameTimer = new BombTagPregameTimer(plugin, () -> {
            teleportPlayersToGame();
            start();
        }, this, 10, TimeTickType.DOWN, false);
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
    public String type() {
        return type.name();
    }

    @Override
    public void stop() {
        if (preGameTimer != null)
            preGameTimer.stop();
    }

    @Override
    public void finish() {
        setState(GameState.POSTGAME);
        List<String> winMessage = plugin.getFileUtil().getMessageFile().getBombTagWinners();
        for (String s : winMessage) {
            UUID uuid1 = getPlacements().getByPlacement(1);
            UUID uuid2 = getPlacements().getByPlacement(2);
            UUID uuid3 = getPlacements().getByPlacement(3);
            String player1 = uuid1 != null ? Bukkit.getOfflinePlayer(uuid1).getName() : "";
            String player2 = uuid2 != null ? Bukkit.getOfflinePlayer(uuid2).getName() : "";
            String player3 = uuid3 != null ? Bukkit.getOfflinePlayer(uuid3).getName() : "";


            broadcastGameMessage(s
                    .replace("%1st%", player1)
                    .replace("%2nd%", player2)
                    .replace("%3rd%", player3)


            );
        }

        postGameTimer = new ElfTimer(plugin, () -> {
            plugin.getGameHandler().teleportToLobbyLocation(getPlayersToPlay());
        }, 10, TimeTickType.DOWN, false);
        postGameTimer.start();

    }

    @Override
    public void broadcastGameMessage(String string) {
        MessageFile messageFile = plugin.getFileUtil().getMessageFile();
        for (Player player : Bukkit.getOnlinePlayers()) {
            player.sendMessage(string.replace("%prefix%", messageFile.getBombTagPrefix()));
        }
    }

    @Override
    public void playerAbandon(Player player) {
        alivePlayers.remove(player.getUniqueId());
        getPlayersToPlay().remove(player.getUniqueId());
        checkWinner();
    }

    private void teleportPlayersToGameLobby() {
        for (Map.Entry<UUID, Player> entry : getPlayersToPlay().entrySet()) {
            Player player = entry.getValue();
            player.teleport(map.getLobbyLocation());
        }
    }

    private void teleportPlayersToGame() {
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
        bombTag.tagPlayer((UUID) getPlayersToPlay().keySet().toArray()[i]);

    }

    public void killPlayer(Player player) {
        MessageFile messageFile = plugin.getFileUtil().getMessageFile();
        placements.addPlacement(player.getUniqueId(), alivePlayers.size());
        player.sendMessage(messageFile.getBombTagBombPlayerDied().replace("%prefix%", messageFile.getBombTagPrefix()).replace("%placement%", (alivePlayers.size()) + ""));
        broadcastGameMessage(messageFile.getBombTagAnnounceBombExplode().replace("%player_name%", player.getName()));
        alivePlayers.remove(player.getUniqueId());
        player.teleport(map.getSpectatorSpawn());
        checkWinner();

    }

    public void checkWinner() {
        if (alivePlayers.size() <= 1) {
            placements.addPlacement(alivePlayers.get(0), 1);
            finish();
        }
        pickRandomPlayerToBeTagged();
    }

}
